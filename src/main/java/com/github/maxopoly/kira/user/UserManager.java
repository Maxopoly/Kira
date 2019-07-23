package com.github.maxopoly.kira.user;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;

import com.github.maxopoly.kira.KiraMain;
import com.github.maxopoly.kira.permission.KiraRoleManager;

import net.dv8tion.jda.core.JDA;

public class UserManager {

	private Map<Long, KiraUser> userByDiscordID;
	private Map<Integer, KiraUser> userByID;
	private Map<UUID, KiraUser> userByUUID;
	private Map<String, KiraUser> userByName;
	private Logger logger;

	public UserManager(Logger logger) {
		userByDiscordID = new HashMap<>();
		this.logger = logger;
		userByID = new HashMap<>();
		userByUUID = new HashMap<>();
		userByName = new HashMap<>();
	}

	public void addUser(KiraUser user) {
		userByID.put(user.getID(), user);
		if (user.hasDiscord()) {
			userByDiscordID.put(user.getDiscordID(), user);
		}
		if (user.hasIngameAccount()) {
			userByUUID.put(user.getIngameUUID(), user);
			userByName.put(user.getName().toLowerCase(), user);
		}
	}

	public KiraUser createUser(long discordID) {
		logger.info("Creating db entry for user with discord id " + discordID);
		int userID = KiraMain.getInstance().getDAO().createUser(discordID);
		if (userID == -1) {
			logger.error("Failed to create user with discord id " + discordID);
			return null;
		}
		KiraUser user = new KiraUser(userID, null, discordID, null, null);
		addUser(user);
		KiraRoleManager roleMan = KiraMain.getInstance().getKiraRoleManager();
		roleMan.giveRoleToUser(user, roleMan.getDefaultRole());
		return user;
	}

	public Set<KiraUser> getAllUsers() {
		return new HashSet<>(userByID.values());
	}

	public KiraUser getOrCreateUserByDiscordID(long discordID) {
		KiraUser user = userByDiscordID.get(discordID);
		if (user == null) {
			user = createUser(discordID);
		}
		return user;
	}

	public KiraUser getUser(int userID) {
		return userByID.get(userID);
	}

	public KiraUser getUserByIngameUUID(UUID uuid) {
		return userByUUID.get(uuid);
	}

	private KiraUser parseDiscordUser(String input, StringBuilder feedback) {
		if (input.contains("#")) {
			// normal discord user name in the form of "Anon#1234"
			if (input.startsWith("@")) {
				input = input.substring(1);
			}
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
					KiraUser user = getOrCreateUserByDiscordID(discordUser.getIdLong());
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
			if (input.startsWith("<@") && input.endsWith(">")) {
				input = input.substring(2, input.length() - 1);
			}
			try {
				long id = Long.parseLong(input);
				KiraUser user = getOrCreateUserByDiscordID(id);
				if (user == null) {
					feedback.append(
							"Successfully parsed discord user id " + id + ", but could not find user with given id\n");
				} else {
					feedback.append("Found user: " + user.toString() + "\n");
				}
				return user;
			} catch (NumberFormatException e) {
				feedback.append("Tried to parse " + input
						+ " as discord account, but it was neither a user id, nor a user name\n");
				return null;
			}
		}
	}

	private KiraUser parseIngameUser(String input, StringBuilder feedback) {
		String lower = input.toLowerCase();
		String regex = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
		if (Pattern.matches(regex, lower)) {
			UUID uuid = UUID.fromString(lower);
			KiraUser user = userByUUID.get(uuid);
			if (user == null) {
				feedback.append("Parsed " + input + " as minecraft uuid, but it was not a known UUID\n");
			}
			return user;
		}
		KiraUser user = userByName.get(lower);
		if (user == null) {
			feedback.append("Tried to parse " + input + " as minecraft account, but no match was found\n");
		}
		return user;
	}

	private KiraUser parseRedditUser(String input, StringBuilder feedback) {
		feedback.append("Tried to parse " + input + " as reddit account, but this is not implemented yet\n");
		return null;
	}

	public KiraUser parseUser(String input, StringBuilder feedback) {
		String lower = input.toLowerCase().trim();
		if (lower.contains(":")) {
			String[] parts = input.split(":");
			if (parts.length != 2) {
				feedback.append("Could not parse input user, ':' found, but split length was " + input.length()
						+ " for input " + input + "\n");
				return null;
			}
			switch (parts[0].toLowerCase()) {
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
		return parseDiscordUser(input, feedback);
	}

}
