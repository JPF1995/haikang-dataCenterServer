package com.haikang.datacenter.kafka;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Serializer;

/**
 * Filename:    RawMessageEncoder
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-5-28 14:33
 * Description:
 */

public class RawMessageEncoder implements Serializer<Object> {


    public static byte[] ObjectToByte(java.lang.Object obj) {
        return SerializationUtils.serialize((Serializable) obj);
    }

    /** (非 Javadoc)
     * <p>Title: configure</p>
     * <p>Description: </p>
     * @param configs
     * @param isKey
     * @see org.apache.kafka.common.serialization.Serializer#configure(java.util.Map, boolean)
     */
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // TODO Auto-generated method stub

    }

    /** (非 Javadoc)
     * <p>Title: serialize</p>
     * <p>Description: </p>
     * @param topic
     * @param data
     * @return
     * @see org.apache.kafka.common.serialization.Serializer#serialize(java.lang.String, java.lang.Object)
     */
    @Override
    public byte[] serialize(String topic, Object data) {
        // TODO Auto-generated method stub
        return ObjectToByte(data);
    }

    /** (非 Javadoc)
     * <p>Title: close</p>
     * <p>Description: </p>
     * @see org.apache.kafka.common.serialization.Serializer#close()
     */
    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

}
