package com.github.maxopoly.kira.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import com.github.maxopoly.kira.KiraMain;

public class WebUtil {

	public static String readPage(String stringURL) {
		// based on
		// https://stackoverflow.com/questions/238547/how-do-you-programmatically-download-a-webpage-in-java
		URL url;
		try {
			url = new URL(stringURL);
		} catch (MalformedURLException e) {
			KiraMain.getInstance().getLogger().error("Malformed URL", e);
			return "";
		}
		String line;
		StringBuilder sb = new StringBuilder();
		try (InputStream is = url.openStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr)) {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException ioe) {
			KiraMain.getInstance().getLogger().error("Failed to read web page", ioe);
		}
		return sb.toString();
	}

}
