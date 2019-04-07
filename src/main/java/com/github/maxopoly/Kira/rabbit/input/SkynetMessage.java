package com.github.maxopoly.Kira.rabbit.input;

import org.json.JSONObject;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.rabbit.RabbitInputSupplier;
import com.github.maxopoly.Kira.relay.actions.SkynetAction;
import com.github.maxopoly.Kira.relay.actions.SkynetType;

public class SkynetMessage extends RabbitMessage {

	public SkynetMessage() {
		super("skynet");
	}

	@Override
	public void handle(JSONObject json, RabbitInputSupplier supplier) {
		String player = json.getString("player");
		SkynetType type = SkynetType.valueOf(json.getString("action").toUpperCase());
		long timestamp = json.optLong("timestamp", System.currentTimeMillis());
		SkynetAction action = new SkynetAction(timestamp, player, type);
		KiraMain.getInstance().getAPISessionManager().handleSkynetMessage(action);
		KiraMain.getInstance().getGroupChatManager().applyToAll(chat -> {chat.sendSkynet(action);});
	}
}
