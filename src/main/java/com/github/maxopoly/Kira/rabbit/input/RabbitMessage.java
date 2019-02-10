package com.github.maxopoly.Kira.rabbit.input;

import org.json.JSONObject;

import com.github.maxopoly.Kira.command.TextInput;

public abstract class RabbitMessage extends TextInput {

	public RabbitMessage(String identifier, String... alt) {
		super(identifier, alt);
		// TODO Auto-generated constructor stub
	}

	public abstract void handle(JSONObject json);

}
