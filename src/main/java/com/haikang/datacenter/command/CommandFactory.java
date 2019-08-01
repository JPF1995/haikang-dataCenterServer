package com.haikang.datacenter.command;

import com.haikang.common.messagebody.CommandMessage;
import com.haikang.datacenter.command.process.CommandProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filename:    CommandFactory
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-5-21 19:50
 * Description:
 */

public class CommandFactory {

    private static final Logger logger = LoggerFactory.getLogger(CommandFactory.class);

    /**
     * 将指令转换成协议消息体
     *
     * @param cmd
     * @param sim
     * @param messageID
     * @param param
     * @return
     */
    public CommandMessage transformData(int cmd, String sim, int messageID, String[] param) {
        CommandProcess process = CommandProcessManager.getProcess(cmd);
        CommandMessage message = process.transformData(sim, messageID, param);
        logger.debug("解析消息：" + message.getCmd() + message.toString());
        return message;
    }
}
