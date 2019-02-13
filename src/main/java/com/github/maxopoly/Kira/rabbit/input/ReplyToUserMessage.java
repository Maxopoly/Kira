package com.github.maxopoly.Kira.rabbit.input;

import java.util.UUID;

import org.json.JSONObject;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.command.DiscordCommandPMSupplier;
import com.github.maxopoly.Kira.user.KiraUser;

import net.dv8tion.jda.core.JDA;

public class ReplyToUserMessage extends RabbitMessage {

	public ReplyToUserMessage() {
		super("replytouser");
	}

	@Override
	public void handle(JSONObject json) {
		UUID uuid = UUID.fromString(json.getString("user"));
		String msg = json.getString("msg");
		JDA jda = KiraMain.getInstance().getJDA();
		KiraUser user = KiraMain.getInstance().getUserManager().getUserByIngameUUID(uuid);
		if (user != null) {
			new DiscordCommandPMSupplier(user).reportBack(msg);
		}
	}
}
