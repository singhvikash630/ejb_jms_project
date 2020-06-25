package com.jspider.jms;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class HelloWorldProducer {

	public static void main(String[] args) {
		try {
			ActiveMQConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://INDELPC389:61616");
			Connection connection=connectionFactory.createConnection();
			connection.start();
			Session session=connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination=session.createQueue("vikash");
			
			MessageProducer producer=session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			
			String text="Hello World! From:"+Thread.currentThread().getName();
			TextMessage message=session.createTextMessage(text);
			
			System.out.println("Sent message:"+message.hashCode()+Thread.currentThread().getName());
			producer.send(message);
			session.close();
			connection.close();
		} catch (Exception e) {
			
			System.out.println(e.getStackTrace());
		}

	}

}
