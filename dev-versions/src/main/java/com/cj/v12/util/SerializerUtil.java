package com.cj.v12.util;

import com.cj.v12.serializer.JavaSerializer;
import com.cj.v12.serializer.JsonSerializer;
import com.cj.v12.serializer.KryoSerializer;
import com.cj.v12.serializer.Serializer;

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
