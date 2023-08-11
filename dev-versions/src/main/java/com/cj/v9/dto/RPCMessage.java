package com.cj.v9.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 通信消息
 *  0   1   2   3   4         5         6   7   8   9   10        11    12    13   14   15          16
 *  +------------------------------------------------------------------------------------------------+
 *  |      魔数      | 版本号  | 序列化算法 |  消息号         | 消息类型  |      消息总长        | 保留字段    |
 *  |     4byte     | 1byte  | 1byte    |  4byte        |  1byte   |      4byte         | 1byte      |
 *  +------------------------------------------------------------------------------------------------+
 *  |                                    数据内容 （长度不定）                                           |
 *  |                                                                                                |
 *  +------------------------------------------------------------------------------------------------+
 */
@Data
@Builder
public class RPCMessage {

    private int magicNum;       // 魔数
    private byte version;       // 版本号
    private byte serializeTpe;  // 序列化算法
    private int messageId;      // 消息号
    private byte messageType;    // 消息类型
    private int messageLength;  // 消息总长
    private byte reserve;       // 保留字段
    private Object data;        // 消息数据
}
