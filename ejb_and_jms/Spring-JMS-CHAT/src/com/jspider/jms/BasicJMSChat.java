package com.jspider.jms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

public class BasicJMSChat implements MessageListener{

	private JmsTemplate chatJmsTemplate;
	public JmsTemplate getChatJmsTemplate() {
		return chatJmsTemplate;
	}

	public void setChatJmsTemplate(JmsTemplate chatJmsTemplate) {
		this.chatJmsTemplate = chatJmsTemplate;
	}

	private Topic chatTopic;
	private static String userID;

	private void subscribe(TopicConnection topicConnection, Topic chatTopic,
			BasicJMSChat commandLineChat) throws JMSException {
		TopicSession TopicSession=topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		TopicSubscriber ts=TopicSession.createSubscriber(chatTopic);
		ts.setMessageListener(commandLineChat);

	}

	private void publish(TopicConnection topicConnection, Topic chatTopic,
			String userID) throws JMSException, IOException {
		TopicSession topicSession=topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		TopicPublisher topicPublisher= topicSession.createPublisher(chatTopic);
		topicConnection.start();

		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String msgToSend=reader.readLine();
			if (msgToSend.equalsIgnoreCase("exit")) {
				topicConnection.close();
			} else {
				TextMessage msg=topicSession.createTextMessage();
				msg.setText("\n["+userID+":"+msgToSend+"]");
				topicPublisher.publish(msg);
			}

		}
	}

	@Override
	public void onMessage(Message message) {

		if (message instanceof Message) {
			try {
				String msgText=((TextMessage)message).getText();
				if (!msgText.startsWith("["+userID)) {
					System.out.println(msgText);
				}
			} catch (JMSException e) {
				e.getStackTrace();
			}
		} else {
			String errMsg="Message is not of expected type testmessage";
			System.err.println(errMsg);
			throw new RuntimeException(errMsg);
		}
	}

	public static void main(String[] args) throws JMSException, IOException {
		if (args.length !=1) {
			System.out.println("User name is required...");
		} else {
			userID=args[0];
			ApplicationContext context=new ClassPathXmlApplicationContext("app-context.xml");

			BasicJMSChat basicJMSChat=(BasicJMSChat) context.getBean("basicJMSChat");

			TopicConnectionFactory topicConnectionFactory=(TopicConnectionFactory) basicJMSChat.chatJmsTemplate.getConnectionFactory();
			TopicConnection topicConnection=topicConnectionFactory.createTopicConnection();

			basicJMSChat.publish(topicConnection,basicJMSChat.chatTopic,userID);
			basicJMSChat.subscribe(topicConnection,basicJMSChat.chatTopic,basicJMSChat);
		}

	}



}
