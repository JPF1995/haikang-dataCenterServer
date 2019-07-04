package com.haikang.datacenter.command.process;


import com.haikang.common.messagebody.CommandMessage;
import com.haikang.common.messagebody.Message0x8008;

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

public class Process2007 extends CommandProcess {

    @Override
    public CommandMessage transformData(String sim, int messageID, String[] param) {
        CommandMessage message = new Message0x8008();
        message.setCmd(0x8008);
        message.setSim(sim);
        message.setMessageID(messageID);
        message.setBodyLength(20);
        return message;
    }
}
