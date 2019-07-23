package com.github.maxopoly.kira.command.discord.api;

import java.util.Collection;

import com.github.maxopoly.kira.KiraMain;
import com.github.maxopoly.kira.api.token.APIToken;
import com.github.maxopoly.kira.command.model.discord.Command;
import com.github.maxopoly.kira.command.model.top.InputSupplier;

public class ListTokens extends Command {

	public ListTokens() {
		super("listtokens", "showtokens", "apitokens", "tokens", "tokenlist", "listapitokens");
		doesRequireUser();
	}

	@Override
	public String getFunctionality() {
		return "Lists all unused API tokens for your account";
	}

	@Override
	public String getRequiredPermission() {
		return "isauth";

	}

	@Override
	public String getUsage() {
		return "listtokens";
	}

	@Override
	protected String handleInternal(String argument, InputSupplier sender) {
		Collection<APIToken> tokens = KiraMain.getInstance().getAPISessionManager().getTokenManager()
				.getTokensForUser(sender.getUser());
		if (tokens.isEmpty()) {
			return "You have no open tokens";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("You have a total of ");
		sb.append(tokens.size());
		sb.append(" open tokens:\n");
		int counter = 1;
		for(APIToken token: tokens) {
			sb.append(counter++);
			sb.append(" - Secret: ");
			sb.append(token.getSecret());
			long expireTime = token.getExpirationTime();
			if (expireTime == -1) {
				sb.append(", Expires never\n");
			}
			else {
				sb.append(", Expires in: ");
				sb.append((System.currentTimeMillis() - expireTime)/ (1000 * 60));
				sb.append(" minutes");
			}
		}
		return sb.toString();
	}

}
