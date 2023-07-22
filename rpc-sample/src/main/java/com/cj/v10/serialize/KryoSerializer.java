package com.cj.v10.serialize;

import com.cj.v10.dto.RPCMessage;
import com.cj.v10.dto.RPCRequest;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class KryoSerializer implements Serializer {
    private static final Kryo kryo = new Kryo();

    static {
        kryo.register(RPCRequest.class);
        kryo.register(RPCMessage.class);
    }
    @Override
    public <T> byte[] serialize(T object) {
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Output output = new Output(byteArrayOutputStream)) {
            kryo.writeObject(output, object);
            return output.toBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            Input input = new Input(byteArrayInputStream)) {
            T t = kryo.readObject(input, clazz);
            return clazz.cast(t);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
