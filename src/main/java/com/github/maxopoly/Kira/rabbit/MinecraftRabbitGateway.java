package com.github.maxopoly.Kira.rabbit;

import java.util.UUID;

import org.json.JSONObject;

import com.github.maxopoly.Kira.group.GroupChat;
import com.github.maxopoly.Kira.user.User;

public class MinecraftRabbitGateway {

	private RabbitHandler rabbit;

	public MinecraftRabbitGateway(RabbitHandler rabbit) {
		this.rabbit = rabbit;
	}

	public void runCommand(UUID uuid, String command) {
		JSONObject json = new JSONObject();
		json.put("runner", uuid.toString());
		json.put("command", command);
		rabbit.sendMessage("ingame", json);
	}
	
	public void sendMessage(UUID receiver, String msg) {
		JSONObject json = new JSONObject();
		json.put("receiver", receiver.toString());
		json.put("message", msg);
		rabbit.sendMessage("sendmessage", json);
	}
	
	public void sendGroupChatMessage(User sender, GroupChat chat, String msg) {
		JSONObject json = new JSONObject();
		json.put("group", chat.getName());
		json.put("sender", sender.getIngameUUID().toString());
		json.put("message", msg);
		rabbit.sendMessage("sendgroupmessage", json);
	}

}
