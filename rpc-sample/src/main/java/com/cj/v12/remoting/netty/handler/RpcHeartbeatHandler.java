package com.cj.v12.remoting.netty.handler;

import com.cj.v12.remoting.dto.RpcHeartbeat;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcHeartbeatHandler extends SimpleChannelInboundHandler<RpcHeartbeat> {

    private static final Logger logger = LoggerFactory.getLogger(RpcHeartbeatHandler.class);

    // 心跳消息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcHeartbeat rpcHeartbeat) throws Exception {
        // 处理心跳消息
        logger.info("收到心跳消息: [{}]", rpcHeartbeat.getMessage());
    }

    // 心跳事件
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            // 触发读空闲事件，说明对方出现问题，关闭通道
            if (e.state() == IdleState.READER_IDLE) {
                logger.info("关闭通道: {}", ctx);
                ctx.close();
            }
        }
    }
}
