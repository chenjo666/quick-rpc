package api;

/**
 * 自定义序列化接口
 */
public interface Serializer {

    byte SERIALIZER_JAVA = 0;
    byte SERIALIZER_JSON = 1;
    byte SERIALIZER_KRYO = 2;

    // 序列化方法：把对象转成字节数组
    <T> byte[] serialize(T object);
    // 反序列化方法：把字节数组转成对象
    <T> T deserialize(Class<T> clazz, byte[] bytes);

    static Serializer getSerializer(byte serializeType) {
        switch (serializeType) {
            case 0:
                return new JavaSerializer();
            case 1:
                return new JsonSerializer();
            case 2:
                return new KryoSerializer();
            default:
                return null;
        }
    }
}
