package com.ibm.switchbox.clients;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

public class MQTTProducer implements MqttCallback {

	public static final String HOST = "tcp://localhost:61613";
	public static final String TOPIC = "switchbox/a/b";
	private static final String clientid = "server";

	private MqttClient client;
	private MqttTopic topic;
	private String userName = "admin";
	private String passWord = "password";

	private MqttMessage message;

	public MQTTProducer() throws MqttException {
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
			client.setCallback(this);
			client.connect(options);
			topic = client.getTopic(TOPIC);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void publish(MqttMessage message) throws MqttPersistenceException, MqttException {
		MqttDeliveryToken token = topic.publish(message);
		token.waitForCompletion();
		System.out.println(token.isComplete() + "========");
	}

	public static void main(String[] args) throws MqttException, InterruptedException {
		MQTTProducer server = new MQTTProducer();
		for (int i = 0; i < 1; i++) {
			server.message = new MqttMessage();
			server.message.setQos(1);
			server.message.setRetained(false);
			server.message.setPayload(("[" + i + "] DDDD " + UUID.randomUUID().toString()).getBytes());
			server.publish(server.message);
			Thread.currentThread().sleep(1000);
		}
		server.client.disconnect();
	}

	public void connectionLost(Throwable cause) {
		System.out.println("disconnected");
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		System.out.println("deliveryComplete " + token.isComplete());
	}

	@Override
	public void messageArrived(String arg0, MqttMessage message) throws Exception {
		System.out.println("Recieved topic:" + arg0);
		System.out.println("Recieved Message Qos:" + message.getQos());
		System.out.println("Recieved Message content:" + new String(message.getPayload()));

	}
}