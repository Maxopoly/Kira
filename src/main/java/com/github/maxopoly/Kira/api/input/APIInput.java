package com.github.maxopoly.Kira.api.input;

import com.github.maxopoly.Kira.command.model.json.JsonInput;

public abstract class APIInput extends JsonInput<APISupplier> {

	public APIInput(String identifier, String ... alt) {
		super(identifier, alt);
	}
}
