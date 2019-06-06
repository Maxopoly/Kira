package com.github.maxopoly.kira.rabbit.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.github.maxopoly.kira.rabbit.RabbitHandler;

public class RequestSessionManager {

	private static final String idField = "RequestSessionId";
	private static final String keyField = "RequestSessionKey";

	private Map<Long, RequestSession> sessions;
	private long counter;
	private RabbitHandler rabbit;
	private Logger logger;

	public RequestSessionManager(RabbitHandler rabbit, Logger logger) {
		sessions = new ConcurrentHashMap<>();
		this.logger = logger;
		counter = 0;
		this.rabbit = rabbit;
	}

	public void handleReply(JSONObject reply) {
		long id = reply.getLong(idField);
		RequestSession session = sessions.remove(id);
		if (session == null) {
			logger.error("Received invalid json with unknown code: " + reply.toString());
			return;
		}
		session.handleReply(reply);
	}

	private synchronized long pullTicket() {
		return counter++;
	}

	public void request(RequestSession session) {
		JSONObject json = session.getRequest();
		long id = pullTicket();
		sessions.put(id, session);
		json.put(keyField, session.getSendingKey());
		json.put(idField, id);
		rabbit.sendMessage("requestsession", json);
	}
}
