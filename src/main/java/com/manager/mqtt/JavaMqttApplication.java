package com.manager.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class JavaMqttApplication {

	public static void main(String[] args){
		SpringApplication.run(JavaMqttApplication.class,args);
	}

}
