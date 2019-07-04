package com.haikang.datacenter.bus;


/** 
* @ClassName: BusListener 
* @Description: 监听接口
* @author hekun
* @date 2017年6月26日 下午2:15:52 
*  
*/
public interface BusListener {


	/** 
	* @Title: commandReceived 
	* @Description: 接收消息
	* @param @param command
	* @param @param connectName    参数
	* @return void 
	* @throws: 
	*/
	void commandReceived(Command command, String connectName);
	
	
	/**
	* @Title: init
	* @Description: 初始化
	* @param @param para    参数
	* @return void    返回类型
	* @throws
	*/
	void init();
}
