package com.gao.nettyinaction.common;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * User: wangchen
 * Date: 2016/12/4
 * Time: 15:03
 */
public class TimeClient {

    public void connect(int port, String host) throws InterruptedException {

        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new TimeClientHandler());
                        }
                    });

            //发起异步操作
            ChannelFuture sync = bootstrap.connect(host, port).sync();

            sync.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        new TimeClient().connect(8080, "127.0.0.1");
    }

    /**
     * 分析TCP的拆包和粘包的功能
     */
    private class TimeClientHandler extends SimpleChannelInboundHandler {

        private int counter;

        private byte[] bytes;

        public TimeClientHandler() {
            bytes = ("query time order" + System.getProperty("line.separator")).getBytes();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println(cause);
            ctx.close();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ByteBuf message = null;
            for (int i = 0; i < 100; i++) {
                message = Unpooled.buffer(bytes.length);
                message.writeBytes(bytes);
                ctx.writeAndFlush(message);
            }
        }

        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            String s = (String) msg;
            /*ByteBuf buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];

            buf.readBytes(bytes);
            String s = new String(bytes, "utf-8");*/

            System.out.println("now is : " + s + "; the counter is" + ++counter);
        }
    }

    /*private class TimeClientHandler extends SimpleChannelInboundHandler {

        private final ByteBuf firstMessage;

        public TimeClientHandler() {
            byte[] bytes = "query time order".getBytes();
            firstMessage = Unpooled.buffer(bytes.length);
            firstMessage.writeBytes(bytes);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println(cause);
            ctx.close();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(firstMessage);
        }

        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];

            buf.readBytes(bytes);
            String s = new String(bytes, "utf-8");

            System.out.println("now is:" + s);
        }
    }*/
}

