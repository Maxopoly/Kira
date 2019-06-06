package com.github.maxopoly.kira.command.discord;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.github.maxopoly.kira.api.token.APIDataType;
import com.github.maxopoly.kira.command.model.discord.ArgumentBasedCommand;
import com.github.maxopoly.kira.command.model.top.InputSupplier;
import com.github.maxopoly.kira.rabbit.session.APIPermissionRequest;
import com.github.maxopoly.kira.user.KiraUser;
import com.github.maxopoly.kira.KiraMain;

public class GenerateAPIToken extends ArgumentBasedCommand {

	public GenerateAPIToken() {
		super("apitoken", 1, 3, "genapitoken", "getapitoken");
		doesRequireIngameAccount();
	}

	@Override
	public String getFunctionality() {
		return "Generates a token for use with Kiras API";
	}

	@Override
	public String getRequiredPermission() {
		return "isauth";
	}

	@Override
	public String getUsage() {
		return "apitoken <SNITCH|CHAT|SKYNET>";
	}

	@Override
	public String handle(InputSupplier sender, String[] args) {
		KiraUser user = sender.getUser();
		List<APIDataType> requestedData = new LinkedList<>();
		for (String arg : args) {
			try {
				APIDataType dataType = APIDataType.valueOf(arg.toUpperCase());
				requestedData.add(dataType);
			} catch (IllegalArgumentException e) {
				return arg + " is not a valid data type, allowed ones are: " + Arrays.asList(APIDataType.values());
			}
		}
		KiraMain.getInstance().getRequestSessionManager()
				.request(new APIPermissionRequest(user.getIngameUUID(), sender, requestedData, -1));
		return "Contacting ingame server to retrieve group permission data";

	}

}
