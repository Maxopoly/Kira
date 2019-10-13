package com.github.maxopoly.kira.rabbit.session;

import org.json.JSONObject;

import com.github.maxopoly.kira.command.model.top.InputSupplier;

public class SendIngameCommandSession extends RequestSession {
	
	protected static final String REPLY_FIELD = "reply";
	
	protected String command;
	
	public SendIngameCommandSession(InputSupplier supplier, String command) {
		super("ingame", supplier);
		this.command = command;
	}

	@Override
	public JSONObject getRequest() {
		JSONObject json = new JSONObject();
		json.put("uuid", requester.getUser().getIngameUUID().toString());
		json.put("command", command);
		return json;
	}

	@Override
	public void handleReply(JSONObject json) {
		String reply = json.getString(REPLY_FIELD);
		if (reply.trim().length() == 0) {
			reply = "Server sent empty reply";
		}
		requester.reportBack(reply);
	}

}
