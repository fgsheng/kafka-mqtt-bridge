package com.ibm.switchbox;

import java.util.regex.Pattern;

public class MqttKafkaBridgeTest {

	public static void main(String args[])
	{
		boolean b = Pattern.matches("switchbox.*", "switchbox.orga.ac");
		System.out.println(b);
		boolean c = Pattern.matches("switchbox*", "switchbox.orga.ac");
		System.out.println(c);
	}

}
