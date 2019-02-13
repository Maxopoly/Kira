package com.github.maxopoly.Kira.rabbit.input;

import org.json.JSONObject;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.group.GroupChat;
import com.github.maxopoly.Kira.group.GroupChatManager;

public class SnitchHitMessage extends RabbitMessage {

	public SnitchHitMessage() {
		super("sendsnitchhit");
	}

	@Override
	public void handle(JSONObject json) {
		String groupName = json.getString("groupName");
		GroupChatManager man = KiraMain.getInstance().getGroupChatManager();
		GroupChat chat = man.getGroupChat(groupName);
		if (chat == null) {
			return;
		}
		String snitchName = json.getString("snitchName");
		//UUID victimUUID = UUID.fromString(json.getString("victimUUID"));
		String victimName = json.getString("victimName");
		int x = json.getInt("x");
		int y = json.getInt("y");
		int z = json.getInt("z");
		chat.sendSnitchHit(victimName, snitchName, x, y, z);
	}
}
