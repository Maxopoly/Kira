package com.github.maxopoly.kira.rabbit;

import java.util.UUID;

import org.json.JSONObject;

import com.github.maxopoly.kira.relay.GroupChat;
import com.github.maxopoly.kira.user.KiraUser;

import net.dv8tion.jda.api.entities.TextChannel;

public class MinecraftRabbitGateway {

	private RabbitHandler rabbit;

	public MinecraftRabbitGateway(RabbitHandler rabbit) {
		this.rabbit = rabbit;
	}

	public void requestRelayCreation(KiraUser sender, String name, TextChannel channel) {
		JSONObject json = new JSONObject();
		json.put("group", name);
		json.put("sender", sender.getIngameUUID().toString());
		json.put("channelID", channel.getId());
		json.put("guildID", channel.getGuild().getId());
		rabbit.sendMessage("requestrelaycreation", json);
	}

	public void sendGroupChatMessage(KiraUser sender, GroupChat chat, String msg) {
		JSONObject json = new JSONObject();
		json.put("group", chat.getName());
		json.put("sender", sender.getIngameUUID().toString());
		json.put("message", msg);
		rabbit.sendMessage("sendgroupmessage", json);
	}

	public void sendMessage(UUID receiver, String msg) {
		JSONObject json = new JSONObject();
		json.put("receiver", receiver.toString());
		json.put("message", msg);
		rabbit.sendMessage("sendmessage", json);
	}

}
