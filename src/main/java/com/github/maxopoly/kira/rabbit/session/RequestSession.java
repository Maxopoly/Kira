package com.github.maxopoly.kira.rabbit.session;

import org.json.JSONObject;

import com.github.maxopoly.kira.command.model.top.InputSupplier;

public abstract class RequestSession {

	private String sendingKey;
	protected final InputSupplier requester;

	public RequestSession(String sendingKey, InputSupplier requester) {
		this.sendingKey = sendingKey;
		this.requester = requester;
	}

	public abstract JSONObject getRequest();

	public InputSupplier getRequester() {
		return requester;
	}
	
	public String getSendingKey() {
		return sendingKey;
	}

	public abstract void handleReply(JSONObject json);
}
