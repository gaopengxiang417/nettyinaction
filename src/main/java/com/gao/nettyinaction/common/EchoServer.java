package com.gao.nettyinaction.common;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * User: wangchen
 * Date: 2016/12/4
 * Time: 16:03
 */
public class EchoServer {

    public static void main(String[] args) throws InterruptedException {
        new EchoServer().bind(8080);
    }

    /**
     * 服务端启动绑定逻辑
     * @param port
     */
    public void bind(int port) throws InterruptedException {
        //配置服务端的线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //ByteBuf byteBuf = Unpooled.copiedBuffer("$_".getBytes());
                            //ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, byteBuf));
                            ch.pipeline().addLast(new FixedLengthFrameDecoder(20));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            ChannelFuture sync = serverBootstrap.bind(port).sync();

            sync.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

/**
 * 服务端接收到以后真正的处理请求逻辑
 */
class EchoServerHandler extends SimpleChannelInboundHandler{

    int counter = 0;

    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println(msg);
        //进行消息转化
        /*String body = (String) msg;
        System.out.println("this is : " + ++counter + " times recevie client : " + body);

        body += "$_";
        ByteBuf byteBuf = Unpooled.copiedBuffer(body.getBytes());

        ctx.writeAndFlush(byteBuf);*/
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
