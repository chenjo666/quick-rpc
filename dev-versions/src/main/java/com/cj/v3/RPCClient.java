package com.cj.v3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RPCClient {
    private static final Logger logger = LoggerFactory.getLogger(RPCClient.class);
    public static void main(String[] args) {
        // 生成客户端代理类
        RPCClientProxy rpcClientProxy = new RPCClientProxy("127.0.0.1",8888);
        // 代理具体服务
        UserService userService = rpcClientProxy.getProxyService(UserService.class);

        // 执行服务方法
        User user1 = userService.getUserById("1");
        logger.info("user1: {}", user1);

        User user2 = userService.updateUserName("1", "v3 rpc");
        logger.info("user2: {}", user2);
    }
}
