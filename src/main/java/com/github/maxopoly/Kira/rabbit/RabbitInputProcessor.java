package com.github.maxopoly.Kira.rabbit;

import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.github.maxopoly.Kira.command.InputSupplier;
import com.github.maxopoly.Kira.command.TextInput;
import com.github.maxopoly.Kira.command.TextInputHandler;
import com.github.maxopoly.Kira.rabbit.input.AddAuthMessage;
import com.github.maxopoly.Kira.rabbit.input.CreateGroupChatMessage;
import com.github.maxopoly.Kira.rabbit.input.RabbitMessage;
import com.github.maxopoly.Kira.rabbit.input.SendGroupChatMessage;
import com.github.maxopoly.Kira.rabbit.input.SyncGroupChatMembers;

public class RabbitInputProcessor extends TextInputHandler<RabbitMessage> {

	public RabbitInputProcessor(Logger logger) {
		super(logger);
	}

	@Override
	protected void registerCommands() {
		registerCommand(new AddAuthMessage());
		registerCommand(new SendGroupChatMessage());
		registerCommand(new CreateGroupChatMessage());
		registerCommand(new SyncGroupChatMembers());
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
