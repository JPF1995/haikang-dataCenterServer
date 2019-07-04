package com.haikang.datacenter.command;

import com.haikang.datacenter.command.process.*;
import com.haikang.util.ConvertUtil;

/**
 * Filename:    CommandProcessManager
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-5-21 19:27
 * Description:
 */

public enum CommandProcessManager {

    //司机证件照下发设备上传
    P2002(new Process2002()),
    //司机库删除下发设备上传
    P2003(new Process2003()),
    //司机库查询下发设备上传
    P2004(new Process2004()),
    //司机证件照上传设备上传
    P2005(new Process2005()),
    //主动识别设备上传
    P2007(new Process2007());

    /**
     * 消息处理
     */
    private CommandProcess process;

    /**
     * @Description:构造方法
     * @param :args
     * @return
     * @throws Exception
     */
     CommandProcessManager(
            CommandProcess process) {
        this.process = process;
    }

    /**
     * @Description:获取解析类
     * @param :args
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static CommandProcess getProcess(
            int cmd) {
        try {
            String cmdStr = ConvertUtil.getStringValue(cmd);
            String key = "P"+cmdStr;
            return CommandProcessManager.valueOf(key).process;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
