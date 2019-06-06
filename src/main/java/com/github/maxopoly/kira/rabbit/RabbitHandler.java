package com.github.maxopoly.Kira.rabbit;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.github.maxopoly.kira.KiraMain;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class RabbitHandler {

	private ConnectionFactory connectionFactory;
	private String incomingQueue;
	private String outgoingQueue;
	private Logger logger;
	private Connection conn;
	private Channel incomingChannel;
	private Channel outgoingChannel;
	private RabbitInputProcessor inputHandler;

	public RabbitHandler(ConnectionFactory connFac, String incomingQueue, String outgoingQueue, Logger logger) {
		this.connectionFactory = connFac;
		this.incomingQueue = incomingQueue;
		this.outgoingQueue = outgoingQueue;
		this.logger = logger;
		this.inputHandler = new RabbitInputProcessor(logger);
	}

	public void beginAsyncListen() {
		new Thread(() -> {
			KiraMain.getInstance().getLogger().info("Beginning to listen for rabbit input...");
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				try {
					String message = new String(delivery.getBody(), "UTF-8");
					System.out.println(" [x] Received '" + message + "'");
					inputHandler.handle(message, new RabbitInputSupplier());
				} catch (Exception e) {
					// if we dont do this the exception falls back into rabbit, which causes tons of
					// problems
					logger.error("Exception in rabbit listener", e);
				}
			};
			try {
				incomingChannel.basicConsume(incomingQueue, true, deliverCallback, consumerTag -> {
				});
			} catch (Exception e) {
				logger.error("Error in rabbit listener", e);
			}
		}).start();
	}

	private boolean sendMessage(String msg) {
		try {
			outgoingChannel.basicPublish("", outgoingQueue, null, msg.getBytes("UTF-8"));
			return true;
		} catch (Exception e) {
			logger.error("Failed to send rabbit message", e);
			return false;
		}
	}

	public boolean sendMessage(String id, JSONObject json) {
		return sendMessage(id + " " + json.toString());
	}

	public boolean setup() {
		try {
			conn = connectionFactory.newConnection();
			incomingChannel = conn.createChannel();
			incomingChannel.queueDeclare(incomingQueue, false, false, false, null);
			outgoingChannel = conn.createChannel();
			outgoingChannel.queueDeclare(outgoingQueue, false, false, false, null);
			return true;
		} catch (IOException | TimeoutException e) {
			logger.error("Failed to setup rabbit connection", e);
			return false;
		}
	}

	public void shutdown() {
		try {
			incomingChannel.close();
			outgoingChannel.close();
			conn.close();
		} catch (Exception e) {
			logger.error("Failed to close rabbit connection", e);
		}
	}

}
