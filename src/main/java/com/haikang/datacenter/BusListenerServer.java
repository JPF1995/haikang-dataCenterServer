package com.haikang.datacenter;



import com.haikang.datacenter.bus.BusListener;
import com.haikang.datacenter.bus.BusManager;
import com.haikang.datacenter.config.BusListenerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * Filename:    BusListenerServer
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-2-28 10:47
 * Description:
 */

@Component
public class BusListenerServer {

    private static final Logger logger = LoggerFactory.getLogger(BusListenerServer.class);

    // 通道监听表 <通道名,通道监听类>
    private Map<String, BusListener> busListeners;

    @Autowired
    private BusListenerConfig busListenerConfig;

    public boolean init(){
        logger.info("DataAccessServer begin to init.");
        busListeners = busListenerConfig.getBusListenerConfig();
        // 初始化内部组件监听
        Set<String> keys = busListeners.keySet();
        for (String key : keys) {
            BusManager.createConnect(key);
            BusManager.registerListener(key, busListeners.get(key), 1);
        }
        return true;
    }

    public boolean start() {
        // 启动内部组件监听
        Set<String> keys = busListeners.keySet();
        for (String key : keys) {
            busListeners.get(key).init();
            BusManager.startConnect(key);
        }

        logger.debug("server start ok");
        return true;
    }

    public Map<String, BusListener> getBusListeners() {
        return busListeners;
    }

    public void setBusListeners(Map<String, BusListener> busListeners) {
        this.busListeners = busListeners;
    }
}
