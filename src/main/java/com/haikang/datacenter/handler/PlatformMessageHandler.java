package com.haikang.datacenter.handler;

import com.haikang.common.entity.DeviceEventUploadMessage;
import com.haikang.common.messagebody.Message0x0006;
import com.haikang.datacenter.bus.Command;
import com.haikang.datacenter.dao.BaseInfoFromCohDao;
import com.haikang.datacenter.kafka.KafkaTopic;
import com.haikang.datacenter.util.*;
import com.haikang.util.BytesUtil;
import com.haikang.util.CrcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Filename:    PlatformMessageHandler
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-5-30 11:08
 * Description:
 */
@Component
public class PlatformMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(PlatformMessageHandler.class);

    @Autowired
    private FastDFSCHandler fastDFSCHandler;

    public FastDFSCHandler getFastDFSCHandler() {
        return fastDFSCHandler;
    }

    public void setFastDFSCHandler(FastDFSCHandler fastDFSCHandler) {
        this.fastDFSCHandler = fastDFSCHandler;
    }

    private BaseInfoHandler baseHandler = BaseInfoHandler.getInstance();

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    private static BaseInfoFromCohDao cohDao = new BaseInfoFromCohDao();

    public Command getKafkaMessage(Command command) {
        String topic = command.getSource();
        String sim = String.valueOf(command.getParam());
        Object value = command.getResult();
        if (null == sim || null == value) {
            logger.error("receive error data for topic:(" + topic + ")");
            return null;
        }
        String vehicleId = baseHandler.getVehicleIdBySim(sim);
        if (vehicleId == null) {
            logger.error("coherence do not have vehicle by sim :(" + sim + ")");
            return null;
        }
        Message0x0006 message = (Message0x0006) value;
        DeviceEventUploadMessage event = new DeviceEventUploadMessage();
        String driverId = message.getDriverID();
        byte[] photo = message.getPhoto();
        int type = message.getType();
        String photoID = "null";
        if (photo != null) {
            int check = message.getCheck();
            int photoCheck = BytesUtil.bytes2int2(CrcUtil.check(photo));
            if (check != photoCheck) {
                logger.warn("设备上传事件照片校验不合格：vehicleId=" + vehicleId + ",driverID=" + driverId);
            } else {
                Date dt = new Date();
                String time = sdf.format(dt);
                String photoName = "HaiKangG4" + File.separator + time + "_event.jpg";
                photoID = fastDFSCHandler.uploadFastDfs(photo, photoName, type, vehicleId, driverId);
            }
        } else {
            logger.warn("设备上传事件未收到照片：vehicleId=" + vehicleId + ",driverID=" + driverId);
        }
        event.setTime(message.getTime());
        event.setDriverID(driverId);
        event.setPhotoID(photoID);
        event.setType(type);
        event.setVehicleId(vehicleId);
        saveDriverChange(event);
        cohDao.updateDriverReocgnition(vehicleId, driverId);
        Command response = ToolUtil.packageCommand(vehicleId, event, KafkaTopic.DEVICE_ACTIVE_RECOGNITION_DRIVER);
        logger.debug("成功收到设备上传事件：vehicleId=" + vehicleId + ",driverID=" + driverId + ",type=" + type);
        return response;

    }

    private void saveDriverChange(DeviceEventUploadMessage event) {
        String vehicleId = event.getVehicleId();
        String driverIdLast = cohDao.getDriverReocgnition(vehicleId);
        String driverIdNow = event.getDriverID();
        String fid = event.getPhotoID();
        int result;
//        if (DeviceTypeConstant.ILLEGAL_DRIVER_ID.equals(driverIdNow)) {
//            if (!driverIdLast.equals(driverIdNow)) {
//                result = fastDFSCHandler.insertDriverChange(vehicleId, driverIdNow, 0, fid);
//                if (result > 0) {
//                    logger.debug("司机上车情况插入成功！");
//                }
//                if (!DeviceTypeConstant.NO_DRIVER_ID.equals(driverIdLast)){
//                    result = fastDFSCHandler.insertDriverChange(vehicleId, driverIdLast, 1, fid);
//                    if (result > 0) {
//                        logger.debug("司机下车情况插入成功！");
//                    }
//                }
//            }
//        }else
        if (DeviceTypeConstant.NO_DRIVER_ID.equals(driverIdNow)) {
            if (!driverIdLast.equals(driverIdNow)) {
                result = fastDFSCHandler.insertDriverChange(vehicleId, driverIdLast, 1, fid);
                if (result > 0) {
                    logger.debug("司机下车情况插入成功！");
                }
            }
        } else {
            if (!driverIdLast.equals(driverIdNow)) {
                result = fastDFSCHandler.insertDriverChange(vehicleId, driverIdNow, 0, fid);
                if (result > 0) {
                    logger.debug("司机上车情况插入成功！");
                }
                if (!DeviceTypeConstant.NO_DRIVER_ID.equals(driverIdLast)&&!"".equals(driverIdLast)) {
                    result = fastDFSCHandler.insertDriverChange(vehicleId, driverIdLast, 1, fid);
                    if (result > 0) {
                        logger.debug("司机下车情况插入成功！");
                    }
                }

            }
        }
    }
}
