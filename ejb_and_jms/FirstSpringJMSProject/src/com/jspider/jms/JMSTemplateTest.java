package com.jspider.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.omg.CORBA.portable.ApplicationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class JMSTemplateTest {

	public static void main(String[] args) {
		ApplicationContext context=new ClassPathXmlApplicationContext("app-context.xml");
		JmsTemplate template=(JmsTemplate) context.getBean("jmsTemplate");
		template.send("SpringSendTestQueue", new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message=session.createTextMessage();
				message.setText("This is a text message");
				return message;
			}
		});
		System.out.println("message sent");
		System.exit(1);
	}

}