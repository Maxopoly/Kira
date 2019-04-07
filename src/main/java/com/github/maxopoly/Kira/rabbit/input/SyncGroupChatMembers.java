package com.github.maxopoly.Kira.rabbit.input;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.rabbit.RabbitInputSupplier;
import com.github.maxopoly.Kira.relay.GroupChat;
import com.github.maxopoly.Kira.relay.GroupChatManager;
import com.github.maxopoly.Kira.user.KiraUser;
import com.github.maxopoly.Kira.user.UserManager;

public class SyncGroupChatMembers extends RabbitMessage {

	public SyncGroupChatMembers() {
		super("syncgroupchatmembers");
	}

	@Override
	public void handle(JSONObject json, RabbitInputSupplier supplier) {
		JSONArray memberArray = json.getJSONArray("members");
		String group = json.getString("group");
		UUID sender = UUID.fromString(json.getString("sender"));
		GroupChatManager man = KiraMain.getInstance().getGroupChatManager();
		UserManager userMan = KiraMain.getInstance().getUserManager();
		GroupChat chat = man.getGroupChat(group);
		if (chat == null) {
			KiraMain.getInstance().getMCRabbitGateway().sendMessage(sender,
					"That group does not have a relay setup");
			return;
		}
		if (KiraMain.getInstance().getGuild().getIdLong() != chat.getGuildId()) {
			KiraMain.getInstance().getMCRabbitGateway().sendMessage(sender,
					"This relay is not managed by Kira, it can not be synced");
			return;
		}
		Set<Integer> shouldBeMembers = new HashSet<>();
		for (int i = 0; i < memberArray.length(); i++) {
			UUID uuid = UUID.fromString(memberArray.getString(i));
			KiraUser user = userMan.getUserByIngameUUID(uuid);
			if (user == null) {
				continue;
			}
			shouldBeMembers.add(user.getID());
		}
		man.syncAccess(chat, shouldBeMembers);
	}
}