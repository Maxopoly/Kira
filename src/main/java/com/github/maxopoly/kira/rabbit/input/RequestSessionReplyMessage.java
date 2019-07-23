package com.github.maxopoly.kira.rabbit.input;

import org.json.JSONObject;

import com.github.maxopoly.kira.KiraMain;
import com.github.maxopoly.kira.rabbit.RabbitInputSupplier;

public class RequestSessionReplyMessage extends RabbitMessage {

	public RequestSessionReplyMessage() {
		super("requestsession");
	}

	@Override
	public void handle(JSONObject json, RabbitInputSupplier supplier) {
		KiraMain.getInstance().getRequestSessionManager().handleReply(json);
	}
}
