package com.github.maxopoly.kira.api.input;

import com.github.maxopoly.kira.command.model.json.JsonInput;

public abstract class APIInput extends JsonInput<APISupplier> {

	public APIInput(String identifier, String ... alt) {
		super(identifier, alt);
	}
}
