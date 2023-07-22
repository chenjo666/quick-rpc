package com.cj.v11;

import com.cj.v11.remoting.AbstractRpcClient;
import com.cj.v11.remoting.proxy.RpcClientProxy;
import com.cj.v11.api.pojo.Book;
import com.cj.v11.api.pojo.User;
import com.cj.v11.api.service.UserService;
import com.cj.v11.remoting.netty.NettyRpcClient;
import com.cj.v11.api.service.BookService;

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
