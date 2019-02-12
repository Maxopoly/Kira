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

public class SyncGroupChatMembers extends RabbitMessage {

	public SyncGroupChatMembers() {
		super("syncgroupchatmembers");
	}

	@Override
	public void handle(JSONObject json) {
		JSONArray memberArray = json.getJSONArray("members");
		String group = json.getString("group");
		GroupChatManager man = KiraMain.getInstance().getGroupChatManager();
		UserManager userMan = KiraMain.getInstance().getUserManager();
		GroupChat chat = man.getGroupChat(group);
		if (chat == null) {
			logger.warn("Tried to update members for group " + group + ", but it wasnt found");
			return;
		}
		Set<Integer> shouldBeMembers = new HashSet<>();
		for (int i = 0; i < memberArray.length(); i++) {
			UUID uuid = UUID.fromString(memberArray.getString(i));
			User user = userMan.getUserByIngameUUID(uuid);
			if (user == null) {
				continue;
			}
			shouldBeMembers.add(user.getID());
		}
		man.syncAccess(chat, shouldBeMembers);
	}
}