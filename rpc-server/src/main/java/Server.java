
import netty.NettyRpcServer;
import provider.DefaultServiceProvider;
import provider.ServiceProvider;
import server.AbstractRpcServer;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server {
    private static final int port = 8888;
    public static void main(String[] args) throws UnknownHostException {
        // 注册服务
        RpcHelloService service = new RpcHelloServiceImpl();
        // 添加服务
        ServiceProvider serviceProvider = new DefaultServiceProvider(InetAddress.getLocalHost().getHostAddress(), port);
        serviceProvider.addService(service.getClass().getInterfaces()[0].getName(), service);
        // 发布服务
        serviceProvider.pubService(service.getClass().getInterfaces()[0].getName());
        // 测试服务
        AbstractRpcServer abstractRpcServer = new NettyRpcServer(serviceProvider);
        abstractRpcServer.startServer();
    }
}
