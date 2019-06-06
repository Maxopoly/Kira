package com.github.maxopoly.kira.command.model.json;

import org.json.JSONObject;

import com.github.maxopoly.kira.command.model.top.InputSupplier;
import com.github.maxopoly.kira.command.model.top.TextInput;

public abstract class JsonInput <I extends InputSupplier> extends TextInput<JSONObject, I> {

	public JsonInput(String identifier, String ... alt) {
		super(identifier, alt);
	}
}
