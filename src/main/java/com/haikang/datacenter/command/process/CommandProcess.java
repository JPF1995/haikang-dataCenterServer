package com.haikang.datacenter.command.process;

import com.haikang.common.messagebody.CommandMessage;

/**
 * Filename:    CommandProcess
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-5-21 19:29
 * Description:
 */

public abstract class CommandProcess {

    /**
     * 将指令转换成协议消息体
     * @param sim
     * @param messageID
     * @param param
     * @return
     */
    public abstract CommandMessage transformData(String sim, int messageID, String[] param);
}
