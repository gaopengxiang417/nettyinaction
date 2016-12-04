package com.gao.nettyinaction.common;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.util.Date;

/**
 * User: wangchen
 * Date: 2016/12/4
 * Time: 14:27
 */
public class TimeServer {

    public static void main(String[] args) throws InterruptedException {
        new TimeServer().bind(8080);
    }

    public void bind(int port) throws InterruptedException {
        //配置线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());

            //绑定端口，同步等待成功
            ChannelFuture channelFuture = bootstrap.bind(port).sync();

            //等待服务器监听端口关闭
            channelFuture.channel().closeFuture().sync();
        }finally {
            //优雅的关闭，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
            ch.pipeline().addLast(new StringDecoder());
            ch.pipeline().addLast(new TimeServerHandler());
        }
    }

    private class TimeServerHandler extends ChannelInboundHandlerAdapter {

        private int counter;

        /*@Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];

            buf.readBytes(bytes);
            String s = new String(bytes, "utf-8");

            System.out.println("time server recivce order:" + s);

            String currentTime = "query time order".equalsIgnoreCase(s) ? new Date(System.currentTimeMillis()).toString() : "bad order";

            ByteBuf byteBuf = Unpooled.copiedBuffer(currentTime.getBytes());

            ctx.write(byteBuf);

        }*/

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String body = (String) msg;
            /*ByteBuf buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];

            buf.readBytes(bytes);
            //进行字符串的拼接
            String body = new String(bytes, "UTF-8").substring(0, bytes.length - System.getProperty("line.separator").length());*/
            //进行计数
            System.out.println("the time server receiver order:" + body + "; the counter is :" + ++counter);
            //构造当前时间
            String currentTime = "query time order".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "bad order";

            currentTime = currentTime + System.getProperty("line.separator");

            ByteBuf byteBuf = Unpooled.copiedBuffer(currentTime.getBytes());

            ctx.writeAndFlush(byteBuf);
        }

        /**
         * 解决TCP的拆包和粘包的功能
         * @param ctx
         * @throws Exception
         */


        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }
    }
}
