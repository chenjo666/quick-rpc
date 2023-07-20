package com.cj.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
public class RPCClient {
    private static final Logger logger = LoggerFactory.getLogger(RPCClient.class);
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException {
        // 连接服务器
        Socket socket = new Socket(InetAddress.getLocalHost(), 8888);
        logger.info("客户端已经启动...");
        if (socket.isConnected()) {
            // 得到 IO 流
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            // 构造请求
            RPCRequest request = RPCRequest.builder()
                    .interfaceName(UserService.class.getName())
                    .methodName(UserService.class.getDeclaredMethod("updateUserName", String.class, String.class).getName())
                    .args(new Object[]{"1", "v2 rpc"})
                    .argsTypes(new Class[]{String.class, String.class})
                    .build();
            // 传入请求
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
            // 得到数据
            RPCResponse response = (RPCResponse) objectInputStream.readObject();
            logger.info("客户端收到：{}", response);

            // 关闭 IO 流
            objectInputStream.close();
            objectOutputStream.close();
        }
        // 关闭连接
        socket.close();
    }
}
