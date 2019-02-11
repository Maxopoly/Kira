package com.github.maxopoly.Kira.command.commands;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.command.Command;
import com.github.maxopoly.Kira.command.InputSupplier;
import com.github.maxopoly.Kira.permission.KiraRoleManager;

public class ReloadPermissionCommand extends Command {

	public ReloadPermissionCommand() {
		super("reloadperms", 0, 0, "reloadpermissions");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(InputSupplier sender, String[] args) {
		KiraRoleManager roleMan = KiraMain.getInstance().getDAO().loadAllRoles();
		KiraMain.getInstance().getKiraRoleManager().reload(roleMan);
		return "Successfully reloaded permissions";
	}

	@Override
	public String getUsage() {
		return "reloadperms";
	}

	@Override
	public String getFunctionality() {
		return "Reloads all permissions and roles from the database";
	}

	@Override
	public String getRequiredPermission() {
		return "admin";
	}

}
