package com.github.maxopoly.kira.relay.actions;

public enum SnitchHitType {

	ENTER, LOGIN, LOGOUT;

	public static SnitchHitType fromString(String value) {
		return SnitchHitType.valueOf(value.trim().toUpperCase());
	}

}
