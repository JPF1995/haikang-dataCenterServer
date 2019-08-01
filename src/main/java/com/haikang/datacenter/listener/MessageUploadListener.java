package com.haikang.datacenter.listener;

import com.cnpc.vms.coherence.entity.DTerminalCommandResponse;
import com.haikang.datacenter.bus.BusListener;
import com.haikang.datacenter.bus.Command;
import com.haikang.datacenter.dao.BaseInfoFromCohDao;
import com.haikang.datacenter.handler.HaiKangCommandHandler;
import com.haikang.datacenter.util.CoherenceTopic;
import com.haikang.datacenter.util.CoherenceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Filename:    ReceiveCommandListener
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-2-28 15:13
 * Description:
 */

@Component("MessageUploadListener")
public class MessageUploadListener implements BusListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageUploadListener.class);

    @Autowired
    private HaiKangCommandHandler handler;

    private static BaseInfoFromCohDao cohDao = new BaseInfoFromCohDao();

    @Override
    public void commandReceived(Command command, String connectName) {

        DTerminalCommandResponse response = handler.getHKCommandResponse(command);
        if (response != null) {
            logger.info("收到指令回馈，cmd:" + response.getCmdID() + ",seq" + response.getSeqID());
            cohDao.putCoherence(CoherenceTopic.COMMAND_RESPONSE, String.valueOf(response.getSeqID()), response);
        } else {
            logger.warn("收到未知消息command:" + command.toString());
        }
    }

    public HaiKangCommandHandler getHandler() {
        return handler;
    }

    public void setHandler(HaiKangCommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public void init() {

    }
}
