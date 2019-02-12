package com.github.maxopoly.Kira.rabbit.input;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.group.GroupChat;
import com.github.maxopoly.Kira.group.GroupChatManager;
import com.github.maxopoly.Kira.user.User;
import com.github.maxopoly.Kira.user.UserManager;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

public class CreateGroupChatMessage extends RabbitMessage {

	public CreateGroupChatMessage() {
		super("creategroupchat");
	}

	@Override
	public void handle(JSONObject json) {
		UUID creatorUUID = UUID.fromString(json.getString("creator"));
		User creator = KiraMain.getInstance().getUserManager().getUserByIngameUUID(creatorUUID);
		if (creator == null) {
			KiraMain.getInstance().getMCRabbitGateway().sendMessage(creatorUUID, "Channel creation failed, "
					+ "no discord account tied");
			return;
		}
		String group = json.getString("group");
		GroupChatManager man = KiraMain.getInstance().getGroupChatManager();
		logger.info("Attempting creation of chat for " + group + " as initiated by " + creator.toString());
		GroupChat chat = man.createGroupChat(group);
		if (chat == null) {
			KiraMain.getInstance().getMCRabbitGateway().sendMessage(creatorUUID, "Channel creation failed, "
					+ "ask an admin about this");
			return;
		}
		JSONArray memberArray = json.getJSONArray("members");
		Set<Integer> shouldBeMembers = new HashSet<>();
		UserManager userMan = KiraMain.getInstance().getUserManager();
		for (int i = 0; i < memberArray.length(); i++) {
			UUID uuid = UUID.fromString(memberArray.getString(i));
			User user = userMan.getUserByIngameUUID(uuid);
			if (user == null) {
				continue;
			}
			shouldBeMembers.add(user.getID());
		}
		man.syncAccess(chat, shouldBeMembers);
		KiraMain.getInstance().getMCRabbitGateway().sendMessage(creatorUUID, "Created channel successfully");
		JDA jda = KiraMain.getInstance().getJDA();
		TextChannel channel = jda.getTextChannelById(chat.getDiscordChannelId());
		if (channel != null) {
			Member mem = channel.getGuild().getMemberById(creator.getDiscordID());
			channel.sendMessage(mem.getAsMention()).queue();
		}
	}

}
