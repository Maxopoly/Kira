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

public class DeleteGroupChatMessage extends RabbitMessage {

	public DeleteGroupChatMessage() {
		super("deletegroupchat");
	}

	@Override
	public void handle(JSONObject json) {
		UUID destroyerUUID = UUID.fromString(json.getString("sender"));
		User destroyer = KiraMain.getInstance().getUserManager().getUserByIngameUUID(destroyerUUID);
		if (destroyer == null) {
			KiraMain.getInstance().getMCRabbitGateway().sendMessage(destroyerUUID, "Channel deletion failed, "
					+ "no discord account tied");
			return;
		}
		String group = json.getString("group");
		GroupChatManager man = KiraMain.getInstance().getGroupChatManager();
		GroupChat chat = man.getGroupChat(group);
		if (chat == null) {
			logger.warn("Failed to delete group chat"+ group + ", it was already gone");
			KiraMain.getInstance().getMCRabbitGateway().sendMessage(destroyerUUID, "Channel deletion failed, no channel found");
			return;
		}
		logger.info("Attempting delete group of chat for " + group + " as initiated by " + destroyer.toString());
		man.deleteGroupChat(chat);
		KiraMain.getInstance().getMCRabbitGateway().sendMessage(destroyerUUID, "Deleted channel successfully");
	}

}

