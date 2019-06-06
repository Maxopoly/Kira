package com.github.maxopoly.kira.command.discord;

import com.github.maxopoly.kira.command.model.discord.Command;
import com.github.maxopoly.kira.command.model.top.InputSupplier;
import com.github.maxopoly.kira.permission.KiraRoleManager;
import com.github.maxopoly.kira.KiraMain;

public class ReloadPermissionCommand extends Command {

	public ReloadPermissionCommand() {
		super("reloadperms", "reloadpermissions");
	}

	@Override
	public String getFunctionality() {
		return "Reloads all permissions and roles from the database";
	}

	@Override
	public String getRequiredPermission() {
		return "admin";
	}

	@Override
	public String getUsage() {
		return "reloadperms";
	}

	@Override
	public String handleInternal(String argument, InputSupplier sender) {
		KiraRoleManager roleMan = KiraMain.getInstance().getDAO().loadAllRoles();
		KiraMain.getInstance().getKiraRoleManager().reload(roleMan);
		return "Successfully reloaded permissions";
	}
}
