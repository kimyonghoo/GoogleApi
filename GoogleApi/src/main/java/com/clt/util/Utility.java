package com.clt.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.helpers.UtilLoggingLevel;

public class Utility {
	public static String getProperty(String key) {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream("properties/application.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return props.getProperty(key);
	}

	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat(Utility.getProperty("YMD"));
		Calendar c1 = Calendar.getInstance();
		return sdf.format(c1.getTime());
	}

	public static String getDateFromTimestamp(long timestamp) throws IOException {
		Timestamp ts = new Timestamp(timestamp);
		Date date = new Date(ts.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat(Utility.getProperty("YMDHMS"));
		return sdf.format(date);
	}

	public static String getMimeType(String filePath) throws IOException {
		Path source = Paths.get(filePath);
		String mimeType = Files.probeContentType(source);
		return mimeType;
	}

	public static String getRandomString(String DATA, int length) {
		SecureRandom random = new SecureRandom();
		
		if (length < 1)
			throw new IllegalArgumentException("length must be a positive number.");
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(DATA.charAt(random.nextInt(DATA.length())));
		}
		return sb.toString();
	}
}
