package com.github.maxopoly.Kira.util;

import java.util.function.Predicate;

public class ParsingUtils {

	public static long parseTime(String input) {
		input = input.replace(" ", "").toLowerCase();
		long result = 0;
		boolean set = true;
		try {
			result += Long.parseLong(input);
		} catch (NumberFormatException e) {
			set = false;
		}
		if (set) {
			return result;
		}
		while (!input.equals("")) {
			String typeSuffix = getSuffix(input, a -> Character.isLetter(a));
			input = input.substring(0, input.length() - typeSuffix.length());
			String numberSuffix = getSuffix(input, a -> Character.isDigit(a));
			input = input.substring(0, input.length() - numberSuffix.length());
			long duration;
			if (numberSuffix.length() == 0) {
				duration = 0;
			} else {
				duration = Long.parseLong(numberSuffix);
			}
			switch (typeSuffix) {
			case "s": // seconds
			case "sec":
			case "second":
			case "seconds":
				result += 1000 * duration;
				break;
			case "m": // minutes
			case "min":
			case "minute":
			case "minutes":
				result += 60 * 1000 * duration;
				break;
			case "h": // hours
			case "hour":
			case "hours":
				result += 60 * 60 * 1000 * duration;
				break;
			case "d": // days
			case "day":
			case "days":
				result += 24 * 60 * 60 * 1000 * duration;
				break;
			case "w": // weeks
			case "week":
			case "weeks":
				result += 7 * 24 * 60 * 60 * 1000 * duration;
				break;
			case "month": // weeks
			case "months":
				result += 30 * 24 * 60 * 60 * 1000 * duration;
				break;
			case "never":
			case "inf":
			case "infinite":
			case "perm":
			case "perma":
				// 1000 years counts as perma
				result += 1000 * 365 * 24 * 60 * 60 * 1000;
			default:
				// just ignore it
			}
		}
		return result;
	}

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

}
