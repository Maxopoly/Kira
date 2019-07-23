package com.github.maxopoly.kira.api.input.packets;

import org.json.JSONObject;

import com.github.maxopoly.kira.api.APISession;
import com.github.maxopoly.kira.api.input.APIInput;
import com.github.maxopoly.kira.api.input.APISupplier;
import com.github.maxopoly.kira.api.token.APIToken;

public class RequestReplacementToken extends APIInput {

	public RequestReplacementToken() {
		super("new-token");
	}

	@Override
	public void handle(JSONObject argument, APISupplier supplier) {
		APISession session = supplier.getSession();
		APIToken token = APIToken.generate(session);
		session.sendReplacementToken(token);
		session.close();
	}

}
