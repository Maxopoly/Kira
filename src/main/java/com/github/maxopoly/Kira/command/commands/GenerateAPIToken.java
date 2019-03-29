package com.github.maxopoly.Kira.command.commands;

import com.github.maxopoly.Kira.command.Command;
import com.github.maxopoly.Kira.command.InputSupplier;
import com.github.maxopoly.Kira.user.KiraUser;

public class GenerateAPIToken extends Command {

	public GenerateAPIToken() {
		super("apitoken", 0, 0, "genapitoken", "getapitoken");
		doesRequireIngameAccount();
	}

	@Override
	public String execute(InputSupplier sender, String[] args) {
		KiraUser user = sender.getUser();
		
		return "";
		
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFunctionality() {
		return "Generates a token for use with Kiras API";
	}

	@Override
	public String getRequiredPermission() {
		return "isauth";
	}

}
