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
import com.github.maxopoly.Kira.user.User;
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
	private DAO dao;
	private long sectionID;
	private Logger logger;

	public GroupChatManager(DAO dao, Logger logger, long sectionID) {
		groupChatByName = new HashMap<String, GroupChat>();
		chatsByChannelId = new TreeMap<Long, GroupChat>();
		this.dao = dao;
		this.logger = logger;
		this.sectionID = sectionID;
		for (GroupChat chat : dao.loadGroupChats()) {
			putGroupChat(chat);
		}
		logger.info("Loaded " + groupChatByName.size() + " group chats from database");
	}

	public GroupChat createGroupChat(String name, long guildID, long channelID) {
		KiraRole perm = KiraMain.getInstance().getKiraRoleManager().getOrCreateRole(name + accessPermSuffix);
		int id = dao.createGroupChat(guildID, channelID, name, perm);
		GroupChat chat = new GroupChat(id, name, channelID, guildID, perm);
		putGroupChat(chat);
		logger.info("Successfully created group chat for group " + chat.toString());
		return chat;
	}

	public GroupChat createGroupChat(String name) {
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
		return createGroupChat(name, guild.getIdLong(), channel.getIdLong());
	}
	
	public void deleteGroupChat(GroupChat chat) {
		Channel channel = KiraMain.getInstance().getJDA().getTextChannelById(chat.getDiscordChannelId());
		logger.info("Deleting channel for " + channel.getName());
		if (channel.getGuild().getIdLong() == KiraMain.getInstance().getGuild().getIdLong()) {
			channel.delete().queue();
		}
		groupChatByName.remove(chat.getName());
		chatsByChannelId.remove(chat.getDiscordChannelId());
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
	}

	public void addMember(GroupChat chat, User user) {
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

	public void removeMember(GroupChat chat, User user) {
		KiraMain.getInstance().getKiraRoleManager().takeRoleFromUser(user, chat.getTiedRole());
		logger.info("Taking tied role for chat " + chat.getName() + " from " + user.toString());
		//TODO
	}

}
