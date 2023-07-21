package com.cj.v8.util;

import com.cj.v8.serialize.JdkSerializer;
import com.cj.v8.serialize.JsonSerializer;
import com.cj.v8.serialize.KryoSerializer;
import com.cj.v8.serialize.Serializer;
public class SerializerUtil {
    public static Serializer getSerializer(byte serializeType) {
        switch (serializeType) {
            case 0:
                return new JdkSerializer();
            case 1:
                return new JsonSerializer();
            case 2:
                return new KryoSerializer();
            default:
                return null;
        }
    }
}
