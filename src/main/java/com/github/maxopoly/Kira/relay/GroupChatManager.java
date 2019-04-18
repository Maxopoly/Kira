package com.github.maxopoly.Kira.relay;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.apache.logging.log4j.Logger;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.database.DAO;
import com.github.maxopoly.Kira.permission.KiraRole;
import com.github.maxopoly.Kira.permission.KiraRoleManager;
import com.github.maxopoly.Kira.user.KiraUser;
import com.github.maxopoly.Kira.user.UserManager;

import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PermissionOverride;
import net.dv8tion.jda.core.entities.TextChannel;

public class GroupChatManager {

	private static final String accessPermSuffix = "_ACCESS";
	private static final long channelPerms = 379968L;

	public static float getChatCountLimit() {
		return 4.0f;
	}

	public static String getNameLayerManageChannelPermission() {
		return "KIRA_MANAGE_CHANNEL";
	}

	private Map<String, GroupChat> groupChatByName;
	private Map<Long, Set<GroupChat>> chatsByChannelId;
	private Map<Integer, Float> ownedChatsByUserId;
	private DAO dao;
	private long sectionID;

	private Logger logger;

	private RelayConfigManager relayConfigManager;

	public GroupChatManager(DAO dao, Logger logger, long sectionID, RelayConfigManager relayConfigManager) {
		groupChatByName = new ConcurrentHashMap<String, GroupChat>();
		chatsByChannelId = new TreeMap<>();
		ownedChatsByUserId = new TreeMap<>();
		this.dao = dao;
		this.relayConfigManager = relayConfigManager;
		this.logger = logger;
		this.sectionID = sectionID;
		for (GroupChat chat : dao.loadGroupChats(relayConfigManager)) {
			putGroupChat(chat);
		}
		logger.info("Loaded " + groupChatByName.size() + " group chats from database");
	}

	public void addMember(GroupChat chat, KiraUser user) {
		KiraRoleManager roleMan = KiraMain.getInstance().getKiraRoleManager();
		if (!roleMan.getRoles(user).contains(chat.getTiedRole())) {
			logger.info("Giving tied role for chat " + chat.getName() + " to " + user.toString());
			KiraMain.getInstance().getKiraRoleManager().giveRoleToUser(user, chat.getTiedRole());
		}
		Guild guild = KiraMain.getInstance().getGuild();
		if (guild.getIdLong() == chat.getGuildId()) {
			Channel channel = KiraMain.getInstance().getJDA().getTextChannelById(chat.getDiscordChannelId());
			Member member = guild.getMemberById(user.getDiscordID());
			if (channel == null) {
				logger.error(
						"Could not update member perm on channel for group " + chat.getName() + ", it didnt exist");
				return;
			}
			if (member != null) {
				PermissionOverride perm = channel.getPermissionOverride(member);
				if (perm == null) {
					perm = channel.createPermissionOverride(member).complete();
				}
				if (perm.getAllowedRaw() != channelPerms) {
					logger.info("Adjusting channel perms to " + chat.getName() + " for " + user.toString());
					perm.getManager().grant(channelPerms).queue();
				}
			}
		}
	}

	public void applyToAll(Consumer<GroupChat> function) {
		for (GroupChat chat : groupChatByName.values()) {
			function.accept(chat);
		}
	}

	public GroupChat createGroupChat(String name, KiraUser creator) {
		Guild guild = KiraMain.getInstance().getGuild();
		Category cat = guild.getCategoryById(sectionID);
		if (cat == null) {
			logger.warn("Tried to create channel, but category for it could not be found");
			return null;
		}
		Channel channel = cat.createTextChannel(name).complete();
		if (channel == null) {
			logger.warn("Tried to create channel, but it didn't work");
			return null;
		}
		return createGroupChat(name, guild.getIdLong(), channel.getIdLong(), creator);
	}

	public GroupChat createGroupChat(String name, long guildID, long channelID, KiraUser creator) {
		KiraRole role = KiraMain.getInstance().getKiraRoleManager().getOrCreateRole(name + accessPermSuffix);
		int id = dao.createGroupChat(guildID, channelID, name, role, creator.getID(),
				relayConfigManager.getDefaultConfig());
		if (id == -1) {
			return null;
		}
		GroupChat chat = new GroupChat(id, name, channelID, guildID, role, creator,
				relayConfigManager.getDefaultConfig());
		putGroupChat(chat);
		logger.info("Successfully created group chat for group " + chat.toString());
		return chat;
	}

	public void deleteGroupChat(GroupChat chat) {
		TextChannel channel = KiraMain.getInstance().getJDA().getTextChannelById(chat.getDiscordChannelId());
		boolean isManaged;
		if (channel == null) {
			// already deleted
			isManaged = false;
		} else {
			isManaged = channel.getGuild().getIdLong() == KiraMain.getInstance().getGuild().getIdLong();
		}
		logger.info("Deleting channel for " + chat.getName());
		if (isManaged) {
			channel.delete().queue();
		}
		Float count = ownedChatsByUserId.get(chat.getCreator().getID());
		if (count != null) {
			ownedChatsByUserId.put(chat.getCreator().getID(), Math.max(0, count - 1));
		}
		groupChatByName.remove(chat.getName().toLowerCase());
		Set<GroupChat> channels = chatsByChannelId.get(chat.getDiscordChannelId());
		if (channels != null) {
			channels.remove(chat);
		}
		// db clean up is done by deleting the chat via foreign keys
		KiraMain.getInstance().getKiraRoleManager().deleteRole(chat.getTiedRole(), false);
		dao.deleteGroupChat(chat);
		if (!isManaged && channel != null) {
			channel.sendMessage("Relay " + chat.getName()
					+ " which was previously linked to this channel is being deleted as requested by a user. It will no longer broadcast anything");
		}
	}

	public Set<GroupChat> getChatByChannelID(long id) {
		Set<GroupChat> existing = chatsByChannelId.get(id);
		if (existing == null) {
			return new TreeSet<>();
		}
		return existing;
	}

	public GroupChat getGroupChat(String name) {
		return groupChatByName.get(name.toLowerCase());
	}

	public float getOwnedChatCount(KiraUser user) {
		Float count = ownedChatsByUserId.get(user.getID());
		if (count == null) {
			count = 0.0f;
		}
		return count;
	}

	public void putGroupChat(GroupChat chat) {
		groupChatByName.put(chat.getName().toLowerCase(), chat);
		Set<GroupChat> existing = chatsByChannelId.get(chat.getDiscordChannelId());
		if (existing == null) {
			existing = new HashSet<>();
			chatsByChannelId.put(chat.getDiscordChannelId(), existing);
		}
		existing.add(chat);
		Float count = ownedChatsByUserId.get(chat.getCreator().getID());
		if (count == null) {
			count = 0.0f;
		}
		ownedChatsByUserId.put(chat.getCreator().getID(), count + chat.getWeight());
	}

	public void removeMember(GroupChat chat, KiraUser user) {
		KiraMain.getInstance().getKiraRoleManager().takeRoleFromUser(user, chat.getTiedRole());
		logger.info("Taking tied role for chat " + chat.getName() + " from " + user.toString());
		// TODO
	}

	public void setConfig(GroupChat chat, RelayConfig config) {
		dao.setRelayConfigForChat(chat, config);
		chat.setConfig(config);
	}

	public void syncAccess(GroupChat chat, Set<Integer> intendedMembers) {
		UserManager userMan = KiraMain.getInstance().getUserManager();
		Set<Integer> currentMembers = dao.getGroupChatMembers(chat);
		// remove all members that shouldnt be there
		currentMembers.stream().filter(i -> !intendedMembers.contains(i))
				.forEach(i -> removeMember(chat, userMan.getUser(i)));
		// add all that are missing
		intendedMembers.stream().forEach(i -> addMember(chat, userMan.getUser(i)));
	}

}
