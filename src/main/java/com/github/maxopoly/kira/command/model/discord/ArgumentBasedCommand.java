package com.github.maxopoly.kira.command.model.discord;

import com.github.maxopoly.kira.command.model.top.InputSupplier;

public abstract class ArgumentBasedCommand extends Command {

	private int minArgs;
	private int maxArgs;

	public ArgumentBasedCommand(String identifier, int minArgs, int maxArgs, String ... alt) {
		super(identifier, alt);
		this.minArgs = minArgs;
		this.maxArgs = maxArgs;
	}
	
	public ArgumentBasedCommand(String identifier, int argCount, String ... alt) {
		this(identifier, argCount, argCount, alt);
	}

	protected abstract String handle(InputSupplier sender, String[] args);

	@Override
	public String handleInternal(String argument, InputSupplier sender) {
		String [] args;
		if (argument.length() == 0) {
			args = new String [0];
		}
		else {
			args = argument.split(" ");
		}
		if (args.length < minArgs) {
			return getIdentifier() + " requires at least " + minArgs + " parameter\nUsage: "
					+ getUsage();
		}
		if (args.length > maxArgs) {
			return getIdentifier() + " accepts at maximum " + maxArgs + " parameter\nUsage: "
					+ getUsage();
		}
		return handle(sender, args);
	}

}
