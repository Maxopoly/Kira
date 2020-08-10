package com.github.maxopoly.kira.command.model.top;

import com.github.maxopoly.kira.user.KiraUser;

public interface InputSupplier {

	long getChannelID();

	String getIdentifier();

	KiraUser getUser();

	boolean hasPermission(String perm);

	void reportBack(String msg);

}
