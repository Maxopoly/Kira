package com.github.maxopoly.Kira.rabbit.input;

import org.json.JSONObject;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.relay.GroupChat;
import com.github.maxopoly.Kira.relay.GroupChatManager;

public class SendGroupChatMessage extends RabbitMessage {

	public SendGroupChatMessage() {
		super("groupchatmessage");
	}

	@Override
	public void handle(JSONObject json) {
		String msg = json.getString("msg");
		String runner = json.getString("sender");
		String group = json.getString("group");
		GroupChatManager man = KiraMain.getInstance().getGroupChatManager();
		GroupChat chat = man.getGroupChat(group);
		if (chat != null && chat.getConfig().shouldRelayToDiscord()) {
			chat.sendMessage(runner, msg);
		}
	}
}
