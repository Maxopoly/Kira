package com.github.maxopoly.Kira.command;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

public abstract class TextInputHandler {

	protected Map<String, TextInput> commands;
	protected Logger logger;

	public TextInputHandler(Logger logger) {
		this.commands = new HashMap<String, TextInput>();
		this.logger = logger;
		registerCommands();
	}

	/**
	 * Registers all native inputs
	 */
	protected abstract void registerCommands();

	protected abstract String getHandlerName();

	protected abstract void handleInput(TextInput input, InputSupplier supplier, String arguments);

	protected abstract void handleError(InputSupplier supplier, String input);

	public synchronized void registerCommand(TextInput command) {
		commands.put(command.getIdentifier().toLowerCase(), command);
		if (command.getAlternativeIdentifiers() != null) {
			for (String alt : command.getAlternativeIdentifiers()) {
				commands.put(alt.toLowerCase(), command);
			}
		}
	}

	public synchronized void unregisterCommand(TextInput command) {
		String key = command.getIdentifier().toLowerCase();
		if (commands.get(key) == command) {
			commands.remove(key);
		}
		if (command.getAlternativeIdentifiers() != null) {
			for (String alt : command.getAlternativeIdentifiers()) {
				key = alt.toLowerCase();
				if (commands.get(key) == command) {
					commands.remove(key);
				}
			}
		}
	}

	public void handle(String input, InputSupplier supplier) {
		if (input == null || input.equals("")) {
			logger.info("Invalid input in " + getHandlerName());
			return;
		}
		int spaceIndex = input.indexOf(" ");
		String arguments;
		String command;
		if (spaceIndex == -1) {
			arguments = "";
			command = input;
		} else {
			arguments = input.substring(spaceIndex + 1);
			command = input.substring(0, spaceIndex);
		}
		TextInput comm = commands.get(command);
		if (comm == null) {
			handleError(supplier, input);
			return;
		}
		handleInput(comm, supplier, arguments);

	}

}
