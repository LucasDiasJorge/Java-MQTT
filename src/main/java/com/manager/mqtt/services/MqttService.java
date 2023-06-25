package com.manager.mqtt.services;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MqttService {

    @EventListener(ApplicationReadyEvent.class) // Substitute of extend thread and run in a while looping at main function
    public void run() {
        System.out.println("MQTT is running ...");

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("your_username");
            options.setPassword("your_password".toCharArray());

            MqttClient mqttClient = new MqttClient("tcp://services.com", "HelloWorldSub");
            mqttClient.connect(options);

            mqttClient.subscribe("/testtopic/", 0);
            mqttClient.setCallback(new MqttCallback() {
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    handleIncomingMessage(topic, message);
                }

                public void connectionLost(Throwable cause) {
                    handleConnectionLost(cause);
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });

            while (true) {
                System.out.println("Listening ...");
                MqttMessage message = new MqttMessage();
                message.setPayload("{secret content}".getBytes());
                mqttClient.publish("/testtopic/", message);

                try {
                    Thread.sleep(1000); // Delay for 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleIncomingMessage(String topic, MqttMessage message) {
        Date time = new Date();
        System.out.println("\nReceived a Message!" +
                "\n\tTime:    " + time +
                "\n\tTopic:   " + topic +
                "\n\tMessage: " + new String(message.getPayload()) +
                "\n\tQoS:     " + message.getQos() + "\n");
    }

    private void handleConnectionLost(Throwable cause) {
        System.out.println("Connection to Solace messaging lost!" + cause.getMessage());
    }
}
