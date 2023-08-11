package netty.handler;

import api.Serializer;
import constants.RpcConstant;
import dtos.RpcMessage;
import dtos.RpcPing;
import dtos.RpcPong;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcHeartbeatHandler extends SimpleChannelInboundHandler<RpcPing> {

    private static final Logger logger = LoggerFactory.getLogger(RpcHeartbeatHandler.class);

    // 心跳消息处理
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcPing rpcPing) throws Exception {
        // 处理心跳消息
        logger.info("服务端收到 ping: [{}]", rpcPing.getMessage());
        // 返回一个 pong
        RpcPong pong = new RpcPong();
        // 构造发送数据
        RpcMessage rpcMessage = RpcMessage.builder()
                .magicNum(RpcConstant.MESSAGE_MAGIC_NUM)
                .version(RpcConstant.MESSAGE_VERSION)
                .messageType(RpcConstant.MESSAGE_TYPE_PONG)
                .serializeTpe(Serializer.SERIALIZER_KRYO)
                .data(pong)
                .build();

        ctx.channel().writeAndFlush(rpcMessage);
        logger.info("服务端响应 pong: [{}]", pong);
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
