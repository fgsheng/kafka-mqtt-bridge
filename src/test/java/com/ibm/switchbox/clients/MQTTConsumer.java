package com.ibm.switchbox.clients;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

public class MQTTConsumer implements MqttCallback {

	public static final String HOST = "tcp://localhost:1414";
	public static final String TOPIC = "switchbox/a/b";
	private static final String clientid = "clientA";
	private MqttClient client;
	private MqttConnectOptions options;
	private String userName = "admin";
	private String passWord = "password";

	private ScheduledExecutorService scheduler;

	// reconnect
	public void startReconnect() {
		if (!client.isConnected()) {
			try {
				client.connect(options);
				System.out.println("Reconnect status: "+ client.isConnected());
			} catch (MqttSecurityException e) {
				e.printStackTrace();
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
	}

	private void start() {
		try {
			client = new MqttClient(HOST, clientid, new MqttDefaultFilePersistence());
			options = new MqttConnectOptions();
			options.setCleanSession(false);
			options.setUserName(userName);
			options.setPassword(passWord.toCharArray());
			options.setConnectionTimeout(10);
			options.setKeepAliveInterval(20);
			client.setCallback(this);
//			MqttTopic topic = client.getTopic(TOPIC);
//			options.setWill(topic, (client.getClientId()+" DISCONNECTED").getBytes(), 1, true);
			client.connect(options);
			System.out.println("CLient connect status:"+ client.isConnected());
			// 订阅消息
			int[] Qos = { 1 };
			String[] topic1 = { TOPIC };
			client.subscribe(topic1, Qos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			client.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void connectionLost(Throwable cause) {
		// 连接丢失后，一般在这里面进行重连
		System.out.println("连接断开，开始重连");
		try {
			Thread.currentThread().sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.startReconnect();
	}
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// publish后会执行到这里
		System.out.println("deliveryComplete---------" + token.isComplete());
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// subscribe后得到的消息会执行到这里面
		System.out.print("接收消息主题:" + topic);
		System.out.print(" 接收消息Qos:" + message.getQos());
		System.out.println(" 接收消息内容:" + new String(message.getPayload()));

	}

	public static void main(String[] args) throws MqttException {
		MQTTConsumer client = new MQTTConsumer();
		client.start();
	}
}
