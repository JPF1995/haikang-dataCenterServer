package com.haikang.datacenter.dao;

import com.haikang.datacenter.util.CoherenceTopic;
import com.haikang.datacenter.util.CoherenceUtil;
import com.haikang.datacenter.util.ConvertUtil;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * Filename:    BaseInfoFromCohDao
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-5-24 10:06
 * Description:
 */

@Component
public class BaseInfoFromCohDao {

    /**
     * 获取车辆静态信息
     *
     * @return
     */
    public Map loadVehicle() {
        CoherenceUtil coUtil = CoherenceUtil.getConnection(CoherenceTopic.VEHICLE_INFO);
        Map map = coUtil.getAll();
        return map;
    }

    /**
     * 更新司机识别信息
     *
     * @param vehicleId
     * @param driverId
     */
    public void updateDriverReocgnition(String vehicleId, String driverId) {
        CoherenceUtil coUtil = CoherenceUtil.getConnection(CoherenceTopic.DRIVER_RECOGNITION);
        coUtil.put(vehicleId, driverId);
    }

    /**
     * 获取司机识别信息
     *
     * @param vehicleId
     */
    public String getDriverReocgnition(String vehicleId) {
        CoherenceUtil coUtil = CoherenceUtil.getConnection(CoherenceTopic.DRIVER_RECOGNITION);
        String driverId = ConvertUtil.getStringValue(coUtil.get(vehicleId));
        return driverId;
    }

    public void putCoherence(String topic, Object key, Object value) {
        CoherenceUtil coUtil = CoherenceUtil.getConnection(topic);
        coUtil.put(key, value);
    }

    public static void main(String[] args) {
        CacheFactory.ensureCluster();
        NamedCache cache = CacheFactory.getCache("vms_wy_cahce_driver_recognition");
        Set set = cache.keySet();
        Map map = cache.getAll(set);
        System.out.println(map);
    }
}
