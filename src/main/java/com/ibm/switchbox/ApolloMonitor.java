package com.ibm.switchbox;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * In this class, we will send http request to call Apollo server rest api to check the status of our topics to make 
 * sure whether there is some client that subscripted to these topics. Then we can decided whether we should push message
 * to Apollo from Kafka side with. following is the rest api call.
 * https://localhost:61681/api/json/broker/virtual-hosts/switchbox/topics/switchbox?producers=false&consumers=false
 * **/
@Component
@ConfigurationProperties(prefix = "monitor")
public class ApolloMonitor implements Runnable {

	public static final String REQ_URL = "http://admin:password@localhost:61680/api/json/broker/connectors/tcp";
	
	public static final int INTERVAL = 2000;

	private int connectedCount;

	public ApolloMonitor(){
		new Thread(this).start();
	}

	public int getConnectedCount() {
		return connectedCount;
	}

	public void setConnectedCount(int connectedCount) {
		this.connectedCount = connectedCount;
	}

	@Override
	public void run() {
		while(true){
			try {
				String content = Request.Get(REQ_URL).execute().returnContent().asString();
				ObjectMapper mapper = new ObjectMapper();
				String count = mapper.readTree(content).get("connected").toString();
				this.connectedCount = Integer.valueOf(count);
//			    System.out.println("Apollo monitor: current connection count is "+ this.connectedCount);
			    Thread.currentThread().sleep(INTERVAL);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
	}
	
	
}
