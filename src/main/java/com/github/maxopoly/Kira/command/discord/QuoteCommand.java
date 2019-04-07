package com.github.maxopoly.Kira.command.discord;

import com.github.maxopoly.Kira.command.model.discord.Command;
import com.github.maxopoly.Kira.command.model.top.InputSupplier;
import com.github.maxopoly.Kira.util.Quote;
import com.github.maxopoly.Kira.util.QuoteHandler;

public class QuoteCommand extends Command {

	private QuoteHandler quoteHandler;

	public QuoteCommand() {
		super("quote", "advice");
		quoteHandler = new QuoteHandler();
	}

	@Override
	public String getFunctionality() {
		return "Gives life advice";
	}

	@Override
	public String getRequiredPermission() {
		return "isauth";
	}

	@Override
	public String getUsage() {
		return "quote";
	}

	@Override
	public String handleInternal(String argument, InputSupplier sender) {
		Quote quote;
		try {
		quote = quoteHandler.getQuote();
		}
		catch (Exception e) {
			e.printStackTrace();
			return "Sometimes things dont go the way we expect them to";
		}
		return quote.getQuote() + "\n" + " - " + quote.getAuthor(); 
	}

}
