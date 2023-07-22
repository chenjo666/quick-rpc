package com.cj.v11.remoting.netty.codec;

import com.cj.v11.constant.RpcConstant;
import com.cj.v11.serializer.Serializer;
import com.cj.v11.dto.RpcMessage;
import com.cj.v11.util.SerializerUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通信协议格式
 *  *  0   1   2   3   4         5         6   7   8   9   10        11    12    13   14   15          16
 *  *  +------------------------------------------------------------------------------------------------+
 *  *  |      魔数      | 版本号  | 序列化算法 |  消息号         | 消息类型  |      消息总长        | 保留字段    |
 *  *  |     4byte     | 1byte  | 1byte    |  4byte        |  1byte   |      4byte         | 1byte      |
 *  *  +------------------------------------------------------------------------------------------------+
 *  *  |                                    数据内容 （长度不定）                                           |
 *  *  |                                                                                                |
 *  *  +------------------------------------------------------------------------------------------------+
 */
public class RPCEncoder extends MessageToByteEncoder<RpcMessage> {
    private static final Logger logger = LoggerFactory.getLogger(RPCEncoder.class);
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage, ByteBuf byteBuf) throws Exception {
        // 1. 魔数
        byteBuf.writeInt(rpcMessage.getMagicNum());
        // 2. 版本号
        byteBuf.writeByte(rpcMessage.getVersion());
        // 3. 序列化类型
        byteBuf.writeByte(rpcMessage.getSerializeTpe());
        // 4. 消息号
        byteBuf.writeInt(rpcMessage.getMessageId());
        // 5. 消息类型
        byteBuf.writeByte(rpcMessage.getMessageType());
        // 6. 消息总长: 数据长度 + 头部长度
        Serializer serializer = SerializerUtil.getSerializer(rpcMessage.getSerializeTpe());
        byte[] bytes = serializer.serialize(rpcMessage.getData());
        byteBuf.writeInt(bytes.length + RpcConstant.MESSAGE_HEADER_LENGTH);
        // 7. 保留字段
        byteBuf.writeByte(rpcMessage.getReserve());
        // 8. 消息
        byteBuf.writeBytes(bytes);
    }
}
