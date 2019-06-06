package com.github.maxopoly.kira.rabbit.session;

import org.json.JSONObject;

public abstract class RequestSession {

	private String sendingKey;

	public RequestSession(String sendingKey) {
		this.sendingKey = sendingKey;
	}

	public abstract JSONObject getRequest();

	public String getSendingKey() {
		return sendingKey;
	}

	public abstract void handleReply(JSONObject json);
}
