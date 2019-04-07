package com.github.maxopoly.Kira.rabbit;

import java.util.UUID;

import org.json.JSONObject;

import com.github.maxopoly.Kira.relay.GroupChat;
import com.github.maxopoly.Kira.user.KiraUser;

import net.dv8tion.jda.core.entities.Channel;

public class MinecraftRabbitGateway {

	private RabbitHandler rabbit;

	public MinecraftRabbitGateway(RabbitHandler rabbit) {
		this.rabbit = rabbit;
	}

	public void requestRelayCreation(KiraUser sender, String name, Channel channel) {
		JSONObject json = new JSONObject();
		json.put("group", name);
		json.put("sender", sender.getIngameUUID().toString());
		json.put("channelID", channel.getId());
		json.put("guildID", channel.getGuild().getId());
		rabbit.sendMessage("requestrelaycreation", json);
	}

	public void runCommand(UUID uuid, String command) {
		JSONObject json = new JSONObject();
		json.put("uuid", uuid.toString());
		json.put("command", command);
		rabbit.sendMessage("ingame", json);
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
