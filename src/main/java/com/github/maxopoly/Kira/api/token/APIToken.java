package com.github.maxopoly.Kira.api.token;

import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.java_websocket.WebSocket;
import org.json.JSONObject;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.api.APISession;
import com.github.maxopoly.Kira.user.KiraUser;

public class APIToken {

	private static final String VALID_CHARACTERS = "ABCDEFGHIJKLMOPGRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final SecureRandom rng = new SecureRandom();

	public static APIToken generate(KiraUser user, List<String> snitchGroups, List<String> chatGroups, boolean skyNet,
			long expirationTime, long validTime) {
		return new APIToken(generateCode(64), user, snitchGroups, chatGroups, skyNet, expirationTime, validTime);
	}

	public static APIToken generate(APISession session) {
		return generate(session.getUser(), session.getSnitchGroups(), session.getChatGroups(), session.receivesSkynet(),
				-1L, TimeUnit.HOURS.toMillis(3));
	}
	/*
	public static APIToken fromJSON(JSONObject json) {
		String token = json.getString("token");
		KiraUser user = KiraMain.getInstance().getUserManager().getUser(userID)
		return new APIToken(json.getString("token"), user, snitchGroups, chatGroups, skyNet, expirationTime, validTimeFrame)
		
		
		JSONObject json = new JSONObject();
		json.put("token", token);
		json.put("user", user.getID());
		json.put("snitch", snitchGroups);
		json.put("chat", chatGroups);
		json.put("skynet", skyNet);
		return json;
		return generate(session.getUser(), session.getSnitchGroups(), session.getChatGroups(), session.receivesSkynet(),
				-1L, TimeUnit.HOURS.toMillis(3));
	} */

	private static String generateCode(int length) {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int index = rng.nextInt(VALID_CHARACTERS.length());
			sb.append(VALID_CHARACTERS.charAt(index));
		}
		return sb.toString();

	}

	private String token;
	private KiraUser user;
	private List<String> snitchGroups;
	private List<String> chatGroups;
	private boolean skyNet;
	private long validTimeFrame;

	// if a session is created using this token, when should that session be
	// automatically ended
	private long expirationTime;

	// tokens expire if not used within 5 minutes
	private long creationTime;

	private APIToken(String token, KiraUser user, List<String> snitchGroups, List<String> chatGroups, boolean skyNet,
			long expirationTime, long validTimeFrame) {
		this.token = token;
		this.user = user;
		this.snitchGroups = snitchGroups;
		this.chatGroups = chatGroups;
		this.skyNet = skyNet;
		this.creationTime = System.currentTimeMillis();
		this.expirationTime = expirationTime;
		this.validTimeFrame = validTimeFrame;
	}

	public APISession generateSession(WebSocket connection) {
		return new APISession(user, snitchGroups, chatGroups, skyNet, expirationTime, connection);
	}

	public String getToken() {
		return token;
	}

	public boolean isOutdated() {
		return (System.currentTimeMillis() - creationTime) > validTimeFrame;
	}
	
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.put("token", token);
		json.put("user", user.getID());
		json.put("snitch", snitchGroups);
		json.put("chat", chatGroups);
		json.put("skynet", skyNet);
		return json;
	}

}
