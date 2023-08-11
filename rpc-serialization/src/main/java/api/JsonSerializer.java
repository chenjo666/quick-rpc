package api;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonSerializer implements Serializer {
    private static final Logger logger = LoggerFactory.getLogger(JsonSerializer.class);

    @Override
    public <T> byte[] serialize(T object) {
        return JSON.toJSONString(object, JSONWriter.Feature.WriteClassName).getBytes();
    }
    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        // 1. JSONReader.Feature.SupportClassForName
        // 错误原因：not support ClassForName : java.lang.String, you can config 'JSONReader.Feature.SupportClassForName'
        // 解决 Class<?> 参数类型时出现的问题
        // 2. JSONReader.Feature.SupportAutoType + JSONWriter.Feature.WriteClassName
        // 错误原因：class com.alibaba.fastjson2.JSONObject cannot be cast to class com.cj.v8.pojo.User
        // 解决子属性中是未知 Object 类型的问题
        return JSON.parseObject(bytes, clazz, JSONReader.Feature.SupportClassForName, JSONReader.Feature.SupportAutoType);
    }
}
