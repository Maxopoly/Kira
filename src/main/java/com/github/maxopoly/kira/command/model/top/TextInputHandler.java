package com.github.maxopoly.kira.command.model.top;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.logging.log4j.Logger;

public abstract class TextInputHandler <T extends TextInput<A,S>, A, S extends InputSupplier> {

	protected Map<String, T> commands;
	protected Logger logger;

	public TextInputHandler(Logger logger) {
		this.commands = new HashMap<>();
		this.logger = logger;
		registerCommands();
	}

	protected abstract A convertIntoArgument(String raw);

	public Collection<T> getAllInputs() {
		//new set to remove duplicates because of aliases
		return new HashSet<>(commands.values());
	}

	protected abstract A getCommandArguments(A fullArgument);
	
	protected abstract String getCommandIdentifier(A argument);
	
	public T getHandler(String key) {
		return commands.get(key.toLowerCase());
	}
	
	protected abstract String getHandlerName();

	public void handle(String input, S supplier) {
		if (input == null || input.equals("")) {
			logger.info("Invalid input in " + getHandlerName());
			return;
		}
		A fullArgument = convertIntoArgument(input);
		String key = getCommandIdentifier(fullArgument);
		T comm = commands.get(key);
		if (comm == null) {
			handleError(supplier, fullArgument);
			return;
		}
		comm.handle(getCommandArguments(fullArgument), supplier);
	}

	protected abstract void handleError(S supplier, A input);

	public void registerCommand(T command) {
		commands.put(command.getIdentifier().toLowerCase(), command);
		if (command.getAlternativeIdentifiers() != null) {
			for (String alt : command.getAlternativeIdentifiers()) {
				commands.put(alt.toLowerCase(), command);
			}
		}
	}

	/**
	 * Registers all native inputs
	 */
	protected abstract void registerCommands();

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

}
