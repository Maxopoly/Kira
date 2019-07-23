package com.github.maxopoly.kira.command.discord.relay;

import com.github.maxopoly.kira.KiraMain;
import com.github.maxopoly.kira.command.model.discord.ArgumentBasedCommand;
import com.github.maxopoly.kira.command.model.top.InputSupplier;
import com.github.maxopoly.kira.relay.RelayConfig;
import com.github.maxopoly.kira.relay.RelayConfigManager;
import com.github.maxopoly.kira.user.KiraUser;

public class CreateRelayConfig extends ArgumentBasedCommand {

	public CreateRelayConfig() {
		super("createrelayconfig", 1);
		setRequireIngameAccount();
	}

	@Override
	public String getFunctionality() {
		return "Creates a new relay configuration";
	}

	@Override
	public String getRequiredPermission() {
		return "isauth";
	}

	@Override
	public String getUsage() {
		return "createrelayconfig [name]";
	}

	@Override
	public String handle(InputSupplier sender, String[] args) {
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

}
