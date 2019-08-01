package com.haikang.datacenter.handler;

import com.cnpc.vms.coherence.entity.DTerminalCommandResponse;
import com.cnpc.vms.coherence.entity.SVehicleInfo;
import com.google.common.cache.*;
import com.haikang.common.messagebody.CommandMessage;
import com.haikang.datacenter.dao.BaseInfoFromCohDao;
import com.haikang.datacenter.util.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Filename:    BaseInfoHandler
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-5-23 17:29
 * Description:
 */

public class BaseInfoHandler {

    private static final Logger logger = LoggerFactory.getLogger(BaseInfoHandler.class);

    private static volatile BaseInfoHandler instance = null;

    private static int messageID = 1;

    private LoadingCache<Integer, Long> idCache;

    private LoadingCache<Integer, CommandMessage> commandCache;

    private ConcurrentHashMap<Integer, SVehicleInfo> vehicleCache = new ConcurrentHashMap();

    private ConcurrentHashMap<String, SVehicleInfo> simCache = new ConcurrentHashMap();

    private BaseInfoFromCohDao cohDao = new BaseInfoFromCohDao();

    private BaseInfoHandler() {
        init();
    }

    public static ReentrantLock lock = new ReentrantLock();

    public static BaseInfoHandler getInstance() {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (instance == null) {
            // 同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (BaseInfoHandler.class) {
                // 未初始化，则初始instance变量
                if (instance == null) {
                    instance = new BaseInfoHandler();
                }
            }
        }
        return instance;
    }

    /**
     * 写入车辆缓存信息
     *
     * @param vehicle
     */
    public void putVehicleCache(SVehicleInfo vehicle) {
        if (vehicle == null) {
            logger.warn("车辆信息为空，无法添加到本地缓存(VehicleCache).");
            return;
        }

        if (vehicle.getVehicleId() == null || vehicle.getVehicleId() == 0) {
            logger.warn("车辆id为空，无法添加到本地缓存(VehicleCache).");
            return;
        }

        if (vehicle.getSimNum() == null || vehicle.getSimNum().isEmpty()) {
            logger.warn("sim卡号为空，无法添加到本地缓存(VehicleCache),vid(" + vehicle.getVehicleId() + ").");
            return;
        }

        vehicleCache.put(vehicle.getVehicleId(), vehicle);
        simCache.put(vehicle.getSimNum(), vehicle);

    }

    /**
     * 删除车辆缓存信息
     *
     * @param vehicle
     * @return
     */
    public SVehicleInfo removeVehicle(SVehicleInfo vehicle) {
        if (vehicle == null) {
            logger.warn("车辆信息为空，无法删除本地缓存。VehicleCache");
            return null;
        }

        if (vehicle.getVehicleId() == null || vehicle.getVehicleId() == 0) {
            logger.warn("车辆id为空，无法删除本地缓存。VehicleCache");
            return null;
        }

        if (vehicle.getSimNum() == null || vehicle.getSimNum().isEmpty()) {
            logger.warn("sim卡号为空，无法删除本地缓存。VehicleCache");
            return null;
        }

        vehicleCache.remove(vehicle.getVehicleId());
        simCache.remove(vehicle.getSimNum());
        return vehicle;
    }

    /**
     * 获取车辆sim
     *
     * @param vehicleIdStr
     * @return
     */
    public String getSimByVehicleId(String vehicleIdStr) {
        int vehicleId = ConvertUtil.getIntValue(vehicleIdStr);
        if (vehicleId == 0) {
            return null;
        }
        SVehicleInfo vehicleInfo = vehicleCache.get(vehicleId);
        if (vehicleInfo == null) {
            return null;
        }
        String sim = vehicleInfo.getSimNum();
        return sim;
    }

    /**
     * 获取车辆sim
     *
     * @param sim
     * @return
     */
    public String getVehicleIdBySim(String sim) {

        SVehicleInfo vehicleInfo = simCache.get(sim);
        if (vehicleInfo == null) {
            return null;
        }
        int vehicleId = vehicleInfo.getVehicleId();
        String vehicleIdStr = ConvertUtil.getStringValue(vehicleId);
        return vehicleIdStr;
    }

    /**
     * 获取messageID
     *
     * @param seqId
     * @return
     */
    public int getMessageID(long seqId) {
        int id = messageID;
        idCache.put(id, seqId);
        messageID++;
        return id;
    }

    /**
     * 获取seqId
     *
     * @param messageID
     * @return
     */
    public long getSeqId(int messageID) {
        try {
            long seqId = idCache.get(messageID);
//            logger.info("cache size:"+idCache.size());
            return seqId;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 存入命令缓存
     *
     * @param message
     */
    public void putCommand(CommandMessage message) {
        commandCache.put(message.getMessageID(), message);
    }

    /**
     * 移除命令缓存
     *
     * @param messageID
     */
    public void removeCommand(int messageID) {
        commandCache.invalidate(messageID);
    }

    public class TimerTaskHandler extends TimerTask {
        @Override
        public void run() {
            commandCache.cleanUp();
        }
    }

    private void init() {
//        try {
//            Thread.sleep(50000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Map<Object, Object> baseMap = cohDao.loadVehicle();
        for (Map.Entry<Object, Object> entry : baseMap.entrySet()) {
            if (entry.getValue() instanceof SVehicleInfo) {
                SVehicleInfo vehicle = (SVehicleInfo) entry.getValue();
                putVehicleCache(vehicle);
            }
        }
        idCache = CacheBuilder.newBuilder().maximumSize(150000).expireAfterWrite(1, TimeUnit.MINUTES)
                .build(new CacheLoader<Integer, Long>() {
                    @Override
                    public Long load(Integer key) throws Exception {
                        return (long) 0;
                    }
                });
        // 这个方法时当缓存中有key-value 被删除时调用 rn承载被删除的k-v对象
        commandCache = CacheBuilder.newBuilder().maximumSize(150000).expireAfterWrite(30, TimeUnit.SECONDS)
                .removalListener((RemovalListener<Integer, CommandMessage>) cache -> {
                    // 判断是否是超时删除的，而不是被替换删除的（因为替换的数据也会被onRemoval接收到）（过期的）
                    try {
                        lock.lock();
                        if (RemovalCause.EXPIRED == cache.getCause()) {
                            CommandMessage message = cache.getValue();
                            logger.debug("Timer delete command :" + message.getCmd() + ":" + message.getSim() + ".");
                            DTerminalCommandResponse dtcr = new DTerminalCommandResponse();

                            long seqId = getSeqId(message.getMessageID());
                            String vehicleId = getVehicleIdBySim(message.getSim());
                            dtcr.setSeqID(seqId);
                            dtcr.setVid(vehicleId);

                            dtcr.setCmdID(message.getCmd());
                            dtcr.setContent("终端返回应答超时,请过1分钟再发送指令... ...");
                            dtcr.setResult(1);

                            cohDao.putCoherence("mapCommandResponse", String.valueOf(seqId), dtcr);

                        } else if (RemovalCause.EXPLICIT == cache.getCause()) {
                            CommandMessage message = cache.getValue();
                            logger.debug("Timer delete command :" + message.getCmd() + ":" + message.getSim() + ".");
                        }

                    } finally {
                        lock.unlock();
                    }
                }).build(new CacheLoader<Integer, CommandMessage>() {
                    @Override
                    public CommandMessage load(Integer key) {
                        return null;
                    }
                });
        TimerTaskHandler offlineTask = new TimerTaskHandler();
        Timer timer = new Timer();
        timer.schedule(offlineTask, 30000, 10000);
    }


}
