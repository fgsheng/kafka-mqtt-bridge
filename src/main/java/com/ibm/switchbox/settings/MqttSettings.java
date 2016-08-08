package com.ibm.switchbox.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttSettings {

    private String username;
    private String password;
    private String url;
    private boolean autostartup;
    private String topic;
    private int qos;
    private String inboundclientid;
    private String outboundclientid;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isAutostartup() {
		return autostartup;
	}
	public void setAutostartup(boolean autostartup) {
		this.autostartup = autostartup;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public int getQos() {
		return qos;
	}
	public void setQos(int qos) {
		this.qos = qos;
	}
	public String getInboundclientid() {
		return inboundclientid;
	}
	public void setInboundclientid(String inboundclientid) {
		this.inboundclientid = inboundclientid;
	}
	public String getOutboundclientid() {
		return outboundclientid;
	}
	public void setOutboundclientid(String outboundclientid) {
		this.outboundclientid = outboundclientid;
	}
    
    
}