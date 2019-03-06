package com.github.maxopoly.Kira.command;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.logging.log4j.Logger;

public abstract class TextInputHandler <T extends TextInput> {

	protected Map<String, T> commands;
	protected Logger logger;

	public TextInputHandler(Logger logger) {
		this.commands = new HashMap<String, T>();
		this.logger = logger;
		registerCommands();
	}

	/**
	 * Registers all native inputs
	 */
	protected abstract void registerCommands();

	protected abstract String getHandlerName();

	protected abstract void handleInput(T input, InputSupplier supplier, String arguments);

	protected abstract void handleError(InputSupplier supplier, String input);

	public void registerCommand(T command) {
		commands.put(command.getIdentifier().toLowerCase(), command);
		if (command.getAlternativeIdentifiers() != null) {
			for (String alt : command.getAlternativeIdentifiers()) {
				commands.put(alt.toLowerCase(), command);
			}
		}
	}

	public T getHandler(String key) {
		return commands.get(key.toLowerCase());
	}

	public void unregisterCommand(T command) {
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

	public Collection<T> getAllInputs() {
		//new set to remove duplicates because of aliases
		return new HashSet<>(commands.values());
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
		command = command.trim().toLowerCase();
		T comm = commands.get(command);
		if (comm == null) {
			handleError(supplier, input);
			return;
		}
		handleInput(comm, supplier, arguments);
	}

}
