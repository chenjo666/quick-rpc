package com.cj.v12.remoting.netty.handler;

import com.cj.v12.constant.RpcConstant;
import com.cj.v12.remoting.dto.RpcHeartbeat;
import com.cj.v12.remoting.dto.RpcMessage;
import com.cj.v12.serializer.Serializer;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcHeartbeatTrigger extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(RpcHeartbeatTrigger.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            // 触发写空闲事件，发送一个心跳包表示自己还存活
            if (e.state() == IdleState.WRITER_IDLE) {
                RpcHeartbeat rpcHeartbeat = new RpcHeartbeat();
                // 构造发送数据
                RpcMessage rpcMessage = RpcMessage.builder()
                        .magicNum(RpcConstant.MESSAGE_MAGIC_NUM)
                        .version(RpcConstant.MESSAGE_VERSION)
                        .messageType(RpcConstant.MESSAGE_TYPE_HEARTBEAT)
                        .serializeTpe(Serializer.SERIALIZER_KRYO)
                        .data(rpcHeartbeat)
                        .build();

                ctx.channel().writeAndFlush(rpcMessage);
                logger.info("发送心跳: [{}]", rpcHeartbeat);
            }
        }
    }
}
