package com.github.maxopoly.Kira.command.commands;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.command.Command;
import com.github.maxopoly.Kira.command.InputSupplier;
import com.github.maxopoly.Kira.relay.RelayConfig;
import com.github.maxopoly.Kira.relay.RelayConfigManager;
import com.github.maxopoly.Kira.user.KiraUser;

public class CreateRelayConfig extends Command {

	public CreateRelayConfig() {
		super("createrelayconfig", 1, 1);
		setRequireIngameAccount();
	}

	@Override
	public String execute(InputSupplier sender, String[] args) {
		KiraUser user = sender.getUser();
		RelayConfigManager configMan = KiraMain.getInstance().getRelayConfigManager();
		RelayConfig config = configMan.getByName(args [0]);
		if (config != null) {
			return "A relay config with the given name already exists";
		}
		config = configMan.createRelayConfig(args[0], user);
		if (config == null) {
			return "Failed to create relay config, something went wrong";
		}
		return "Successfully created relay config " + config.getName();
	}

	@Override
	public String getUsage() {
		return "createrelayconfig [name]";
	}

	@Override
	public String getFunctionality() {
		return "Creates a new relay configuration";
	}

	@Override
	public String getRequiredPermission() {
		return "isauth";
	}

}
