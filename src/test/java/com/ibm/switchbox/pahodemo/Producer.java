package com.ibm.switchbox.pahodemo;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

public class Producer {

//	public static final String HOST = "tcp://localhost:61613";
	public static final String HOST = "tcp://9.123.155.29:61613";
	public static final String TOPIC = "switchbox/a/b";
	private static final String clientid ="server"; 

	private MqttClient client;
	private MqttTopic topic;
	private String userName = "admin";
	private String passWord = "password";

	private MqttMessage message;

	public Producer() throws MqttException {
		 //MemoryPersistence设置clientid的保存形式，默认为以内存保存
		client = new MqttClient(HOST, clientid, new MqttDefaultFilePersistence());
		connect();
	}
	
	private void connect() {
		MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(20);
        try {
               client.setCallback(new PushCallback());
               client.connect(options);
               topic = client.getTopic(TOPIC);
        } catch (Exception e) {
               e.printStackTrace();
        }
	}
	
	public void publish(MqttMessage message) throws MqttPersistenceException, MqttException{
		MqttDeliveryToken token = topic.publish(message);
        token.waitForCompletion();
        System.out.println(token.isComplete()+"========");
	}

	public static void main(String[] args) throws MqttException, InterruptedException {
		Producer server =  new Producer();
		for(int i =0 ;i<3;i++)
		{
			server.message = new MqttMessage();
			server.message.setQos(1);
			server.message.setRetained(false);
			server.message.setPayload(("["+i+"] BBB " + UUID.randomUUID().toString()).getBytes());
	        server.publish(server.message);
	        Thread.currentThread().sleep(1000);
		}
        server.client.disconnect();
	}

}