package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import netty.codec.RPCDecoder;
import netty.codec.RPCEncoder;
import netty.handler.RpcHeartbeatHandler;
import netty.handler.RpcRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.ServiceProvider;
import server.AbstractRpcServer;


public class NettyRpcServer extends AbstractRpcServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyRpcServer.class);


    public NettyRpcServer(ServiceProvider serviceProvider) {
        super(serviceProvider);
    }

    @Override
    public void startServer() {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        // 心跳检测
                        nioSocketChannel.pipeline().addLast(new IdleStateHandler(60, 0, 0));
                        // 解码器
                        nioSocketChannel.pipeline().addLast(new RPCDecoder());
                        // 心跳消息处理器（rpcHeartbeat）
                        nioSocketChannel.pipeline().addLast(new RpcHeartbeatHandler());
                        // 请求消息处理器（rpcRequest）
                        nioSocketChannel.pipeline().addLast(new RpcRequestHandler(getServiceProvider()));
                        // 编码器
                        nioSocketChannel.pipeline().addLast(new RPCEncoder());
                    }
                })
                .bind(getServiceProvider().getPort());
        logger.info("Netty 服务端等待连接...");
    }

    @Override
    public void stopServer() {

    }
}
