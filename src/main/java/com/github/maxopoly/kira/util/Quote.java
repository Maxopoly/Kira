package com.github.maxopoly.kira.util;

public class Quote {

	private final String quote;
	private final String author;

	public Quote(String quote, String author) {
		this.quote = quote;
		this.author = author;
	}

	public String getAuthor() {
		return author;
	}

	public String getQuote() {
		return quote;
	}

}
