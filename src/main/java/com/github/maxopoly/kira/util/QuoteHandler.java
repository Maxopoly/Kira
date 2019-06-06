package com.github.maxopoly.kira.util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class QuoteHandler {

	public Queue<Quote> quotes;

	public QuoteHandler() {
		quotes = new LinkedList<>();
	}

	public synchronized Quote getQuote() {
		if (quotes.isEmpty()) {
			refillQuotes();
		}
		return quotes.poll();
	}

	private void refillQuotes() {
		Document doc;
		try {
			doc = Jsoup.connect("http://www.quotationspage.com/random.php").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		Element el = doc.child(0).child(1).child(4).child(0).child(0).child(1).child(2);
		boolean isAuthor = false;
		String quote = null;
		String author = null;
		for(Element e : el.children()) {
			if (!isAuthor) {
				quote = e.child(0).text();
			}
			else {
				Element authorEl = e.child(1);
				if (authorEl.childNodeSize() == 0) {
					author = authorEl.text();
				}
				else {
					author = authorEl.child(0).text();
				}
				quotes.add(new Quote(quote, author));
			}
			isAuthor = !isAuthor;
		}
	}

}
