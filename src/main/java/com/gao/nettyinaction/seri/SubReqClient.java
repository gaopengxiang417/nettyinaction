package com.gao.nettyinaction.seri;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * User: wangchen
 * Date: 2016/12/12
 * Time: 00:02
 */
public class SubReqClient {

    public static void main(String[] args) throws InterruptedException {
        new SubReqClient().connet(8080, "127.0.0.1");

    }

    public void connet(int port, String host) throws InterruptedException {

        NioEventLoopGroup grou = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(grou)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ObjectDecoder(1024,
                                    ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new SubReqClientHandler());
                        }
                    });

            ChannelFuture sync = bootstrap.connect(host, port).sync();

            sync.channel().closeFuture().sync();
        }finally {
            grou.shutdownGracefully();
        }
    }

    class SubReqClientHandler extends SimpleChannelInboundHandler {

        public SubReqClientHandler() {
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            for (int i = 0; i < 10; i++) {
                ctx.write(subReq(i));
            }
            ctx.flush();
        }


        private SubscribeReq subReq(int i) {
            SubscribeReq req = new SubscribeReq();
            req.setAddress("文一西路");
            req.setPhoneNumber("123121212");
            req.setProductName("netty in action");
            req.setUserName("wangchen");
            req.setSubReqID(i);

            return req;
        }

        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("recevie server response : [" + msg + " ]");
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }
    }

}
