package com.haikang.datacenter.command.process;

import com.haikang.common.messagebody.CommandMessage;
import com.haikang.common.messagebody.Message0x8004;
import com.haikang.util.BytesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Filename:    Process2002
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-5-21 19:27
 * Description:
 */

public class Process2004 extends CommandProcess {

    @Override
    public CommandMessage transformData(String sim, int messageID, String[] param) {
        CommandMessage message = new Message0x8004();
        String driverIDParam = param[0];
        List<String> driverIDList = new ArrayList<>(100);
        int bodyLength;
        if ("".equals(driverIDParam)) {
            driverIDList = null;
            bodyLength = 21;
        } else {
            String[] driverIDStr = driverIDParam.split(",");
            for (String driverID : driverIDStr) {
                driverIDList.add(driverID);
            }
            bodyLength = 20 * driverIDList.size() + 20;
        }
        message.setCmd(0x8004);
        message.setSim(sim);
        message.setMessageID(messageID);
        ((Message0x8004) message).setDriverIdList(driverIDList);
        message.setBodyLength(bodyLength);
        return message;
    }
}
