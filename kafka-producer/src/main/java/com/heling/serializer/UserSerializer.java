package com.heling.serializer;

import com.heling.domain.User;
import org.apache.kafka.common.serialization.Serializer;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * @Desc：User类序列化
 * @Author: heling
 * @Date: 2020/9/30 13:58
 */
public class UserSerializer implements Serializer<User> {

    private static final String CHAR_SET = "UTF-8";

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, User data) {
        if (null == data) {
            return null;
        }
        byte[] name, age;
        try {
            if (data.getName() != null) {
                name = data.getName().getBytes(CHAR_SET);
            } else {
                name = new byte[0];
            }
            if (data.getAge() != null) {
                age = new byte[]{(byte) (data.getAge() >>> 24),
                        (byte) (data.getAge() >>> 16),
                        (byte) (data.getAge() >>> 8),
                        data.getAge().byteValue()};
            } else {
                age = new byte[0];
            }
            ByteBuffer buf = ByteBuffer.allocate(8 + name.length + age.length);
            buf.putInt(name.length);
            buf.put(name);
            buf.putInt(age.length);
            buf.put(age);
            return buf.array();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public void close() {

    }
}
