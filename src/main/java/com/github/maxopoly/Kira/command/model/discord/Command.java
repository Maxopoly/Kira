package com.github.maxopoly.Kira.command.model.discord;

import com.github.maxopoly.Kira.command.model.top.InputSupplier;
import com.github.maxopoly.Kira.command.model.top.TextInput;

public abstract class Command extends TextInput<String, InputSupplier> {

	private boolean requireUser;
	private boolean requireIngameAccount;

	public Command(String identifier, String... alt) {
		super(identifier, alt);
		this.requireUser = false;
		this.requireIngameAccount = false;
	}

	public boolean doesRequireIngameAccount() {
		return requireIngameAccount;
	}

	public boolean doesRequireUser() {
		return requireUser;
	}

	public abstract String getFunctionality();

	public abstract String getRequiredPermission();

	public abstract String getUsage();

	@Override
	public final void handle(String argument, InputSupplier supplier) {
		if (requireUser && supplier.getUser() == null) {
			supplier.reportBack("You have to be a user to run this command");
			return;
		}
		if (requireIngameAccount && !supplier.getUser().hasIngameAccount()) {
			supplier.reportBack("You need to have an ingame account linked to use this command");
			return;
		}
		if (!supplier.hasPermission(getRequiredPermission())) {
			supplier.reportBack("You don't have the required permission to do this");
			logger.info(supplier.getIdentifier() + " attempted to run forbidden command: " + getIdentifier());
		}
		String reply = handleInternal(argument, supplier);
		supplier.reportBack(reply);
	}

	protected abstract String handleInternal(String argument, InputSupplier sender);

	protected void setRequireIngameAccount() {
		this.requireUser = true;
		this.requireIngameAccount = true;
	}
	
	protected void setRequireUser() {
		this.requireUser = true;
	}

}
