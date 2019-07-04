package com.haikang.datacenter.config;


import com.haikang.datacenter.bus.BusListener;
import com.haikang.datacenter.util.SpringUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Filename:    BusListenerConfig
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-3-1 17:25
 * Description:
 */
@Configuration
@PropertySource(value= "classpath:/busServer.properties")
@ConfigurationProperties(prefix = "data")
public class BusListenerConfig {

    private Map<String, String> busListeners;

    public Map<String, String> getBusListeners() {
        return busListeners;
    }

    public void setBusListeners(Map<String, String> busListeners) {
        this.busListeners = busListeners;
    }

    public Map<String, BusListener> getBusListenerConfig(){
        Map<String, BusListener> listenerMap = new HashMap<String, BusListener>();
        Set<String> keys = busListeners.keySet();
        for (String key : keys) {
            String value = busListeners.get(key);
            listenerMap.put(key,(BusListener)SpringUtil.getBean(value));
        }
        return listenerMap;
    }
}
