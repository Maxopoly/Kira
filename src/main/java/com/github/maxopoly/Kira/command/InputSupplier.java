package com.github.maxopoly.Kira.command;

import com.github.maxopoly.Kira.user.User;

public interface InputSupplier {

	public String getIdentifier();

	public User getUser();

	public void reportBack(String msg);

	public boolean hasPermission(String perm);

}
