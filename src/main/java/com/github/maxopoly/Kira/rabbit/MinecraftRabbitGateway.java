package com.github.maxopoly.Kira.rabbit;

import java.util.UUID;

import org.json.JSONObject;

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

}
