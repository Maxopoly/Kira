package com.github.maxopoly.Kira.rabbit;

import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.github.maxopoly.Kira.command.InputSupplier;
import com.github.maxopoly.Kira.command.TextInput;
import com.github.maxopoly.Kira.command.TextInputHandler;
import com.github.maxopoly.Kira.rabbit.input.AddAuthMessage;
import com.github.maxopoly.Kira.rabbit.input.RabbitMessage;

public class RabbitInputProcessor extends TextInputHandler {

	public RabbitInputProcessor(Logger logger) {
		super(logger);
	}

	@Override
	protected void registerCommands() {
		registerCommand(new AddAuthMessage());
	}

	@Override
	protected String getHandlerName() {
		return "Rabbit input handler";
	}

	@Override
	protected void handleInput(TextInput input, InputSupplier supplier, String arguments) {
		RabbitMessage msg = (RabbitMessage) input;
		JSONObject json = new JSONObject(arguments);
		msg.handle(json);
	}

	@Override
	protected void handleError(InputSupplier supplier, String input) {
		logger.error("Unknown id received in rabbit message: " + input);
	}

}
