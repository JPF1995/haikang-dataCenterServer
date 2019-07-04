package com.haikang.datacenter.kafka;

import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.util.SerializationUtils;

import java.util.Map;

/**
 * Filename:    RawMessageDecoder
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-2-28 11:40
 * Description:
 */

public class RawMessageDecoder implements Deserializer<Object>
{
    public static Object ByteToObject(byte[] bytes)
    {
        try
        {
            return SerializationUtils.deserialize(bytes);
        }
        catch (Exception e) {
            System.out.println("收到欧错误");
            return null;
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public Object deserialize(String topic, byte[] data)
    {
        return ByteToObject(data);
    }

    @Override
    public void close() {}
}
