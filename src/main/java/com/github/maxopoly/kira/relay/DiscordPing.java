package com.github.maxopoly.kira.relay;

public enum DiscordPing {
	
	NONE, HERE, EVERYONE;
	
	
	@Override
	public String toString() {
		switch(this) {
		case EVERYONE:
			return "@everyone";
		case HERE:
			return "@everyone";
		case NONE:
			return "";
		}
		throw new IllegalStateException();
	}

}
