package com.github.maxopoly.Kira.rabbit;

import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.github.maxopoly.Kira.command.model.json.JsonInputHandler;
import com.github.maxopoly.Kira.rabbit.input.AddAuthMessage;
import com.github.maxopoly.Kira.rabbit.input.CreateGroupChatMessage;
import com.github.maxopoly.Kira.rabbit.input.DeleteGroupChatMessage;
import com.github.maxopoly.Kira.rabbit.input.ReplyToUserMessage;
import com.github.maxopoly.Kira.rabbit.input.RequestSessionReplyMessage;
import com.github.maxopoly.Kira.rabbit.input.SendGroupChatMessage;
import com.github.maxopoly.Kira.rabbit.input.SkynetMessage;
import com.github.maxopoly.Kira.rabbit.input.SnitchHitMessage;
import com.github.maxopoly.Kira.rabbit.input.SyncGroupChatMembers;

public class RabbitInputProcessor extends JsonInputHandler<RabbitInputSupplier> {

	public RabbitInputProcessor(Logger logger) {
		super(logger, "packettype");
	}

	@Override
	protected String getHandlerName() {
		return "Rabbit Input handler";
	}

	@Override
	protected void handleError(RabbitInputSupplier supplier, JSONObject input) {
		logger.error("Unknown id received in rabbit message: " + input.toString());
	}

	@Override
	protected void registerCommands() {
		registerCommand(new AddAuthMessage());
		registerCommand(new SendGroupChatMessage());
		registerCommand(new CreateGroupChatMessage());
		registerCommand(new SyncGroupChatMembers());
		registerCommand(new DeleteGroupChatMessage());
		registerCommand(new ReplyToUserMessage());
		registerCommand(new SnitchHitMessage());
		registerCommand(new RequestSessionReplyMessage());
		registerCommand(new SkynetMessage());
	}

}
