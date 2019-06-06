package com.github.maxopoly.kira.rabbit.input;

import org.json.JSONObject;

import com.github.maxopoly.kira.rabbit.RabbitInputSupplier;
import com.github.maxopoly.kira.relay.GroupChat;
import com.github.maxopoly.kira.relay.GroupChatManager;
import com.github.maxopoly.kira.relay.actions.GroupChatMessageAction;
import com.github.maxopoly.kira.KiraMain;

public class SendGroupChatMessage extends RabbitMessage {

	public SendGroupChatMessage() {
		super("groupchatmessage");
	}

	@Override
	public void handle(JSONObject json, RabbitInputSupplier supplier) {
		String msg = json.getString("msg");
		String sender = json.getString("sender");
		String group = json.getString("group");
		long timestamp = json.optLong("timestamp", System.currentTimeMillis());
		GroupChatMessageAction action = new GroupChatMessageAction(timestamp, group, sender, msg);
		KiraMain.getInstance().getAPISessionManager().handleGroupMessage(action);
		GroupChatManager man = KiraMain.getInstance().getGroupChatManager();
		GroupChat chat = man.getGroupChat(group);
		if (chat != null && chat.getConfig().shouldRelayToDiscord()) {
			chat.sendMessage(action);
		}
	}
}
