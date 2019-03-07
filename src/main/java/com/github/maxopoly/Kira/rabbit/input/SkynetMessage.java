package com.github.maxopoly.Kira.rabbit.input;

import org.json.JSONObject;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.relay.SkynetType;

public class SkynetMessage extends RabbitMessage {

	public SkynetMessage() {
		super("skynet");
	}

	@Override
	public void handle(JSONObject json) {
		String player = json.getString("player");
		SkynetType type = SkynetType.valueOf(json.getString("action").toUpperCase());
		KiraMain.getInstance().getGroupChatManager().applyToAll(chat -> {chat.sendSkynet(player, type);});
	}
}
