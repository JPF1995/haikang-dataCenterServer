package com.haikang.datacenter.bus;


/**
 * @author hekun
 * @ClassName: BusListener
 * @Description: 监听接口
 * @date 2017年6月26日 下午2:15:52
 */
public interface BusListener {


    /**
     * @param @param command
     * @param @param connectName    参数
     * @return void
     * @Title: commandReceived
     * @Description: 接收消息
     * @throws:
     */
    void commandReceived(Command command, String connectName);


    /**
     * @param @param para    参数
     * @return void    返回类型
     * @throws
     * @Title: init
     * @Description: 初始化
     */
    void init();
}
