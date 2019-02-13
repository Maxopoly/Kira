package com.github.maxopoly.Kira.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class WebUtil {

	public static String readPage(String stringURL) {
		//taken from https://stackoverflow.com/questions/238547/how-do-you-programmatically-download-a-webpage-in-java
	    InputStream is = null;
	    BufferedReader br;
	    String line;
	    StringBuilder sb = new StringBuilder();
	    try {
	    	URL url = new URL(stringURL);
	    	is = url.openStream();  // throws an IOException
	        br = new BufferedReader(new InputStreamReader(is));

	        while ((line = br.readLine()) != null) {
	            sb.append(line);
	        }
	    } catch (MalformedURLException mue) {
	         mue.printStackTrace();
	    } catch (IOException ioe) {
	         ioe.printStackTrace();
	    } finally {
	        try {
	            if (is != null) is.close();
	        } catch (IOException ioe) {
	            // nothing to see here
	        }
	    }
	    return sb.toString();
	}

}
