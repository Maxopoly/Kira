package com.github.maxopoly.kira.api.sessions;

import org.json.JSONObject;

import com.github.maxopoly.kira.api.input.APISupplier;
import com.github.maxopoly.kira.rabbit.session.SendIngameCommandSession;

public class APIIngameCommandSession extends SendIngameCommandSession {
	
	private APISupplier supplier;
	
	public APIIngameCommandSession(APISupplier supplier, String command) {
		super(supplier, command);
	}

	@Override
	public void handleReply(JSONObject json) {
		String reply = json.getString(replyField);
		if (reply.trim().length() == 0) {
			reply = "Server sent empty reply";
		}
		supplier.getSession().sendInGameCommandResponse(command, reply);
	}
}
