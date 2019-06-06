package com.github.maxopoly.Kira.rabbit.input;

import org.json.JSONObject;

import com.github.maxopoly.Kira.rabbit.RabbitInputSupplier;
import com.github.maxopoly.kira.KiraMain;

public class RequestSessionReplyMessage extends RabbitMessage {

	public RequestSessionReplyMessage() {
		super("requestsession");
	}

	@Override
	public void handle(JSONObject json, RabbitInputSupplier supplier) {
		KiraMain.getInstance().getRequestSessionManager().handleReply(json);
	}
}
