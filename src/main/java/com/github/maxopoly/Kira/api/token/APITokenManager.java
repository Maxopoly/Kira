package com.github.maxopoly.Kira.api.token;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class APITokenManager {
	
	private Map<String, APIToken> tokens;
	
	public APITokenManager() {
		this.tokens = new ConcurrentHashMap<>();
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
	
	
	public void removeToken(APIToken token) {
		this.tokens.remove(token.getToken());
	}
	

}
