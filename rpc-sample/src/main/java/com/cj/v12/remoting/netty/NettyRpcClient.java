package com.cj.v12.remoting.netty;

import com.cj.v12.constant.RpcConstant;
import com.cj.v12.remoting.dto.RpcMessage;
import com.cj.v12.remoting.dto.RpcRequest;
import com.cj.v12.remoting.dto.RpcResponse;
import com.cj.v12.remoting.AbstractRpcClient;
import com.cj.v12.remoting.netty.codec.RPCDecoder;
import com.cj.v12.remoting.netty.codec.RPCEncoder;
import com.cj.v12.remoting.netty.handler.RpcHeartbeatTrigger;
import com.cj.v12.remoting.netty.handler.RpcResponseHandler;
import com.cj.v12.serializer.Serializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Data
public class NettyRpcClient extends AbstractRpcClient {
    private static final Logger logger = LoggerFactory.getLogger(NettyRpcClient.class);
    private final Bootstrap bootstrap;
    private final Map<InetSocketAddress, Channel> channels;
    private final Map<Channel, InetSocketAddress> channelsHelper;
    private final RpcResponseHandler rpcResponseHandler;
    public NettyRpcClient() {
        super();
        this.rpcResponseHandler = new RpcResponseHandler();
        this.channels = new ConcurrentHashMap<>();
        this.channelsHelper = new ConcurrentHashMap<>();
        this.bootstrap = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) {
                        // 【心跳检测 + 断线重连】5s 达到写空闲，发送心跳 ping，60s 达到读空闲，断线重连
                        nioSocketChannel.pipeline().addLast(new IdleStateHandler(60, 5, 0));
                        // 解码器
                        nioSocketChannel.pipeline().addLast(new RPCDecoder());
                        // 心跳触发器
                        nioSocketChannel.pipeline().addLast(new RpcHeartbeatTrigger(NettyRpcClient.this));
                        // 入站处理器
                        nioSocketChannel.pipeline().addLast(rpcResponseHandler);
                        // 编码器
                        nioSocketChannel.pipeline().addLast(new RPCEncoder());
                    }
                });
    }

    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        // 如果对应服务的 channel 不存在，则连接获取 channel
        if (!channels.containsKey(inetSocketAddress)) {
            try {
                Channel channel = bootstrap
                        .connect(inetSocketAddress)
                        .sync()
                        .channel();
                channels.put(inetSocketAddress, channel);
                channelsHelper.put(channel, inetSocketAddress);
                logger.info("Netty 客户端连接服务: {}", inetSocketAddress);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return channels.get(inetSocketAddress);
    }

    @Override
    public RpcResponse sendRPCRequest(RpcRequest request) {
        try {
            // 1. 异步结果
            CompletableFuture<RpcResponse> completableFuture = new CompletableFuture<>();
            rpcResponseHandler.setCompletableFuture(completableFuture);
            // 2. 寻找服务所对应的 channel
            InetSocketAddress address = getServiceDiscovery().discoverService(request);
            Channel channel = getChannel(address);
            // 3. 构造发送数据
            RpcMessage rpcMessage = RpcMessage.builder()
                    .magicNum(RpcConstant.MESSAGE_MAGIC_NUM)
                    .version(RpcConstant.MESSAGE_VERSION)
                    .messageType(RpcConstant.MESSAGE_TYPE_REQUEST)
                    .serializeTpe(Serializer.SERIALIZER_KRYO)
                    .data(request)
                    .build();
            channel.writeAndFlush(rpcMessage);
            logger.info("Netty 客户端发送：[{}]", rpcMessage);
            // 4. 接收异步调用结果
            return completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
