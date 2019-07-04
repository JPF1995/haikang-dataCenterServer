package com.haikang.datacenter.listener;

import com.haikang.datacenter.bus.BusListener;
import com.haikang.datacenter.bus.Command;
import com.haikang.datacenter.handler.PlatformMessageHandler;
import com.haikang.datacenter.util.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Filename:    PlatformKafkaListener
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-5-30 10:38
 * Description:
 */
@Component("PlatformKafkaListener")
public class PlatformKafkaListener implements BusListener {

    private static final Logger logger = LoggerFactory.getLogger(PlatformKafkaListener.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private PlatformMessageHandler handler;


    @Override
    public void commandReceived(Command command, String connectName) {


       Command message = handler.getKafkaMessage(command);
        if (message!=null) {
            String topic = message.getSource();
            String vehicleId = ConvertUtil.getStringValue(message.getParam());
            logger.debug("Push to kafka:topic(" + topic + "),vehicleId(" + vehicleId + "),value(" + message.toString() + ")");
            kafkaTemplate.send(topic, vehicleId, message.getResult());
        }
    }

    public KafkaTemplate getKafkaTemplate() {
        return kafkaTemplate;
    }

    public void setKafkaTemplate(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public PlatformMessageHandler getHandler() {
        return handler;
    }

    public void setHandler(PlatformMessageHandler handler) {
        this.handler = handler;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init() {
    }

}
