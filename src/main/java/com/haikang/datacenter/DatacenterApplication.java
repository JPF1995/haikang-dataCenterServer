package com.haikang.datacenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.ConfigurableApplicationContext;

//@EnableDiscoveryClient
@SpringBootApplication
public class DatacenterApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DatacenterApplication.class, args);
        BusListenerServer busListenerServer = context.getBean(BusListenerServer.class);
        busListenerServer.init();
        busListenerServer.start();
    }

}
