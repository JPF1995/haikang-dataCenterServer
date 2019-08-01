package com.haikang.datacenter.bus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author jakiro
 * @version V1.0
 * @Description 监听
 * @Date 2015年7月17日 下午10:37:55
 * @mail terrorbladeyang@gmail.com
 */
public class ListenerConfig {


    private BusListener listener; //指令监听端口
    private int threads;  //线程数
    private ExecutorService exec; //线程池对象


    /**
     * <p>Title: 构造函数。</p>
     * <p>Description: 1、记录监听器。2、创建线程池。</p>
     *
     * @param listener：一个监听器，如果一个队列有多个监听器就要创建多个ListenerConfig。
     * @param threads：线程池的大小。
     * @param threadName：队列名称，也是线程的名称。
     */
    public ListenerConfig(BusListener listener, int threads, String threadName) {
        this.listener = listener; //绑定接口
        this.threads = threads; //绑定线程数
        final String name = threadName; //绑定名
        exec = Executors.newFixedThreadPool(threads, new ThreadFactory() { //用线程工厂方式 命名线程
            AtomicInteger num = new AtomicInteger();

            @Override
            public Thread newThread(Runnable arg0) {
                Thread thread = new Thread(arg0);
                thread.setName(name + "_Listener-" + num.incrementAndGet());
                return thread;
            }
        });
    }

    protected BusListener getListener() {
        return listener;
    }

    protected int getThreads() {
        return threads;
    }

    protected ExecutorService exec() {
        return exec;
    }


}
