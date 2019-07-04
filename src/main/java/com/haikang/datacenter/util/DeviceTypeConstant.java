package com.haikang.datacenter.util;

/**
 * Filename:    DeviceTypeConstant
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-5-28 11:46
 * Description:
 */

public class DeviceTypeConstant {

    /**
     * 正常的司机
     */
    public static final int NORMAL_DRIVER = 0;

    /**
     * 更换司机
     */
    public static final int CHANGE_DRIVER = 1;
    /**
     * 非法的司机
     */
    public static final int ILLEGAL_DRIVER = 2;
    /**
     * 无司机
     */
    public static final int NO_DRIVER = 3;

    /**
     * 上传司机照片
     */
    public static final int UPLOAD_DRIVER = 10;

    /**
     * 非法的司机id
     * 20个9
     */
    public static final String ILLEGAL_DRIVER_ID = "99999999999999999999";
    /**
     * 无司机id
     * 20个1
     */
    public static final String NO_DRIVER_ID = "11111111111111111111";
}
