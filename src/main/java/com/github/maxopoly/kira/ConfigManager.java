package com.github.maxopoly.kira;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.maxopoly.kira.database.DBConnection;
import com.github.maxopoly.kira.util.ParsingUtils;
import com.rabbitmq.client.ConnectionFactory;

public class ConfigManager {

	private static final String configFileName = "config.json";
	private JSONObject config;
	private Logger logger;

	public ConfigManager(Logger logger) {
		this.logger = logger;
	}

	public String getAPIInetAdress() {
		return config.getJSONObject("api").getString("address");
	}

	public int getAPIPort() {
		return config.getJSONObject("api").getInt("port");
	}

	public long getAPIRate() {
		return ParsingUtils.parseTime(config.optString("apirate", "500ms"));
	}

	public String getAPISSLCertPassword() {
		try {
			return config.getJSONObject("api").getString("sslCertPassword");
		} catch (JSONException e) {
			logger.error("No SSL cert password in config");
			return null;
		}
	}

	public String getAPISSLCertPath() {
		try {
			return config.getJSONObject("api").getString("sslCertPath");
		} catch (JSONException e) {
			logger.error("No SSL cert given in config");
			return null;
		}
	}

	public long getAuthroleID() {
		try {
			return config.getJSONObject("bot").getLong("authroleid");
		} catch (JSONException e) {
			logger.error("Failed to parse auth role id", e);
			return -1L;
		}
	}

	public String getBotToken() {
		try {
			return config.getJSONObject("bot").getString("token");
		} catch (JSONException e) {
			logger.error("Failed to parse bot token", e);
			return null;
		}
	}

	public String getCommandPrefix() {
		try {
			return config.getString("commandprefix");
		} catch (JSONException e) {
			return "!kira ";
		}
	}
	
	
	public Map<String, Long> getConsoleForwardingMapping() {
		JSONObject json = config.getJSONObject("consoleforward");
		Map <String, Long> result = new TreeMap<>();
		for(String key : json.keySet()) {
			long value = json.getLong(key);
			result.put(key, value);
		}
		return result;
	}
	
	public DBConnection getDatabase() {
		try {
			JSONObject json = config.getJSONObject("db");
			String user = json.getString("user");
			String password = json.optString("password", null);
			String host = json.getString("host");
			int port = json.getInt("port");
			String database = json.getString("database");
			return new DBConnection(logger, user, password, host, port, database, 5, 10000, 600000, 1800000);
		} catch (JSONException e) {
			logger.error("Failed to parse db credentials", e);
			return null;
		}
	}
	
	public String getIncomingQueueName() {
		try {
			JSONObject json = config.getJSONObject("rabbitmq");
			return json.getString("incomingQueue");
		} catch (JSONException e) {
			logger.error("Failed to parse rabbit queue", e);
			return null;
		}
	}
	
	public String getOutgoingQueueName() {
		try {
			JSONObject json = config.getJSONObject("rabbitmq");
			return json.getString("outgoingQueue");
		} catch (JSONException e) {
			logger.error("Failed to parse rabbit queue", e);
			return null;
		}
	}
	
	public ConnectionFactory getRabbitConfig() {
		try {
			JSONObject json = config.getJSONObject("rabbitmq");
			ConnectionFactory connFac = new ConnectionFactory();
			String user = json.optString("user", null);
			if (user != null) {
				connFac.setUsername(user);
			}
			String password = json.optString("password", null);
			if (password != null) {
				connFac.setPassword(password);
			}
			String host = json.optString("host", null);
			if (host != null) {
				connFac.setHost(host);
			}
			int port = json.optInt("port", -1);
			if (port != -1) {
				connFac.setPort(port);
			}
			return connFac;
		} catch (JSONException e) {
			logger.error("Failed to parse rabbit credentials", e);
			return null;
		}
	}
	
	public long getRelaySectionID() {
		try {
			return config.getLong("relayCategory");
		} catch (JSONException e) {
			logger.error("No relay category set", e);
			return -1L;
		}
	}
	
	public long getServerID() {
		try {
			return config.getJSONObject("bot").getLong("serverid");
		} catch (JSONException e) {
			logger.error("Failed to parse server id", e);
			return -1L;
		}
	}

	public boolean reload() {
		final StringBuilder sb = new StringBuilder();
		try {
			Files.readAllLines(new File(configFileName).toPath()).forEach(line -> sb.append(line));
			config = new JSONObject(sb.toString());
			return true;
		} catch (IOException | JSONException e) {
			logger.error("Failed to load config file", e);
			return false;
		}
	}

}
