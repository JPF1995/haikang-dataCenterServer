package com.haikang.datacenter.listener;


import com.cnpc.vms.coherence.entity.DCommandInfo;
import com.haikang.common.messagebody.CommandMessage;
import com.haikang.datacenter.bus.BusListener;
import com.haikang.datacenter.bus.Command;
import com.haikang.datacenter.handler.BaseInfoHandler;
import com.haikang.datacenter.handler.HaiKangCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Filename:    SendCommandListener
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-2-28 15:13
 * Description:
 */

@Component("CommandIssuedListener")
public class CommandIssuedListener implements BusListener {

    private static final Logger logger = LoggerFactory.getLogger(CommandIssuedListener.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private HaiKangCommandHandler handler ;


    @Override
    public void commandReceived(Command command, String connectName) {

        String sim = String.valueOf(command.getParam());

        DCommandInfo cmdInfo = (DCommandInfo) command.getResult();
        long seqId = cmdInfo.getSeqID();
        int messageID = BaseInfoHandler.getInstance().getMessageID(seqId);
        Command message = handler.prepareHKCommand(cmdInfo, sim, messageID);
        if (message!=null) {
            BaseInfoHandler.getInstance().putCommand((CommandMessage) message.getResult());
            logger.debug("Push to kafka:topic(" + message.getSource() + "),sim(" + sim + "),value(" + message.toString() + ")");
            kafkaTemplate.send(message.getSource(), sim, message.getResult());
        }
    }

    public KafkaTemplate getKafkaTemplate() {
        return kafkaTemplate;
    }

    public void setKafkaTemplate(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public HaiKangCommandHandler getHandler() {
        return handler;
    }

    public void setHandler(HaiKangCommandHandler handler) {
        this.handler = handler;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init() {

    }
}
