package com.cj.v9.codec;

import com.cj.v9.constant.RPCConstant;
import com.cj.v9.serialize.Serializer;
import com.cj.v9.util.MessageUtil;
import com.cj.v9.util.SerializerUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
/**
 *  *  0   1   2   3   4         5         6   7   8   9   10        11    12    13   14   15          16
 *  *  +------------------------------------------------------------------------------------------------+
 *  *  |      魔数      | 版本号  | 序列化算法 |  消息号         | 消息类型  |      消息总长        | 保留字段    |
 *  *  |     4byte     | 1byte  | 1byte    |  4byte        |  1byte   |      4byte         | 1byte      |
 *  *  +------------------------------------------------------------------------------------------------+
 *  *  |                                    数据内容 （长度不定）                                           |
 *  *  |                                                                                                |
 *  *  +------------------------------------------------------------------------------------------------+
 */
public class RPCDecoder  extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(RPCDecoder.class);
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 1. 读取魔数
        int magicNum = byteBuf.readInt();
        if (!checkMagicNum(magicNum)) {
            throw new IllegalArgumentException("协议错误");
        }
        // 2. 读取版本号
        byte version = byteBuf.readByte();
        if (!checkVersion(version)) {
            throw new IllegalArgumentException("版本错误");
        }
        // 3. 读取序列化算法
        byte serializeType = byteBuf.readByte();
        // 4. 读取消息号
        int messageId = byteBuf.readInt();
        // 5. 读取消息类型
        byte messageType = byteBuf.readByte();
        // 6. 读取消息总长
        int messageLength = byteBuf.readInt();
        // 7. 保留
        byte reserve = byteBuf.readByte();
        // 8. 解析对象
        // 8.1 对象长度 = 消息总长 - 消息头部长度
        int objectLength = messageLength - RPCConstant.MESSAGE_HEADER_LENGTH;
        byte[] objectBytes = new byte[objectLength];
        byteBuf.readBytes(objectBytes);
        // 8.2 根据序列化算法得到真正的序列化算法
        Serializer serializer = SerializerUtil.getSerializer(serializeType);
        // 8.3 根据消息类型得到真正的消息类
        Class clazz = MessageUtil.getMessage(messageType);
        // 8.4 解析出消息
        Object obj = serializer.deserialize(clazz, objectBytes);
        // 8.3 写入结果集
        list.add(obj);
    }

    private boolean checkMagicNum(int magicNum) {
        if (magicNum != RPCConstant.MESSAGE_MAGIC_NUM) {
            return false;
        }
        return true;
    }
    private boolean checkVersion(byte version) {
        if (version != RPCConstant.MESSAGE_VERSION) {
            return false;
        }
        return true;
    }
}
