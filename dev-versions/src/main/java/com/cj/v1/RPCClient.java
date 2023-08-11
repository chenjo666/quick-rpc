package com.cj.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
public class RPCClient {
    private static final Logger logger = LoggerFactory.getLogger(RPCClient.class);
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // 连接服务器
        Socket socket = new Socket(InetAddress.getLocalHost(), 8888);
        logger.info("客户端已经启动...");
        if (socket.isConnected()) {
            // 得到 IO 流
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // 传入数据
            objectOutputStream.writeInt(1);
            objectOutputStream.flush();
            // 得到数据
            User user = (User) objectInputStream.readObject();
            logger.info("客户端收到：{}", user);
            // 关闭 IO 流
            objectInputStream.close();
            objectOutputStream.close();
        }
        // 关闭连接
        socket.close();
    }
}
