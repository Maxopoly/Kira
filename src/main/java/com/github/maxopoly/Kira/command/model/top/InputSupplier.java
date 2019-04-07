package com.github.maxopoly.Kira.command.model.top;

import com.github.maxopoly.Kira.user.KiraUser;

public interface InputSupplier {

	public long getChannelID();

	public String getIdentifier();

	public KiraUser getUser();

	public boolean hasPermission(String perm);

	public void reportBack(String msg);

}
