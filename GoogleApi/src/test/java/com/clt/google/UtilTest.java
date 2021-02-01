package com.clt.google;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.Test;
import com.clt.util.Utility;

public class UtilTest {
	
	@Test
	public void getProps() throws IOException {
		assertEquals("ASIA", Utility.getProperty("LOCATION"));
	}
	
	@Test
	public void getDateFromTimestamp() throws IOException {
		long ts =1611626169864L;
		System.out.println(Utility.getDateFromTimestamp(ts));
	}
	
	@Test
	public void getMimeType() throws IOException {
		System.out.println(Utility.getMimeType("P:/2021012965422301.doc"));
	}
}
