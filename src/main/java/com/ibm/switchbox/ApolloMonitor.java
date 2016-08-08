package com.ibm.switchbox;

/**
 * In this class, we will send http request to call Apollo server rest api to check the status of our topics to make 
 * sure whether there is some client that subscripted to these topics. Then we can decided whether we should push message
 * to Apollo from Kafka side with. following is the rest api call.
 * https://localhost:61681/api/json/broker/virtual-hosts/switchbox/topics/switchbox?producers=false&consumers=false
 * **/
public class ApolloMonitor {

	//TODO, Use this class to 
}
