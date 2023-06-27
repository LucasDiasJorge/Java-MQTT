package com.manager.mqtt.services;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MqttService{

    private final JdbcTemplate jdbcTemplate;

    public MqttService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void mqtt() {

        System.out.println("MQTT is running ...");

        List<String> portalMacs = jdbcTemplate.queryForList("SELECT mac_address FROM sys_portal WHERE deleted_at IS NOT NULL", String.class);

        //System.out.println(portalMacs.toString());

        MqttClient mqttClient = null;

        try {
            mqttClient = new MqttClient("tcp://services.com", "HelloWorldSub");
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }

        try {

            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("your_username");
            options.setPassword("your_password".toCharArray());

            mqttClient.connect(options);

        } catch (MqttException e) {
            throw new RuntimeException(e);
        }

        for (String mac : portalMacs) {

            new MqttClientService(mac, mqttClient).start();
            System.out.println("others");
        }
    }
}
