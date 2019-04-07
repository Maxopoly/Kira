package com.github.maxopoly.Kira.user;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthManager {

	private Map<String, UUID> pendingAuths;
	private Map<UUID, String> playerNames;

	public AuthManager() {
		pendingAuths = new HashMap<String, UUID>();
		playerNames = new HashMap<>();
	}

	public String getName(UUID uuid) {
		return playerNames.get(uuid);
	}

	public UUID getUserForCode(String code) {
		return pendingAuths.get(code.toLowerCase());
	}

	public void putCode(UUID user, String name, String auth) {
		pendingAuths.put(auth.toLowerCase(), user);
		playerNames.put(user, name);
	}

	public void removeCode(String code) {
		UUID uuid = pendingAuths.remove(code.toLowerCase());
		if (uuid != null) {
			playerNames.remove(uuid);
		}
	}

}
