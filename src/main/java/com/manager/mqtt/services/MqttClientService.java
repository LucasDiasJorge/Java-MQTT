package com.manager.mqtt.services;

import org.eclipse.paho.client.mqttv3.*;

import java.util.Date;

public class MqttClientService extends Thread{

    private final String topic;

    private final MqttClient mqttClient;

    public MqttClientService(String topic, MqttClient mqttClient) {
        this.topic = topic;
        this.mqttClient = mqttClient;
    }

    public void run(){

        while (true) {

            System.out.println("\tMQTT as a Thread:");
            System.out.println("\t"+topic);

            MqttMessage message = new MqttMessage();
            message.setPayload("PAPAI".getBytes());

            try {
                mqttClient.subscribe(topic, 0);
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }

            try {
                System.out.println("Publisher\n");
                mqttClient.publish(topic, message);
            } catch (MqttException e) {
                System.out.println("Topic");
                System.out.println(topic);
                throw new RuntimeException(e);
            }

            mqttClient.setCallback(new MqttCallback() {

                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    // Called when a message arrives from the server that
                    // matches any subscription made by the client

                    Date time = new Date();
                    System.out.println("\nReceived a Message!" +
                            "\n\tTime:    " + time +
                            "\n\tTopic:   " + topic +
                            "\n\tMessage: " + new String(message.getPayload()) +
                            "\n\tQoS:     " + message.getQos() + "\n");
                }

                public void connectionLost(Throwable cause) {
                    System.out.println("Connection to Solace messaging lost!" + cause.getMessage());
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                }

            });

            try {
                Thread.sleep(1000); // Delay for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
