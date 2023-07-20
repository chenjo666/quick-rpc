package com.cj.v6.client;

import com.cj.v6.dto.RPCRequest;
import com.cj.v6.dto.RPCResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DefaultRPCClient extends AbstractRPCClient {
    private static final Logger logger = LoggerFactory.getLogger(DefaultRPCClient.class);

    public DefaultRPCClient(String host, int port) {
        super(host, port);
    }

    @Override
    public RPCResponse sendRPCRequest(RPCRequest request) {
        try {
            // 连接服务器
            Socket socket = new Socket(getHost(), getPort());
            logger.info("客户端已经启动...");
            if (socket.isConnected()) {
                // 得到 IO 流
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                // 传入请求
                objectOutputStream.writeObject(request);
                objectOutputStream.flush();
                // 得到数据
                RPCResponse response = (RPCResponse) objectInputStream.readObject();
                logger.info("客户端收到：{}", response);

                // 关闭 IO 流
                objectInputStream.close();
                objectOutputStream.close();

                // 返回请求
                return response;
            }
            // 关闭连接
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
