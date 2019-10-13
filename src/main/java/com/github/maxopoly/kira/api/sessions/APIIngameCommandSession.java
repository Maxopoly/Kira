package com.github.maxopoly.kira.api.sessions;

import org.json.JSONObject;

import com.github.maxopoly.kira.api.input.APISupplier;
import com.github.maxopoly.kira.rabbit.session.SendIngameCommandSession;

public class APIIngameCommandSession extends SendIngameCommandSession {
	
	private APISupplier supplier;
	
	private String identifer;
	
	public APIIngameCommandSession(APISupplier supplier, String command, String id) {
		super(supplier, command);
		this.supplier = supplier;
	}

	@Override
	public void handleReply(JSONObject json) {
		String reply = json.getString(REPLY_FIELD);
		if (reply.trim().length() == 0) {
			reply = "";
		}
		supplier.getSession().sendInGameCommandResponse(identifer, reply);
	}
}
