package com.github.maxopoly.Kira.api.token;

import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.github.maxopoly.Kira.api.APISession;
import com.github.maxopoly.Kira.api.KiraAPIConnection;
import com.github.maxopoly.Kira.user.KiraUser;

public class APIToken {

	private static final String validCharacters = "ABCDEFGHIJKLMOPGRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final SecureRandom rng = new SecureRandom();
	private static final long validTime = TimeUnit.MINUTES.toMillis(5);

	private String token;
	private KiraUser user;
	private List<String> snitchGroups;
	private List<String> chatGroups;
	private boolean skyNet;
	// if a session is created using this token, when should that session be
	// automatically ended
	private long expirationTime;
	// tokens expire if not used within 5 minutes
	private long creationTime;

	private APIToken(String token, KiraUser user, List<String> snitchGroups, List<String> chatGroups, boolean skyNet,
			long expirationTime) {
		this.token = token;
		this.user = user;
		this.snitchGroups = snitchGroups;
		this.chatGroups = chatGroups;
		this.skyNet = skyNet;
		this.creationTime = System.currentTimeMillis();
		this.expirationTime = expirationTime;
	}

	public String getToken() {
		return token;
	}

	public boolean isOutdated() {
		return (System.currentTimeMillis() - creationTime) > validTime;
	}
	
	public APISession generateSession(KiraAPIConnection connection) {
		return new APISession(user, snitchGroups, chatGroups, skyNet, expirationTime, connection);
	}

	public static APIToken generate(KiraUser user, List<String> snitchGroups, List<String> chatGroups, boolean skyNet,
			long expirationTime) {
		return new APIToken(generateCode(64), user, snitchGroups, chatGroups, skyNet, expirationTime);
	}

	private static String generateCode(int length) {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int index = rng.nextInt(validCharacters.length());
			sb.append(validCharacters.charAt(index));
		}
		return sb.toString();

	}

}
