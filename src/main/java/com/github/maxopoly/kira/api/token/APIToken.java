package com.github.maxopoly.kira.api.token;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

import org.java_websocket.WebSocket;
import org.json.JSONArray;
import org.json.JSONObject;

import com.github.maxopoly.kira.KiraMain;
import com.github.maxopoly.kira.api.APISession;
import com.github.maxopoly.kira.user.KiraUser;

public class APIToken {

	private static final String VALID_CHARACTERS = "ABCDEFGHIJKLMOPGRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final SecureRandom rng = new SecureRandom();

	public static APIToken fromJSON(JSONObject json) {
		String token = json.getString("token");
		KiraUser user = KiraMain.getInstance().getUserManager().getUser(json.getInt("user"));
		List<String> snitchGroups = parseStringList(json.getJSONArray("snitch"));
		List<String> chatGroups = parseStringList(json.getJSONArray("chat"));
		boolean skyNet = json.getBoolean("skynet");
		long expirationTime = json.getLong("expirationTime");
		long creationTime = json.getLong("creationTime");
		return new APIToken(token, user, snitchGroups, chatGroups, skyNet, creationTime, expirationTime);
	}

	public static APIToken generate(APISession session) {
		return generate(session.getUser(), session.getSnitchGroups(), session.getChatGroups(), session.receivesSkynet(),
				session.getExpirationTime());
	}

	public static APIToken generate(KiraUser user, List<String> snitchGroups, List<String> chatGroups, boolean skyNet,
			long expirationTime) {
		return new APIToken(generateCode(64), user, snitchGroups, chatGroups, skyNet, System.currentTimeMillis(),
				expirationTime);
	}

	private static String generateCode(int length) {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int index = rng.nextInt(VALID_CHARACTERS.length());
			sb.append(VALID_CHARACTERS.charAt(index));
		}
		return sb.toString();

	}

	private static List<String> parseStringList(JSONArray json) {
		List<String> result = new LinkedList<>();
		for (int i = 0; i < json.length(); i++) {
			result.add((String) json.get(i));
		}
		return result;
	}

	private String token;
	private KiraUser user;
	private List<String> snitchGroups;
	private List<String> chatGroups;
	private boolean skyNet;

	private long expirationTime;
	private long creationTime;

	private APIToken(String token, KiraUser user, List<String> snitchGroups, List<String> chatGroups, boolean skyNet,
			long creationTime, long expirationTime) {
		this.token = token;
		this.user = user;
		this.snitchGroups = snitchGroups;
		this.chatGroups = chatGroups;
		this.skyNet = skyNet;
		this.creationTime = creationTime;
		this.expirationTime = expirationTime;
	}

	public APISession generateSession(WebSocket connection) {
		return new APISession(user, snitchGroups, chatGroups, skyNet, expirationTime, connection);
	}

	public long getExpirationTime() {
		return expirationTime;
	}

	public String getSecret() {
		return token;
	}
	
	public KiraUser getUser() {
		return user;
	}
	
	public boolean isOutdated() {
		return expirationTime != -1 && System.currentTimeMillis() > expirationTime;
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.put("token", token);
		json.put("user", user.getID());
		json.put("snitch", snitchGroups);
		json.put("chat", chatGroups);
		json.put("skynet", skyNet);
		json.put("expirationTime", expirationTime);
		json.put("creationTime", creationTime);
		return json;
	}

}
