package com.jp.bakery.core;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import junit.framework.Assert;

class BakeryMainTest {

	private HashMap<String, Double> vsmap = new HashMap<>();
	private HashMap<String, Double> mbmap = new HashMap<>();
	private HashMap<String, Double> cfmap = new HashMap<>();
	
	
	@BeforeEach
	void setUp() throws Exception {
		vsmap.put("packOf3", 6.99);
		vsmap.put("packOf5", 8.99);

		mbmap.put("packOf2", 9.95);
		mbmap.put("packOf5", 16.95);
		mbmap.put("packOf8", 24.95);

		cfmap.put("packOf3", 5.95);
		cfmap.put("packOf5", 9.95);
		cfmap.put("packOf9", 16.99);		
		
	}

	@AfterEach
	void tearDown() throws Exception {
		vsmap = null;
		mbmap = null;
		cfmap = null;
	}

	@Test
	void testOrder001() {
		BakeryMain bkMain = new BakeryMain();
		int vsorder = 10;
		
		List<String> vslist = bkMain.packItemsAndReturn(vsmap, vsorder);		
		System.out.println("Vegemite Scroll list is  : "+vslist);
		
		String expectedvs = "Pack Of : 5 * 2 : valed at : 17.98";
		for(String vs: vslist) {
			Assert.assertEquals(expectedvs, vs);
		}
		
		int mborder = 14;
		
		List<String> mblist = bkMain.packItemsAndReturn(mbmap, mborder);
		System.out.println("Blueberry Muffin list is  : "+mblist);
		
		String expectedmb = "Pack Of : 8 * 1 : valed at : 24.95 and Pack Of : 2 * 3 : valued at : 29.849999999999998 == full price is : 54.8";
		for(String mb: mblist) {
			Assert.assertEquals(expectedmb, mb);
		}
		
		int cforder = 13;

		List<String> cflist = bkMain.packItemsAndReturn(cfmap, cforder);
		System.out.println("Croissant list is  : "+cflist);
		
		String expectedcf = "Pack Of : 5 * 2 : valed at : 19.9 and Pack Of : 3 * 1 : valued at : 5.95 == full price is : 25.849999999999998";
		for(String cf: cflist) {
			Assert.assertEquals(expectedcf, cf);
		}
	}
	

}
