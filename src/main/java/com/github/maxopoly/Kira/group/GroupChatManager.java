package com.github.maxopoly.Kira.group;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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

public class GroupChatManager {

	private static final String accessPermSuffix = "_ACCESS";

	private Map<String, GroupChat> groupChatByName;
	private Map<Long, GroupChat> chatsByChannelId;
	private Map<Integer, Float> ownedChatsByUserId;
	private DAO dao;
	private long sectionID;
	private Logger logger;

	public GroupChatManager(DAO dao, Logger logger, long sectionID) {
		groupChatByName = new HashMap<String, GroupChat>();
		chatsByChannelId = new TreeMap<Long, GroupChat>();
		ownedChatsByUserId = new TreeMap<>();
		this.dao = dao;
		this.logger = logger;
		this.sectionID = sectionID;
		for (GroupChat chat : dao.loadGroupChats()) {
			putGroupChat(chat);
		}
		logger.info("Loaded " + groupChatByName.size() + " group chats from database");
	}

	public GroupChat createGroupChat(String name, long guildID, long channelID, KiraUser creator) {
		KiraRole role = KiraMain.getInstance().getKiraRoleManager().getOrCreateRole(name + accessPermSuffix);
		int id = dao.createGroupChat(guildID, channelID, name, role, creator.getID());
		if (id == -1) {
			return null;
		}
		GroupChat chat = new GroupChat(id, name, channelID, guildID, role, creator);
		putGroupChat(chat);
		logger.info("Successfully created group chat for group " + chat.toString());
		return chat;
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
	
	public void deleteGroupChat(GroupChat chat) {
		Channel channel = KiraMain.getInstance().getJDA().getTextChannelById(chat.getDiscordChannelId());
		logger.info("Deleting channel for " + channel.getName());
		if (channel.getGuild().getIdLong() == KiraMain.getInstance().getGuild().getIdLong()) {
			channel.delete().queue();
		}
		Float count = ownedChatsByUserId.get(chat.getCreator().getID());
		if (count != null) {
			ownedChatsByUserId.put(chat.getCreator().getID(), Math.max(0, count -1));
		}
		groupChatByName.remove(chat.getName());
		chatsByChannelId.remove(chat.getDiscordChannelId());
		//db clean up is done by deleting the chat via foreign keys
		KiraMain.getInstance().getKiraRoleManager().deleteRole(chat.getTiedRole(), false);
		dao.deleteGroupChat(chat);
	}

	public void syncAccess(GroupChat chat, Set<Integer> intendedMembers) {
		DAO dao = KiraMain.getInstance().getDAO();
		UserManager userMan = KiraMain.getInstance().getUserManager();
		Set<Integer> currentMembers = dao.getGroupChatMembers(chat);
		// remove all members that shouldnt be there
		currentMembers.stream().filter(i -> !intendedMembers.contains(i))
				.forEach(i -> removeMember(chat, userMan.getUser(i)));
		// add all that are missing
		intendedMembers.stream().filter(i -> !currentMembers.contains(i))
				.forEach(i -> addMember(chat, userMan.getUser(i)));
	}

	public GroupChat getGroupChat(String name) {
		return groupChatByName.get(name);
	}
	
	public GroupChat getChatByChannelID(long id) {
		return chatsByChannelId.get(id);
	}

	public void putGroupChat(GroupChat chat) {
		groupChatByName.put(chat.getName(), chat);
		chatsByChannelId.put(chat.getDiscordChannelId(), chat);
		Float count = ownedChatsByUserId.get(chat.getCreator().getID());
		if (count == null) {
			count = 0.0f;
		}
		ownedChatsByUserId.put(chat.getCreator().getID(), count + chat.getWeight());
	}
	
	public float getOwnedChatCount(KiraUser user) {
		Float count = ownedChatsByUserId.get(user.getID());
		if (count == null) {
			count = 0.0f;
		}
		return count;
	}

	public void addMember(GroupChat chat, KiraUser user) {
		KiraRoleManager roleMan = KiraMain.getInstance().getKiraRoleManager();
		if (roleMan.getRoles(user).contains(chat.getTiedRole())) {
			return;
		}
		logger.info("Giving tied role for chat " + chat.getName() + " to " + user.toString());
		KiraMain.getInstance().getKiraRoleManager().giveRoleToUser(user, chat.getTiedRole());
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
				PermissionOverride perm = channel.createPermissionOverride(member).complete();
				perm.getManager().grant(379968L).queue();
			}
		}
	}

	public void removeMember(GroupChat chat, KiraUser user) {
		KiraMain.getInstance().getKiraRoleManager().takeRoleFromUser(user, chat.getTiedRole());
		logger.info("Taking tied role for chat " + chat.getName() + " from " + user.toString());
		//TODO
	}
	
	public static float getChatCountLimit() {
		return 4.0f;
	}

}
