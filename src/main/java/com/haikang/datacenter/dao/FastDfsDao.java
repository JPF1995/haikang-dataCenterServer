package com.haikang.datacenter.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class FastDfsDao {
    private final static Logger logger = LoggerFactory.getLogger(FastDfsDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String photoSql;

    private String driverSql;

    private FastDfsDao() {
        init();
    }

    public void init() {
        photoSql = initPhotoSql();
        driverSql = initDriverSql();
    }


    public synchronized void saveMeta(String filePathName, int type, String vehicleId, String driverId, String fid) {
        // 元数据信息存入数据库
        Object[] params = new Object[]{vehicleId, driverId, filePathName, fid, type};
        int temp = jdbcTemplate.update(photoSql, params);
        if (temp > 0) {
            logger.debug("插入成功！");
        } else {
            logger.debug("插入失败");
        }

    }

    public synchronized int insertDriverChange(String vehicleId, String driverId, String fid,int type) {
        // 元数据信息存入数据库
        Object[] params = new Object[]{vehicleId, driverId,  fid, type};
        int temp = jdbcTemplate.update(driverSql, params);
        if (temp > 0) {
            logger.debug("插入成功！");
        } else {
            logger.debug("插入失败");
        }
        return temp;
    }

    public String initPhotoSql() {
        StringBuffer sb = new StringBuffer();
        sb.append("insert into g_face_recognition_photo ");
        sb.append("(id, vehicleId,driverID, fileName, fid,uploadTime, type) ");
        sb.append(" values ");
        sb.append("(SQ_FACE_RECOGNITION_PHOTO.nextval, ?, ?,?, ?, sysdate, ?)");
        return sb.toString();
    }

    public String initDriverSql() {
        StringBuffer sb = new StringBuffer();
        sb.append("insert into g_driver_change ");
        sb.append("(id, vehicleId,driverID, fid,time, type)");
        sb.append(" values ");
        sb.append("(SQ_DRIVER_CHANGE.nextval, ?, ?, ?, sysdate, ?)");
        return sb.toString();
    }


    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


}
