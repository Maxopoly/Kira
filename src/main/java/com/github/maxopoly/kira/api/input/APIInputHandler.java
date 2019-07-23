package com.github.maxopoly.kira.api.input;

import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.github.maxopoly.kira.api.input.packets.RequestReplacementToken;
import com.github.maxopoly.kira.command.model.json.JsonInputHandler;

public class APIInputHandler extends JsonInputHandler<APISupplier> {

	public APIInputHandler(Logger logger) {
		super(logger, "type");
	}

	@Override
	protected String getHandlerName() {
		return "API Input handler";
	}

	@Override
	protected void handleError(APISupplier supplier, JSONObject input) {
		supplier.getSession().close();
	}

	@Override
	protected void registerCommands() {
		registerCommand(new RequestReplacementToken());
	}
}
