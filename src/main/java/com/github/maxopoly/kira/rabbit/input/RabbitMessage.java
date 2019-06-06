package com.github.maxopoly.kira.rabbit.input;

import com.github.maxopoly.kira.command.model.json.JsonInput;
import com.github.maxopoly.kira.rabbit.RabbitInputSupplier;

public abstract class RabbitMessage extends JsonInput<RabbitInputSupplier> {

	public RabbitMessage(String identifier, String... alt) {
		super(identifier, alt);
	}
}
