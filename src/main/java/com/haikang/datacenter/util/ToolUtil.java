package com.haikang.datacenter.util;

import com.haikang.datacenter.bus.Command;

/**
 * Filename:    ToolUtil
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-5-30 11:20
 * Description:
 */

public class ToolUtil {

    public static Command packageCommand(String sim, Object msg, String topic) {
        Command command = new Command();
        command.setSource(topic);
        command.setParam(sim);
        command.setResult(msg);
        return command;
    }
}
