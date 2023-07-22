package com.cj.v11.util;

import com.cj.v11.serializer.JavaSerializer;
import com.cj.v11.serializer.JsonSerializer;
import com.cj.v11.serializer.KryoSerializer;
import com.cj.v11.serializer.Serializer;

public class SerializerUtil {
    public static Serializer getSerializer(byte serializeType) {
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
