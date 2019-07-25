package com.haikang.datacenter.handler;

import com.cnpc.vms.coherence.entity.DCommandInfo;
import com.cnpc.vms.coherence.entity.DTerminalCommandResponse;
import com.haikang.datacenter.bus.Command;
import com.haikang.datacenter.command.CommandFactory;
import com.haikang.datacenter.kafka.KafkaTopic;
import com.haikang.common.messagebody.*;
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
import java.util.List;


/**
 * Filename:    HaiKangCommandHandler
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-5-20 16:49
 * Description:
 */
@Component
public class HaiKangCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(HaiKangCommandHandler.class);

    private static CommandFactory factory = new CommandFactory();

    @Autowired
    private FastDFSCHandler handler ;

    public FastDFSCHandler getHandler() {
        return handler;
    }

    public void setHandler(FastDFSCHandler handler) {
        this.handler = handler;
    }

    private BaseInfoHandler baseHandler = BaseInfoHandler.getInstance();

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public Command prepareHKCommand(DCommandInfo cmdInfo, String sim, long seqId) {

        String topic = new String();
        CommandMessage message;
        int cmd = cmdInfo.getCmdID();
        String param = cmdInfo.getCmdContent();
        int messageID;
        switch (cmd) {
            case 2002: {
                topic = KafkaTopic.DRIVER_UPDATE_SEND;
                String[] photoMessage = param.split(";");
                messageID = BaseInfoHandler.getInstance().getMessageID(seqId);
                message = factory.transformData(cmd, sim, messageID, photoMessage);
                break;
            }
            case 2003: {
                topic = KafkaTopic.DRIVER_DELETE_SEND;
                String[] driverMessage = param.split(";");
                messageID = BaseInfoHandler.getInstance().getMessageID(seqId);
                message = factory.transformData(cmd, sim, messageID, driverMessage);
                break;
            }
            case 2004: {
                topic = KafkaTopic.DRIVER_SELECT_SEND;
                String[] driverMessage = param.split(";");
                messageID = BaseInfoHandler.getInstance().getMessageID(seqId);
                message = factory.transformData(cmd, sim, messageID, driverMessage);
                break;
            }
            case 2005: {
                topic = KafkaTopic.DRIVER_UPLOAD_SEND;
                String[] driverMessage = param.split(";");
                messageID = BaseInfoHandler.getInstance().getMessageID(seqId);
                message = factory.transformData(cmd, sim, messageID, driverMessage);
                break;
            }
            case 2007: {
                topic = KafkaTopic.DEVICE_ACTIVE_RECOGNITION_SEND;
                messageID = BaseInfoHandler.getInstance().getMessageID(seqId);
                message = factory.transformData(cmd, sim, messageID, null);
                break;
            }
            default: {
                return null;
            }
        }
        Command command = ToolUtil.packageCommand(sim, message, topic);
        return command;
    }

    public DTerminalCommandResponse getHKCommandResponse(Command command) {

        String topic = command.getSource();
        String sim = ConvertUtil.getStringValue(command.getParam());
        Object value = command.getResult();
        if (null == sim || null == value) {
            logger.error("receive error data for topic:(" + topic + ")");
            return null;
        }
        // 推送到缓存，提供web端显示
        DTerminalCommandResponse dtcr = new DTerminalCommandResponse();

        String vehicleId = baseHandler.getVehicleIdBySim(sim);
        dtcr.setVid(vehicleId);

        // 获取终端返回信息推送给平台
        String siteResponse = "null";
        switch (topic) {
            case KafkaTopic.DRIVER_UPDATE_RECEIVE: {
                if (value instanceof Message0x0002) {
                    Message0x0002 message = (Message0x0002) value;
                    int result = message.getResult();
                    if (result==0){
                        siteResponse = "添加证件照成功";
                    }else if (result==1){
                        siteResponse = "覆盖证件照成功";
                    }else {
                        siteResponse = "无法添加新的证件照";
                    }
                    dtcr.setContent(siteResponse);
                    int answerMessageID = message.getAnswerMessageID();
                    long dbSeq = baseHandler.getSeqId(answerMessageID);
                    dtcr.setCmdID(2002);
                    dtcr.setSeqID(dbSeq);
                    dtcr.setResult(0);
                    baseHandler.removeCommand(answerMessageID);
                } else {
                    logger.warn("0002收到未知消息体");
                    return null;
                }
                break;
            }
            case KafkaTopic.DRIVER_DELETE_RECEIVE: {
                if (value instanceof Message0x0003) {
                    Message0x0003 message = (Message0x0003) value;
                    int result = message.getResult();
                    if (result==0) {
                        siteResponse = "删除司机信息成功";
                    }else {
                        siteResponse = "删除司机信息失败";
                    }
                    dtcr.setContent(siteResponse);
                    int answerMessageID = message.getAnswerMessageID();
                    long dbSeq = baseHandler.getSeqId(answerMessageID);
                    dtcr.setCmdID(2003);
                    dtcr.setSeqID(dbSeq);
                    dtcr.setResult(0);
                    baseHandler.removeCommand(answerMessageID);
                } else {
                    logger.warn("0003收到未知消息体");
                    return null;
                }
                break;
            }
            case KafkaTopic.DRIVER_SELECT_RECEIVE: {
                if (value instanceof Message0x0004) {
                    Message0x0004 message = (Message0x0004) value;
                    logger.info("收到指令回馈，value:"+value.toString());
                    List<String> driverIdLIst = message.getDriverIdList();
                    if (driverIdLIst.size()!=0) {
                        StringBuilder sb = new StringBuilder();
                        for (String driverId : driverIdLIst) {
                            sb.append(driverId);
                            sb.append(",");
                        }
                        sb.deleteCharAt(sb.length() - 1);
                        siteResponse = sb.toString();
                    }else {
                        siteResponse = "设备中没有司机信息";
                    }
                    dtcr.setContent(siteResponse);
                    int answerMessageID = message.getAnswerMessageID();
                    long dbSeq = baseHandler.getSeqId(answerMessageID);
                    dtcr.setCmdID(2004);
                    dtcr.setSeqID(dbSeq);
                    dtcr.setResult(0);
                    baseHandler.removeCommand(answerMessageID);
                } else {
                    logger.warn("0004收到未知消息体");
                    return null;
                }
                break;
            }
            case KafkaTopic.DRIVER_UPLOAD_RECEIVE: {
                if (value instanceof Message0x0005) {
                    Message0x0005 message = (Message0x0005) value;
                    String driverId = message.getDriverID();
                    String fid = "null";
                    if (driverId!=DeviceTypeConstant.NO_DRIVER_ID) {
                        byte[] photo = message.getPhoto();
                        if (photo != null) {
                            int check = message.getCheck();
                            int photoCheck = BytesUtil.bytes2int2(CrcUtil.check(photo));
                            if (check != photoCheck) {
                                logger.warn("上传照片校验不合格：vehicleId" + vehicleId + ",driverID" + driverId);
                            } else {
                                Date dt = new Date();
                                String time = sdf.format(dt);
                                String photoName = "HaiKangG4" + File.separator + time + "_upload.jpg";
                                fid = handler.uploadFastDfs(photo, photoName, DeviceTypeConstant.UPLOAD_DRIVER, vehicleId, driverId);
                            }
                        } else {
                            logger.warn("上传未收到照片：sim" + sim + ",driverID" + driverId);
                        }
                    }
                    siteResponse = driverId + ";" + fid;
                    dtcr.setContent(siteResponse);
                    int answerMessageID = message.getAnswerMessageID();
                    long dbSeq = baseHandler.getSeqId(answerMessageID);
                    dtcr.setCmdID(2002);
                    dtcr.setSeqID(dbSeq);
                    dtcr.setResult(0);
                    baseHandler.removeCommand(answerMessageID);
                } else {
                    logger.warn("0005收到未知消息体");
                    return null;
                }
                break;
            }
            case KafkaTopic.DEVICE_ACTIVE_RECOGNITION_RECEIVE: {
                if (value instanceof Message0x0008) {
                    Message0x0008 message = (Message0x0008) value;
                    String driverId = message.getDriverID();
                    byte[] photo = message.getPhoto();
                    int type = message.getType();
                    String fid = "null";
                    if (photo != null) {
                        int check = message.getCheck();
                        int photoCheck = BytesUtil.bytes2int2(CrcUtil.check(photo));
                        if (check != photoCheck) {
                            logger.warn("主动验证照片校验不合格：vehicleId" + vehicleId + ",driverID" + driverId);
                        } else {
                            Date dt = new Date();
                            String time = sdf.format(dt);
                            String photoName = "HaiKangG4" + File.separator + time + "_recognition.jpg";
                            fid = handler.uploadFastDfs(photo, photoName, type, vehicleId, driverId);
                        }
                    } else {
                        logger.warn("主动验证未收到照片：vehicleId" + vehicleId + ",driverID" + driverId);
                    }
                    String typeStr = "null";
                    switch (type){
                        case DeviceTypeConstant.NORMAL_DRIVER:{
                            typeStr="正常司机";
                            break;
                        }
                        case DeviceTypeConstant.CHANGE_DRIVER:{
                            typeStr="更换司机";
                            break;
                        }
                        case DeviceTypeConstant.ILLEGAL_DRIVER:{
                            typeStr="非法的司机";
                            break;
                        }
                        case DeviceTypeConstant.NO_DRIVER:{
                            typeStr="无司机";
                            break;
                        }
                        default:{
                            typeStr="未知事件";
                            break;
                        }
                    }

                    siteResponse = message.getTime()+";"+driverId + ";" + fid + ";" + typeStr;
                    dtcr.setContent(siteResponse);
                    int answerMessageID = message.getAnswerMessageID();
                    long dbSeq = baseHandler.getSeqId(answerMessageID);
                    dtcr.setCmdID(2007);
                    dtcr.setSeqID(dbSeq);
                    dtcr.setResult(0);
                    baseHandler.removeCommand(answerMessageID);
                } else {
                    logger.warn("0008收到未知消息体");
                    return null;
                }
                break;
            }
            default: {
                logger.warn("收到未知消息体");
                return null;
            }
        }
        return dtcr;
    }

}
