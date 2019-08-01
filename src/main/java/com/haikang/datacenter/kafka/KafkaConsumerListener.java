package com.haikang.datacenter.kafka;


import cn.com.ycig.tiisp.common.kafka.SynEntity;
import com.cnpc.vms.coherence.entity.DCommandInfo;
import com.cnpc.vms.coherence.entity.SVehicleInfo;
import com.haikang.datacenter.bus.BusManager;
import com.haikang.datacenter.bus.Command;
import com.haikang.datacenter.handler.BaseInfoHandler;
import com.haikang.datacenter.listener.BusConnectName;
import com.haikang.datacenter.util.ToolUtil;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

/**
 * Filename:    KafkaConsumerListener
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-2-28 14:55
 * Description:
 */
@PropertySource(value = "classpath:/kafka.properties")
@Component
public class KafkaConsumerListener implements MessageListener<String, Object> {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerListener.class);

    /**
     * 监听器自动执行该方法 消费消息 自动提交offset 执行业务代码 （high level api
     * 不提供offset管理，不能指定offset进行消费）
     */
    @Override
    @KafkaListener(topics = "#{'${kafkaTopic}'.split(',')}")
    public void onMessage(ConsumerRecord<String, Object> record) {

        String sim = record.key();
        Object value = record.value();
        String topic = record.topic();
        logger.debug("收到一条指令回馈,消息topic：" + topic + ",sim：" + sim);
        Command command = ToolUtil.packageCommand(sim, value, topic);
        if (KafkaTopic.DEVICE_EVENT_UPLOAD_RECEIVE.equals(topic)) {
            BusManager.sendCommand(BusConnectName.PLATFORMKAFKA, command);
        } else {
            BusManager.sendCommand(BusConnectName.MESSAGEUPLOAD, command);
        }
    }


    /**
     * 处理海康下发指令
     *
     * @param record
     */
    @KafkaListener(topics = {KafkaTopic.COMMAND})
    public void onCommandMessage(ConsumerRecord<String, Object> record) {

        Object value = record.value();
        SynEntity entity = (SynEntity) value;
//        logger.info("收到一条指令下发,消息类型("+entity.getDesc()+"),info("+entity.toString()+").");
        if (entity == null) {
            return;
        }
        DCommandInfo cmdInfo = (DCommandInfo) entity.getNewValue();
        if (cmdInfo != null) {
            String vehicleId = cmdInfo.getVehicleId();
            String sim = BaseInfoHandler.getInstance().getSimByVehicleId(vehicleId);
            if (sim == null) {
                return;
            }
            Command command = ToolUtil.packageCommand(sim, cmdInfo, null);
            BusManager.sendCommand(BusConnectName.COMMANDISSUED, command);
        }
    }

    @KafkaListener(topics = KafkaTopic.VEHICLE_INFO)
    public void onVehicleMessage(ConsumerRecord<String, Object> record) {

        Object data = record.value();
        SynEntity entity = (SynEntity) data;
        logger.debug("收到一条车辆信息修改消息,消息类型(" + entity.getDesc() + "),info(" + entity.toString() + ").");
        if (entity.getType() == SynEntity.DELETE) {
            SVehicleInfo vinfo = (SVehicleInfo) entity.getOldValue();
            BaseInfoHandler.getInstance().removeVehicle(vinfo);
        } else {
            SVehicleInfo vinfo = (SVehicleInfo) entity.getNewValue();
            BaseInfoHandler.getInstance().putVehicleCache(vinfo);
        }
    }

}
