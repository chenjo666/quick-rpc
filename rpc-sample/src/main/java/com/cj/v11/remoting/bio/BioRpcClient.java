package com.cj.v11.remoting.bio;

import com.cj.v11.dto.RpcRequest;
import com.cj.v11.dto.RpcResponse;
import com.cj.v11.registry.discovery.ServiceDiscovery;
import com.cj.v11.remoting.AbstractRpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;


public class BioRpcClient extends AbstractRpcClient {
    private static final Logger logger = LoggerFactory.getLogger(BioRpcClient.class);

    public BioRpcClient(ServiceDiscovery serviceDiscovery) {
        super(serviceDiscovery);
    }


    @Override
    public RpcResponse sendRPCRequest(RpcRequest request) {
        InetSocketAddress address = getServiceDiscovery().discoverService(request);
        String host = address.getHostName();
        int port = address.getPort();

        try {
            // 连接服务器
            Socket socket = new Socket(host, port);
            logger.info("默认客户端连接服务: {}:{}", host, port);

            if (socket.isConnected()) {
                // 得到 IO 流
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                // 传入请求
                objectOutputStream.writeObject(request);
                objectOutputStream.flush();
                // 得到数据
                RpcResponse response = (RpcResponse) objectInputStream.readObject();
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
