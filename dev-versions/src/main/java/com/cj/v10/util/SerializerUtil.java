package com.cj.v10.util;

import com.cj.v10.serialize.JdkSerializer;
import com.cj.v10.serialize.JsonSerializer;
import com.cj.v10.serialize.KryoSerializer;
import com.cj.v10.serialize.Serializer;

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
