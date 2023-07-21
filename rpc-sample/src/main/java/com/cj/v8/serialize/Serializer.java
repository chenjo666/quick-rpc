package com.cj.v8.serialize;

/**
 * 自定义序列化接口
 */
public interface Serializer {
    // 序列化方法：把对象转成字节数组
    <T> byte[] serialize(T object);
    // 反序列化方法：把字节数组转成对象
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
