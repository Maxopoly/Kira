package com.github.maxopoly.Kira.rabbit.input;

import org.json.JSONObject;

import com.github.maxopoly.Kira.KiraMain;

public class RequestSessionReplyMessage extends RabbitMessage {

	public RequestSessionReplyMessage() {
		super("requestsession");
	}

	@Override
	public void handle(JSONObject json) {
		KiraMain.getInstance().getRequestSessionManager().handleReply(json);
	}
}
