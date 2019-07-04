package com.haikang.datacenter.handler;

import com.haikang.datacenter.dao.FastDfsDao;
import com.haikang.datacenter.fastdfs.FastDFSClient;
import com.haikang.datacenter.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Filename:    FastDFSCHandler
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-5-28 9:35
 * Description:
 */
@Component
public class FastDFSCHandler {

    private static final Logger logger = LoggerFactory.getLogger(FastDFSCHandler.class);

    //private static volatile FastDFSCHandler instance = null;

    @Autowired
    private FastDfsDao fastDao;

    public FastDfsDao getFastDao() {
        return fastDao;
    }

    public void setFastDao(FastDfsDao fastDao) {
        this.fastDao = fastDao;
    }

    //    public static FastDFSCHandler getInstance() {
//        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
//        if (instance == null) {
//            // 同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
//            synchronized (FastDFSCHandler.class) {
//                // 未初始化，则初始instance变量
//                if (instance == null) {
//                    instance = new FastDFSCHandler();
//                }
//            }
//        }
//        return instance;
//    }

    /**
     * 上传文件
     *
     * @param data         文件信息
     * @param FilePathName 文件名称
     */
    public String uploadFastDfs(byte[] data, String FilePathName, int type, String vehicleId, String driverID) {
//        logger.info("文件上传任务:" + FilePathName + "开始");

        // 元数据信息存入dfs
        Map<String, String> metaList = new HashMap<String, String>(5);
        metaList.put("vehicleId", vehicleId);
        metaList.put("driverID", driverID);

        String fid = FastDFSClient.uploadFile(data, FilePathName, metaList);
        if (fid == null) {
            return "null";
        }
        try {
            fastDao.saveMeta(FilePathName, type, vehicleId, driverID, fid);
            logger.debug("文件上传任务:" + FilePathName + "完成");
        } catch (Exception e) {
            logger.warn("文件上传任务:" + FilePathName + "失败", e);
        }
        return fid;
    }

    public int insertDriverChange(String vehicleId,String driverId,int type,String fid){
        int result = fastDao.insertDriverChange( vehicleId, driverId, fid,type);
        return result;
    }
}
