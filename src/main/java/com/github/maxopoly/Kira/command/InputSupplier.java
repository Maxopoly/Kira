package com.github.maxopoly.Kira.command;

import com.github.maxopoly.Kira.user.KiraUser;

public interface InputSupplier {

	public String getIdentifier();

	public KiraUser getUser();

	public void reportBack(String msg);

	public boolean hasPermission(String perm);
	
	public long getChannelID();

}
