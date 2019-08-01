package com.haikang.datacenter.kafka;

/**
 * Filename:    KafkaTopic
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-5-8 17:25
 * Description:
 */
public class KafkaTopic {

    /**
     * 设备注册
     */
    public final static String DEVICE_REGISTER = "hk-device-register";
    /**
     * 司机库更新发送
     */
    public final static String DRIVER_UPDATE_SEND = "hk-driver-update-send";
    /**
     * 司机库更新接收
     */
    public final static String DRIVER_UPDATE_RECEIVE = "hk-driver-update-receive";
    /**
     * 司机库删除发送
     */
    public final static String DRIVER_DELETE_SEND = "hk-driver-delete-send";
    /**
     * 司机库删除接收
     */
    public final static String DRIVER_DELETE_RECEIVE = "hk-driver-delete-receive";
    /**
     * 司机库查询发送
     */
    public final static String DRIVER_SELECT_SEND = "hk-driver-select-send";
    /**
     * 司机库查询接收
     */
    public final static String DRIVER_SELECT_RECEIVE = "hk-driver-select-receive";
    /**
     * 司机库证件照上传发送
     */
    public final static String DRIVER_UPLOAD_SEND = "hk-driver-upload-send";
    /**
     * 司机库证件照上传接收
     */
    public final static String DRIVER_UPLOAD_RECEIVE = "hk-driver-upload-receive";

    /**
     * 设备事件上传接收
     */
    public final static String DEVICE_EVENT_UPLOAD_RECEIVE = "hk-event-upload";
    /**
     * 设备心跳
     */
    public final static String DEVICE_HEART_BEAT = "hk-device-heart";
    /**
     * 平台主动识别发送
     */
    public final static String DEVICE_ACTIVE_RECOGNITION_SEND = "hk-recognition-send";
    /**
     * 设备主动识别接收
     */
    public final static String DEVICE_ACTIVE_RECOGNITION_RECEIVE = "hk-recognition-receive";

    /**
     * 设备主动上传事件发送前端
     */
    public final static String DEVICE_ACTIVE_RECOGNITION_DRIVER = "platform-instruct-G4-2006";
    /**
     * 车辆信息修改
     */
    public final static String VEHICLE_INFO = "static-vms-vehicleInfo";
    /**
     * 指令下发
     */
    public final static String COMMAND = "vms_et_cahce_command";


}
