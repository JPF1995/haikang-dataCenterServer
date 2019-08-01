package com.haikang.datacenter.command.process;

import com.haikang.common.messagebody.CommandMessage;
import com.haikang.common.messagebody.Message0x8002;
import com.haikang.datacenter.fastdfs.FastDFSClient;
import com.haikang.util.BytesUtil;
import com.haikang.util.CrcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Filename:    Process2002
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-5-21 19:27
 * Description:
 */

public class Process2002 extends CommandProcess {

    private static final Logger logger = LoggerFactory.getLogger(Process2002.class);

    @Override
    public CommandMessage transformData(String sim, int messageID, String[] param) {
        CommandMessage message = new Message0x8002();

        String driverID = param[0];
        String fileId = param[1];
        byte[] photo = FastDFSClient.getFileData(fileId);
        if (photo == null) {
            logger.info("无法获取照片,fileID:" + fileId);
            return null;
        }
        int photoLength = photo.length;
        int check = BytesUtil.bytes2int2(CrcUtil.check(photo));
        int bodyLength = 46 + photoLength;

        message.setCmd(0x8002);
        message.setSim(sim);
        message.setMessageID(messageID);
        message.setBodyLength(bodyLength);
        ((Message0x8002) message).setDriverID(driverID);
        ((Message0x8002) message).setPhoto(photo);
        ((Message0x8002) message).setPhotoLength(photoLength);
        ((Message0x8002) message).setCheck(check);
        return message;
    }

}
