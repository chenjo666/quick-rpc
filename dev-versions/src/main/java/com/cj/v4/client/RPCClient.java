package com.cj.v4.client;

import com.cj.v4.pojo.Book;
import com.cj.v4.pojo.User;
import com.cj.v4.service.BookService;
import com.cj.v4.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RPCClient {
    private static final Logger logger = LoggerFactory.getLogger(RPCClient.class);
    public static void main(String[] args) {
        // 生成客户端代理类
        RPCClientProxy rpcClientProxy = new RPCClientProxy("127.0.0.1",8888);

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
