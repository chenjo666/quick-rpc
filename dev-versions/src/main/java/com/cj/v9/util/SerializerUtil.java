package com.cj.v9.util;

import com.cj.v9.serialize.JdkSerializer;
import com.cj.v9.serialize.JsonSerializer;
import com.cj.v9.serialize.KryoSerializer;
import com.cj.v9.serialize.Serializer;

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
