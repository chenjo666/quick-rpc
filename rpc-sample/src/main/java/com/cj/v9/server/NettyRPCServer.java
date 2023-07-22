package com.cj.v9.server;

import com.cj.v9.codec.RPCDecoder;
import com.cj.v9.codec.RPCEncoder;
import com.cj.v9.provider.ServiceProvider;
import com.cj.v9.server.handler.NettyRPCServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NettyRPCServer extends AbstractRPCServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyRPCServer.class);


    public NettyRPCServer(int port, ServiceProvider serviceProvider) {
        super(port, serviceProvider);
    }

    @Override
    public void startServer() {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        // 解码器
                        nioSocketChannel.pipeline().addLast(new RPCDecoder());
                        // 管道数据处理器（rpcRequest）
                        nioSocketChannel.pipeline().addLast(new NettyRPCServerHandler(getServiceProvider()));
                        // 编码器
                        nioSocketChannel.pipeline().addLast(new RPCEncoder());
                    }
                })
                .bind(getPort());
        logger.info("Netty 服务端等待连接...");
    }

    @Override
    public void stopServer() {

    }
}
