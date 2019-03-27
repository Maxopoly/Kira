package com.github.maxopoly.Kira.api.token;

import java.util.Map;
import java.util.TreeMap;

public class APITokenManager {
	
	private Map<String, APIToken> tokens;
	
	public APITokenManager() {
		//change to hashmap if we ever have tons of users
		this.tokens = new TreeMap<String, APIToken>();
	}
	
	public void registerToken(APIToken token) {
		this.tokens.put(token.getToken(), token);
	}
	
	public APIToken getToken(String token) {
		APIToken apiToken = tokens.get(token);
		if (apiToken == null) {
			return null;
		}
		if (apiToken.isOutdated()) {
			return null;
		}
		return apiToken;
	}

}
