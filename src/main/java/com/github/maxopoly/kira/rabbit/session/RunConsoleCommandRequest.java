package com.github.maxopoly.kira.rabbit.session;

import java.util.UUID;

import org.json.JSONObject;

import com.github.maxopoly.kira.command.model.top.InputSupplier;

public class RunConsoleCommandRequest extends RequestSession {
	
	private String msg;
	private UUID sender;
	private InputSupplier supplier;

	public RunConsoleCommandRequest(String msg, UUID sender, InputSupplier supplier) {
		super("consolemessageop", supplier);
		this.msg = msg;
		this.sender = sender;
		this.supplier = supplier;
	}

	@Override
	public JSONObject getRequest() {
		JSONObject json = new JSONObject();
		json.put("command", msg);
		json.put("sender", sender.toString());
		return json;
	}

	@Override
	public void handleReply(JSONObject json) {
		String msg = json.getString("replymsg");
		supplier.reportBack(msg);
	}

}
