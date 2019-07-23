package com.github.maxopoly.kira.api.token;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.maxopoly.kira.user.KiraUser;

public class APITokenManager {

	private static final String TOKEN_FILE_PATH = "tokens.json";
	private static final String TOKEN_FIELD_KEY = "tokens";

	private Map<String, APIToken> tokens;
	private Logger logger;

	public APITokenManager(Logger logger) {
		this.tokens = new ConcurrentHashMap<>();
		this.logger = logger;
		loadTokens();
	}

	/**
	 * Retrieves a token based on its secret
	 * 
	 * @param token Secret of the token, case sensitive
	 * @return Token with the given secret or null if no such token exists
	 */
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

	/**
	 * Gets all available tokens tied to the given user
	 * @param user User to get tokens for
	 * @return List containing all tokens for the given user, may be empty
	 */
	public List<APIToken> getTokensForUser(KiraUser user) {
		List<APIToken> result = new LinkedList<>();
		for(APIToken token : tokens.values()) {
			if(token.getUser().getID() == user.getID()) {
				result.add(token);
			}
		}
		return result;
	}

	private void loadTokens() {
		File file = new File(TOKEN_FILE_PATH);
		if (!file.exists()) {
			return;
		}
		JSONObject tokenJson;
		try {
			StringBuilder sb = new StringBuilder();
			Files.readAllLines(file.toPath()).forEach(line -> sb.append(line));
			tokenJson = new JSONObject(sb.toString());
		} catch (IOException | JSONException e) {
			logger.error("Failed to load token file", e);
			return;
		}
		JSONArray tokenArray = tokenJson.optJSONArray(TOKEN_FIELD_KEY);
		if (tokenArray == null) {
			return;
		}
		for (int i = 0; i < tokenArray.length(); i++) {
			JSONObject tokenSerial = (JSONObject) tokenArray.get(i);
			APIToken token = APIToken.fromJSON(tokenSerial);
			registerToken(token);
		}
	}
	
	/**
	 * Adds a new token to the cache
	 * 
	 * @param token Token to add
	 */
	public void registerToken(APIToken token) {
		this.tokens.put(token.getSecret(), token);
	}

	/**
	 * Removes a token from the cache
	 * 
	 * @param token Token to remove
	 */
	public void removeToken(APIToken token) {
		this.tokens.remove(token.getSecret());
	}

	/**
	 * Saves all tokens to a flat file from which they can be loaded
	 */
	public void saveTokens() {
		JSONObject json = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		for (APIToken token : tokens.values()) {
			jsonArray.put(token.toJson());
		}
		json.put(TOKEN_FIELD_KEY, jsonArray);
		try (BufferedWriter out = new BufferedWriter(new FileWriter(TOKEN_FILE_PATH))) {
		    out.write(json.toString());
		} catch (IOException e) {
			logger.error("Failed to save token file", e);
		}
	}

}
