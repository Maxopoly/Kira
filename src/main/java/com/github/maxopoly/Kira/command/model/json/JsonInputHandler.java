package com.github.maxopoly.Kira.command.model.json;

import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.github.maxopoly.Kira.command.model.top.InputSupplier;
import com.github.maxopoly.Kira.command.model.top.TextInputHandler;

public abstract class JsonInputHandler <S extends InputSupplier> extends TextInputHandler<JsonInput<S>, JSONObject, S>{
	
	private String identifierKey;

	public JsonInputHandler(Logger logger, String identifierKey) {
		super(logger);
		this.identifierKey = identifierKey;
	}

	@Override
	protected JSONObject convertIntoArgument(String raw) {
		return new JSONObject(raw);
	}

	@Override
	protected JSONObject getCommandArguments(JSONObject fullArgument) {
		return fullArgument;
	}

	@Override
	protected String getCommandIdentifier(JSONObject argument) {
		return argument.optString(identifierKey, "");
	}

}
