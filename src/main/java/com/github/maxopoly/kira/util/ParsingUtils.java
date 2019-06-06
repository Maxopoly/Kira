package com.github.maxopoly.kira.util;

import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class ParsingUtils {
	
	private static String getSuffix(String arg, Predicate<Character> selector) {
		StringBuilder number = new StringBuilder();
		for (int i = arg.length() - 1; i >= 0; i--) {
			if (selector.test(arg.charAt(i))) {
				number.insert(0, arg.substring(i, i + 1));
			} else {
				break;
			}
		}
		return number.toString();
	}

	public static long parseTime(String input) {
		input = input.replace(" ", "").replace(",", "").toLowerCase();
		long result = 0;
		try {
			result += Long.parseLong(input);
			return result;
		} catch (NumberFormatException e) {
		}
		while (!input.equals("")) {
			String typeSuffix = getSuffix(input, a -> Character.isLetter(a));
			input = input.substring(0, input.length() - typeSuffix.length());
			String numberSuffix = getSuffix(input, a -> Character.isDigit(a));
			input = input.substring(0, input.length() - numberSuffix.length());
			long duration;
			if (numberSuffix.length() == 0) {
				duration = 1;
			} else {
				duration = Long.parseLong(numberSuffix);
			}
			switch (typeSuffix) {
			case "ms":
			case "milli":
			case "millis":
				result += duration;
				break;
			case "s": // seconds
			case "sec":
			case "second":
			case "seconds":
				result += TimeUnit.SECONDS.toMillis(duration);
				break;
			case "m": // minutes
			case "min":
			case "minute":
			case "minutes":
				result += TimeUnit.MINUTES.toMillis(duration);
				break;
			case "h": // hours
			case "hour":
			case "hours":
				result += TimeUnit.HOURS.toMillis(duration);
				break;
			case "d": // days
			case "day":
			case "days":
				result += TimeUnit.DAYS.toMillis(duration);
				break;
			case "w": // weeks
			case "week":
			case "weeks":
				result += TimeUnit.DAYS.toMillis(duration * 7);
				break;
			case "month": // weeks
			case "months":
				result += TimeUnit.DAYS.toMillis(duration * 30);
				break;
			case "never":
			case "inf":
			case "infinite":
			case "perm":
			case "perma":
			case "forever":
				// 1000 years counts as perma
				result += TimeUnit.DAYS.toMillis(365 * 1000);
			default:
				// just ignore it
			}
		}
		return result;
	}

	private ParsingUtils() {
		//private constructor for static help class
	}

}
