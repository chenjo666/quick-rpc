package netty.handler;


import api.Serializer;
import constants.RpcConstant;
import dtos.RpcMessage;
import dtos.RpcPing;
import dtos.RpcPong;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import netty.NettyRpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RpcHeartbeatTrigger extends SimpleChannelInboundHandler<RpcPong> {
    private static final Logger logger = LoggerFactory.getLogger(RpcHeartbeatTrigger.class);
    private final AtomicInteger totalRetry = new AtomicInteger(0);
    private static final int MAX_RETRY = 5;
    private final NettyRpcClient client;

    public RpcHeartbeatTrigger(NettyRpcClient client) {
        this.totalRetry.set(0);
        this.client = client;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcPong rpcPong) throws Exception {
        logger.info("客户端响应 pong: {}", rpcPong);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.WRITER_IDLE) {           // 发送心跳
                handlerPing(ctx.channel());
            } else if (e.state() == IdleState.READER_IDLE) {    // 接收心跳不及时
                handlerRetry(ctx.channel());
            }
        }
    }

    // 断开重连
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        handlerRetry(ctx.channel());
    }

    // 心跳检测
    private void handlerPing(Channel channel) {
        RpcPing ping = new RpcPing();
        // 构造发送数据
        RpcMessage rpcMessage = RpcMessage.builder()
                .magicNum(RpcConstant.MESSAGE_MAGIC_NUM)
                .version(RpcConstant.MESSAGE_VERSION)
                .reserve(RpcConstant.MESSAGE_RESERVE)
                .messageType(RpcConstant.MESSAGE_TYPE_PING)
                .serializeTpe(Serializer.SERIALIZER_KRYO)
                .data(ping)
                .build();

        channel.writeAndFlush(rpcMessage);
        logger.info("客户端发送 ping: [{}]", ping);
    }

    // 处理重试
    private void handlerRetry(Channel channel) {
        logger.info("连接服务端失败，即将进行重试: {}", channel);
        // 1. 删除原先的 channel
        InetSocketAddress inetSocketAddress = client.getChannelsHelper().get(channel);
        client.getChannels().remove(inetSocketAddress);
        client.getChannelsHelper().remove(channel);
        // 2. 关闭原先的 channel
        channel.close();
        // 3. 进行重试
        retryConnect(inetSocketAddress);
    }

    // 断线重试【每 30s 进行一次尝试连接，连接成功则正常连接，连接失败则重新尝试】
    // 1. 得到服务端的 channel, 将其删除并重试
    // 2. 重新尝试连接
    private void retryConnect(InetSocketAddress inetSocketAddress) {
        ChannelFuture channelFuture = client.getBootstrap().connect(inetSocketAddress);
        channelFuture.addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {                  // 5 次连接过程中失败
                // 如果达到最大次数未连接上，那么不再连接
                if (totalRetry.get() == MAX_RETRY) {
                    logger.info("达到最大次数，连接失败!");
                    return;
                }
                // 否则 30s 后交给后台线程重新连接
                future.channel().eventLoop().schedule(() -> {
                    totalRetry.getAndIncrement();
                    logger.info("当前重试次数: {}", totalRetry.get());
                    retryConnect(inetSocketAddress);
                }, 5, TimeUnit.SECONDS);
            } else {                                    // 5 次连接过程中成功
                totalRetry.set(0);
                Channel newChannel = future.channel();
                client.getChannels().put(inetSocketAddress, newChannel);
                client.getChannelsHelper().put(newChannel, inetSocketAddress);
                logger.info("重试成功，连接成功!");
            }
        });
    }
}
