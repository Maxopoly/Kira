package com.github.maxopoly.kira.relay.actions;

/**
 * Describes the different kinds of snitches
 *
 */
public enum SnitchType {

	ENTRY, LOGGING;

	public static SnitchType getType(String serial) {
		switch (serial) {
		case "ENTRY":
		case "entry":
		case "Snitch":
			return SnitchType.ENTRY;
		case "logging":
		case "LOGGING":
		case "Logsnitch":
			return LOGGING;
		default:
			return ENTRY;
		}
	}

}
