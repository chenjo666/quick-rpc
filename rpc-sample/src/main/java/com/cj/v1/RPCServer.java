package com.cj.v1;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RPCServer {
    private static final Logger logger = LoggerFactory.getLogger(RPCServer.class);
    public static void main(String[] args) throws IOException {
        // 注册服务
        UserService userService = new UserServiceImpl();
        ServerSocket serverSocket = new ServerSocket(8888);
        try {
            logger.info("服务器启动成功...");
            // BIO 方式等待客户端连接
            while (true) {
                // 进入阻塞，等待连接
                Socket socket = serverSocket.accept();
                logger.info("服务器连接成功：{}", socket);
                // 开启线程去处理
                new Thread(() -> {
                    try {
                        // 得到 IO 流
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        // 读取数据
                        String id = String.valueOf(ois.readInt());
                        // 调用服务
                        User user = userService.getUserById(id);
                        // 返回结果
                        oos.writeObject(user);
                        oos.flush();
                        logger.info("服务器发送：{}", user);
                        // 关闭通道
                        oos.close();
                        ois.close();
                    } catch (IOException e) {
                        logger.info("客户端连接错误...");
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } catch (IOException e) {
            logger.info("服务器启动失败...");
            throw new RuntimeException(e);
        }
    }
}
