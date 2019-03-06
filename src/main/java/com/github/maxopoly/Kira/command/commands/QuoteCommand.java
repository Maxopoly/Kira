package com.github.maxopoly.Kira.command.commands;

import com.github.maxopoly.Kira.command.Command;
import com.github.maxopoly.Kira.command.InputSupplier;
import com.github.maxopoly.Kira.util.Quote;
import com.github.maxopoly.Kira.util.QuoteHandler;

public class QuoteCommand extends Command {

	private QuoteHandler quoteHandler;

	public QuoteCommand() {
		super("quote", 0, 0, "advice");
		quoteHandler = new QuoteHandler();
	}

	@Override
	public String execute(InputSupplier supplier, String[] args) {
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

	@Override
	public String getUsage() {
		return "quote";
	}

	@Override
	public String getFunctionality() {
		return "Gives life advice";
	}

	@Override
	public String getRequiredPermission() {
		return "isauth";
	}

}
