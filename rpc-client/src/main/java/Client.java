import proxy.AbstractProxyFactory;
import proxy.jdk.JdkProxyFactory;

public class Client {
    public static void main(String[] args) {
        AbstractProxyFactory proxyFactory = new JdkProxyFactory();
        // 代理服务
        RpcHelloService service = proxyFactory.getProxy(RpcHelloService.class);
        RpcObject rpcObject = service.hello("ni hao, rpc!!!");
    }
}
