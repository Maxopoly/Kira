package com.github.maxopoly.Kira.api.input.impl;

import org.json.JSONObject;

import com.github.maxopoly.Kira.api.input.APIInput;
import com.github.maxopoly.Kira.api.input.APISupplier;
import com.github.maxopoly.Kira.user.KiraUser;

public class AuthAPIInput extends APIInput {
	
	private static final int API_VERSION = 1;

	public AuthAPIInput() {
		super("auth");
	}

	@Override
	public void handle(JSONObject json, APISupplier supplier) {
		String token = json.optString("apiToken");
		if (token == null) {
			logger.info("Closing connection with " + supplier.getIdentifier() + ", because no api token was given");
			supplier.getConnection().close();
			return;
		}
		String appId = json.optString("applicationId");
		if (appId == null) {
			logger.info("Closing connection with " + supplier.getIdentifier() + ", because no application id was given");
			supplier.getConnection().close();
			return;
		}
		int version = json.optInt("apiVersion");
		if (version != API_VERSION) {
			logger.info("Closing connection with " + supplier.getIdentifier() + ", because they are using an outdated api version");
			supplier.getConnection().close();
			return;
		}
		
		
	}

}
