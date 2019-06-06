package com.github.maxopoly.Kira.rabbit.input;

import com.github.maxopoly.Kira.command.model.json.JsonInput;
import com.github.maxopoly.Kira.rabbit.RabbitInputSupplier;

public abstract class RabbitMessage extends JsonInput<RabbitInputSupplier> {

	public RabbitMessage(String identifier, String... alt) {
		super(identifier, alt);
	}
}
