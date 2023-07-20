package com.cj.v7;

import com.cj.v7.client.AbstractRPCClient;
import com.cj.v7.client.DefaultRPCClient;
import com.cj.v7.client.NettyRPCClient;
import com.cj.v7.client.RPCClientProxy;
import com.cj.v7.pojo.Book;
import com.cj.v7.pojo.User;
import com.cj.v7.service.BookService;
import com.cj.v7.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    public static void main(String[] args) {
        AbstractRPCClient abstractRpcClient = new NettyRPCClient("127.0.0.1",8888);
        // 生成客户端代理类
        RPCClientProxy rpcClientProxy = new RPCClientProxy(abstractRpcClient);

        // 代理 user 服务
        UserService userService = rpcClientProxy.getProxyService(UserService.class);
        User user = userService.getUser("1");
        logger.info("user: {}", user);

        // 代理 book 服务
        BookService bookService = rpcClientProxy.getProxyService(BookService.class);
        Book book = bookService.getBook("1");
        logger.info("book: {}", book);
    }
}
