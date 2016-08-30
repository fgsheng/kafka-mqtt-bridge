package com.ibm.switchbox.clients;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic; 
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;

public class ActiveMQJMSClient {
	public static void main(String[] args) throws Exception {
		ActiveMQConnectionFactory factory  = new ActiveMQConnectionFactory("admin", "password", "tcp://localhost:1414");
		Connection connection = factory.createConnection();
		connection.start();

		// Create Queue
		Topic topic = new ActiveMQTopic("switchbox.a.b");
		// Create Session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// Create consumer
		MessageConsumer comsumer1 = session.createConsumer(topic);
		comsumer1.setMessageListener(new MessageListener() {
			public void onMessage(Message m) {
				try {
					System.out.println("Consumer1 get " + ((TextMessage) m).getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});

		// Create producer, send message
		MessageProducer producer = session.createProducer(topic);
		for (int i = 0; i < 1; i++) {
			producer.send(session.createTextMessage("Message:" + i));
		}
	}
}