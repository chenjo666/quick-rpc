package proxy;


public abstract class AbstractProxyFactory {
    public abstract  <T>T getProxy(Class<T> clazz);
}
