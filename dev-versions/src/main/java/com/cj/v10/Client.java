package com.cj.v10;

import com.cj.v10.client.AbstractRPCClient;
import com.cj.v10.client.NettyRPCClient;
import com.cj.v10.client.RPCClientProxy;
import com.cj.v10.loadbalance.IpHashLoadBalance;
import com.cj.v10.loadbalance.RoundLoadBalance;
import com.cj.v10.pojo.Book;
import com.cj.v10.pojo.User;
import com.cj.v10.registry.ZkServiceRegistry;
import com.cj.v10.service.BookService;
import com.cj.v10.service.UserService;

public class Client {
    public static void main(String[] args) {
        AbstractRPCClient abstractRpcClient = new NettyRPCClient(new ZkServiceRegistry(new RoundLoadBalance()));
        // 生成客户端代理类
        RPCClientProxy rpcClientProxy = new RPCClientProxy(abstractRpcClient);

        // 代理 user 服务
        UserService userService = rpcClientProxy.getProxyService(UserService.class);
        User user = userService.getUser("1");

        // 代理 book 服务
        BookService bookService = rpcClientProxy.getProxyService(BookService.class);
        Book book = bookService.getBook("1");
    }
}
