package com.github.maxopoly.kira.relay;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.github.maxopoly.kira.database.DAO;
import com.github.maxopoly.kira.user.KiraUser;

public class RelayConfigManager {

	private Map<Integer, RelayConfig> configsById;
	private Map<String, RelayConfig> configsByName;
	private DAO dao;
	private RelayConfig defaultConfig;

	public RelayConfigManager(DAO dao) {
		this.dao = dao;
		this.configsById = new TreeMap<>();
		this.configsByName = new HashMap<>();
		int minIndex = Integer.MAX_VALUE;
		for (RelayConfig config : dao.loadRelayConfigs()) {
			addRelayConfig(config);
			minIndex = Math.min(config.getID(), minIndex);
		}
		if (minIndex == Integer.MAX_VALUE) {
			createDefaultConfig();
		} else {
			defaultConfig = getById(minIndex);
		}
	}

	public void addRelayConfig(RelayConfig config) {
		configsById.put(config.getID(), config);
		configsByName.put(config.getName().toLowerCase(), config);
	}

	private void createDefaultConfig() {
		RelayConfig config = createRelayConfig("default", null);
		defaultConfig = config;
	}

	public RelayConfig createRelayConfig(String name, KiraUser creator) {
		RelayConfig config = dao.createRelayConfig(name, true, true, true, true,
				"`[%TIME%]` `[%GROUP%]` **[%PLAYER%]** %MESSAGE% %PING%",
				"`[%TIME%]` `[%GROUP%]` **%PLAYER%** %ACTION% at %SNITCH% (%X%,%Y%,%Z%) %PING%", "logged in", "logged out",
				"is", "@here", "@everyone", false, "HH:mm:ss", "`[%TIME%]` **%PLAYER%** %ACTION%", "logged in",
				"logged out", false, "`[%TIME%]` **%PLAYER%** is brand new!", false, creator);
		if (config == null) {
			return null;
		}
		addRelayConfig(config);
		return config;
	}

	public RelayConfig getById(int id) {
		return configsById.get(id);
	}

	public RelayConfig getByName(String name) {
		return configsByName.get(name.toLowerCase());
	}

	public RelayConfig getDefaultConfig() {
		return defaultConfig;
	}

}
