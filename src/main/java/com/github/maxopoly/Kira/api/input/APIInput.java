package com.github.maxopoly.Kira.api.input;

import org.json.JSONObject;

import com.github.maxopoly.Kira.command.TextInput;

public abstract class APIInput extends TextInput {

	public APIInput(String identifier, String ... alt) {
		super(identifier, alt);
	}
	
	public abstract void handle(JSONObject json, APISupplier supplier);

}
