package com.github.maxopoly.Kira.user;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.Logger;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.permission.KiraRoleManager;

import net.dv8tion.jda.core.JDA;

public class UserManager {

	private Map<Long, User> userByDiscordID;
	private Map<Integer, User> userByID;
	private Map<UUID, User> userByUUID;
	private Logger logger;

	public UserManager(Logger logger) {
		userByDiscordID = new HashMap<>();
		this.logger = logger;
		userByID = new HashMap<>();
		userByUUID = new HashMap<>();
	}

	public User getUser(int userID) {
		return userByID.get(userID);
	}

	public User getOrCreateUserByDiscordID(long discordID) {
		KiraMain main = KiraMain.getInstance();
		User user = userByDiscordID.get(discordID);
		if (user == null) {
			logger.info("Creating db entry for user with discord id " + discordID);
			int userID = main.getDAO().createUser(discordID);
			if (userID == -1) {
				logger.error("Failed to create user with discord id " + discordID);
				return null;
			}
			main.getDAO().addUserToRole(user, main.getKiraRoleManager().getDefaultRole());
			user = new User(userID, null, discordID, null, null);
			addUser(user);
		}
		return user;
	}

	public boolean createUser(long discordID) {
		logger.info("Creating db entry for user with discord id " + discordID);
		int userID = KiraMain.getInstance().getDAO().createUser(discordID);
		if (userID == -1) {
			logger.error("Failed to create user with discord id " + discordID);
			return false;
		}
		User user = new User(userID, null, discordID, null, null);
		addUser(user);
		KiraRoleManager roleMan = KiraMain.getInstance().getKiraRoleManager();
		roleMan.addRole(user, roleMan.getDefaultRole());
		return true;
	}

	public User getUserByIngameUUID(UUID uuid) {
		return userByUUID.get(uuid);
	}

	public void addUser(User user) {
		userByID.put(user.getID(), user);
		if (user.hasDiscord()) {
			userByDiscordID.put(user.getDiscordID(), user);
		}
		if (user.hasIngameAccount()) {
			userByUUID.put(user.getIngameUUID(), user);
		}
	}

	public Set<User> getAllUsers() {
		return new HashSet<>(userByID.values());
	}

	public User parseUser(String input, StringBuilder feedback) {
		String lower = input.toLowerCase().trim();
		if (lower.contains(":")) {
			String[] parts = input.split(":");
			if (parts.length != 2) {
				feedback.append("Could not parse input user, ':' found, but split length was " + input.length()
						+ " for input " + input + "\n");
				return null;
			}
			switch (parts[0]) {
			case "discord":
				return parseDiscordUser(parts[1], feedback);
			case "reddit":
				return parseRedditUser(parts[1], feedback);
			case "hytale":
			case "minecraft":
			case "mc":
			case "ingame":
			case "game":
				return parseIngameUser(parts[1], feedback);
			}
		}
		if (lower.startsWith("@")) {
			// discord
			return parseDiscordUser(input, feedback);
		}
		return parseIngameUser(input, feedback);
	}

	private User parseDiscordUser(String input, StringBuilder feedback) {
		if (input.contains("#")) {
			// normal discord user name in the form of "Anon#1234"
			if (input.startsWith("@")) {
				input = input.substring(1);
			}
			String[] parts = input.split("#");
			if (parts.length != 2) {
				feedback.append("Could not partse discord user, invalid name format\n");
				return null;
			}
			JDA jda = KiraMain.getInstance().getJDA();
			List<net.dv8tion.jda.core.entities.User> users = jda.getUsersByName(parts[0], true);
			for (net.dv8tion.jda.core.entities.User discordUser : users) {
				if (discordUser.getDiscriminator().equals(parts[1])) {
					User user = getOrCreateUserByDiscordID(discordUser.getIdLong());
					if (user == null) {
						feedback.append("Successfully parsed discord user name and found id " + discordUser.getIdLong()
								+ ", but could not find user account tied to it\n");
					} else {
						feedback.append("Found user: " + user.toString() + "\n");
					}
					return user;
				}
			}
			feedback.append("No user with given name found\n");
			return null;
		} else {
			try {
				long id = Long.parseLong(input);
				User user = getOrCreateUserByDiscordID(id);
				if (user == null) {
					feedback.append(
							"Successfully parsed discord user id " + id + ", but could not find user with given id\n");
				} else {
					feedback.append("Found user: " + user.toString() + "\n");
				}
				return user;
			} catch (NumberFormatException e) {
				feedback.append("Tried to parse " + input
						+ " as discord account, but it was neither a user id, nor a user name");
				return null;
			}
		}
	}

	private User parseRedditUser(String input, StringBuilder feedback) {
		return null;
	}

	private User parseIngameUser(String input, StringBuilder feedback) {
		return null;
	}

}
