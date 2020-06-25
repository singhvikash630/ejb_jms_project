package com.jspider.jms;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class HelloWorldConsumer {

	public static void main(String[] args) {
		try {
			ActiveMQConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://INDELPC389:61616");
			Connection connection=connectionFactory.createConnection();
			connection.start();
			Session session=connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination=session.createQueue("vikash");
			
			MessageConsumer consumer=session.createConsumer(destination);			
			
			Message message=consumer.receive(1000);
			if (message instanceof TextMessage) {
				TextMessage textmessage=(TextMessage)message;
				String text=textmessage.getText();
				System.out.println("Received:"+text);
			} else {
				System.out.println("Received:"+message);

			}
			
			
			session.close();
			connection.close();
		} catch (Exception e) {
			
			System.out.println(e.getStackTrace());
		}

	}

}
