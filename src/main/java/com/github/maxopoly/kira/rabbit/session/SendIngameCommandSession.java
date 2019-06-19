package com.github.maxopoly.kira.rabbit.session;

import org.json.JSONObject;

import com.github.maxopoly.kira.command.model.top.InputSupplier;

public class SendIngameCommandSession extends RequestSession {
	
	private InputSupplier supplier;
	private String command;
	
	public SendIngameCommandSession(InputSupplier supplier, String command) {
		super("ingame", supplier);
		this.supplier = supplier;
		this.command = command;
	}

	@Override
	public JSONObject getRequest() {
		JSONObject json = new JSONObject();
		json.put("uuid", supplier.getUser().getIngameUUID().toString());
		json.put("command", command);
		return json;
	}

	@Override
	public void handleReply(JSONObject json) {
		String reply = json.getString("reply");
		if (reply.trim().length() == 0) {
			reply = "Server sent empty reply";
		}
		supplier.reportBack(reply);
	}

}
