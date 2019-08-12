package com.github.maxopoly.kira.command.discord.api;

import java.util.List;

import com.github.maxopoly.kira.KiraMain;
import com.github.maxopoly.kira.api.token.APIToken;
import com.github.maxopoly.kira.command.model.discord.ArgumentBasedCommand;
import com.github.maxopoly.kira.command.model.top.InputSupplier;

public class RevokeAPIToken extends ArgumentBasedCommand {

	public RevokeAPIToken() {
		super("revoketoken", 1, "deletetoken", "revokeapitoken", "deleteapitoken");
		doesRequireUser();
	}

	@Override
	public String getFunctionality() {
		return "Invalidates an existing API token based on its index number, effectively deleting it";
	}

	@Override
	public String getRequiredPermission() {
		return "isauth";
	}

	@Override
	public String getUsage() {
		return "revoketoken <number>";
	}

	@Override
	protected String handle(InputSupplier sender, String[] args) {
		int number;
		try {
			number = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			return String.format("%s is not a number", args[0]);
		}
		List<APIToken> tokens = KiraMain.getInstance().getAPISessionManager().getTokenManager()
				.getTokensForUser(sender.getUser());
		if (number > tokens.size() || number <= 0) {
			return String.format("You can not delete token %d, because there are only %d tokens total", number,
					tokens.size());
		}
		APIToken token = tokens.get(number - 1);
		KiraMain.getInstance().getAPISessionManager().getTokenManager().removeToken(token);
		return String.format("Deleted token with index '%d' and secret '%s'", number, token.getSecret());
	}

}
