package com.github.maxopoly.Kira.api.input;

import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.maxopoly.Kira.api.input.impl.AuthAPIInput;
import com.github.maxopoly.Kira.command.InputSupplier;
import com.github.maxopoly.Kira.command.TextInputHandler;

public class APIInputHandler extends TextInputHandler<APIInput>{

	public APIInputHandler(Logger logger) {
		super(logger);
	}

	@Override
	protected void registerCommands() {
		registerCommand(new AuthAPIInput());
	}

	@Override
	protected String getHandlerName() {
		return "API Input handler";
	}
	
	public void handle(String input, InputSupplier supplier) {
		JSONObject json;
		try {
		json = new JSONObject(input);
		} catch(JSONException e) {
			logger.warn(supplier.toString() + " supplied invalid json");
			throw new IllegalArgumentException();
		}
		String type = json.optString("type");
		if (type == null) {
			logger.warn(supplier.toString() + " supplied json without type string: " + json.toString());
			throw new IllegalArgumentException();
		}
		APIInput comm = commands.get(type);
		if (comm == null) {
			logger.warn(supplier.toString() + " supplied json with invalid type: " + json.toString());
			throw new IllegalArgumentException();
		}
		comm.handle(json, (APISupplier) supplier);
	}

	@Override
	protected void handleInput(APIInput input, InputSupplier supplier, String arguments) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleError(InputSupplier supplier, String input) {
		// TODO Auto-generated method stub
		
	}

}
