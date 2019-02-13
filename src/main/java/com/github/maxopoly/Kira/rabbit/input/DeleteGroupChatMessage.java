package com.github.maxopoly.Kira.rabbit.input;

import java.util.UUID;

import org.json.JSONObject;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.group.GroupChat;
import com.github.maxopoly.Kira.group.GroupChatManager;
import com.github.maxopoly.Kira.user.KiraUser;

public class DeleteGroupChatMessage extends RabbitMessage {

	public DeleteGroupChatMessage() {
		super("deletegroupchat");
	}

	@Override
	public void handle(JSONObject json) {
		UUID destroyerUUID = UUID.fromString(json.getString("sender"));
		KiraUser destroyer = KiraMain.getInstance().getUserManager().getUserByIngameUUID(destroyerUUID);
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

