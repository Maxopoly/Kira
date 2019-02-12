package com.github.maxopoly.Kira.command.commands;

import com.github.maxopoly.Kira.command.Command;
import com.github.maxopoly.Kira.command.InputSupplier;


public class CreateRelayChannelHereCommand extends Command {

	public CreateRelayChannelHereCommand() {
		super("createrelayhere", 1, 1, "createrelay", "makerelay", "setupsnitchchannel");
	}

	@Override
	public String execute(InputSupplier supplier, String[] args) {
		return "";
	}

	@Override
	public String getUsage() {
		return "createrelayhere [group]";
	}

	@Override
	public String getFunctionality() {
		return "Attempts to create a relay in the channel this message was sent in for the group it was sent by";
	}

	@Override
	public String getRequiredPermission() {
		return "isauth";
	}

}

