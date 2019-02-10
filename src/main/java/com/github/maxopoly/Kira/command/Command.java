package com.github.maxopoly.Kira.command;

public abstract class Command extends TextInput {

	private int minArgs;
	private int maxArgs;

	public Command(String identifier, int minArgs, int maxArgs, String... alt) {
		super(identifier, alt);
		if (maxArgs < minArgs) {
			throw new IllegalArgumentException("minArgs can't be bigger than maxArgs");
		}
		this.minArgs = minArgs;
		this.maxArgs = maxArgs;
	}

	/**
	 * @return Minimum amount of arguments
	 */
	public int minimumArgs() {
		return minArgs;
	}

	/**
	 * @return Maximum amount of arguments
	 */
	public int maximumArgs() {
		return maxArgs;
	}

	public abstract String execute(InputSupplier sender, String[] args);

	public abstract String getUsage();

	public abstract String getFunctionality();

	public abstract String getRequiredPermission();

}
