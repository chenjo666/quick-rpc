public class RpcHelloServiceImpl implements RpcHelloService {

    @Override
    public RpcObject hello(String message) {

        return new RpcObject(message);
    }
}
