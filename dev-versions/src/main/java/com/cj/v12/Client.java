package com.cj.v12;

import com.cj.v12.api.pojo.Book;
import com.cj.v12.api.pojo.User;
import com.cj.v12.api.service.BookService;
import com.cj.v12.api.service.UserService;
import com.cj.v12.remoting.AbstractRpcClient;
import com.cj.v12.remoting.netty.NettyRpcClient;
import com.cj.v12.remoting.proxy.RpcClientProxy;

public class Client {
    public static void main(String[] args) {
        AbstractRpcClient abstractRpcClient = new NettyRpcClient();
        // 生成客户端代理类
        RpcClientProxy rpcClientProxy = new RpcClientProxy(abstractRpcClient);

        // 代理 user 服务
        UserService userService = rpcClientProxy.getProxyService(UserService.class);
        User user = userService.getUser("1");

        // 代理 book 服务
        BookService bookService = rpcClientProxy.getProxyService(BookService.class);
        Book book = bookService.getBook("1");

    }
}
