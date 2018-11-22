package com.jp.bakery.core;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This is test class, that test the different incoming order for the Bakery packing algorithm
 * @author Jeyaprakash Ganesan
 *
 */
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
		
		String vspack = bkMain.packItemsAndReturn(vsmap, vsorder);		
		String expectedvs = "Pack Of : 5 * 2 : valued at : 17.98 == Full Price : 17.98";		
		Assert.assertEquals(vspack, expectedvs);
		
		int mborder = 14;
		
		String mbpack = bkMain.packItemsAndReturn(mbmap, mborder);		
		String expectedmb = "Pack Of : 8 * 1 : valued at : 24.95 and Pack Of : 2 * 3 : valued at : 29.849999999999998 == Full Price : 54.8";		
		Assert.assertEquals(expectedmb, mbpack);
		
		int cforder = 13;

		String cfpack = bkMain.packItemsAndReturn(cfmap, cforder);
		String expectedcf = "Pack Of : 5 * 2 : valued at : 19.9 and Pack Of : 3 * 1 : valued at : 5.95 == Full Price : 25.849999999999998";
		
		Assert.assertEquals(expectedcf, cfpack);
		
		System.out.println("Test Order 001 Success");
	}
	
	@Test
	void testOrder002() {
		BakeryMain bkMain = new BakeryMain();
		
		int vsorder = 13;
		
		String vspack = bkMain.packItemsAndReturn(vsmap, vsorder);		
		String expectedvs="Pack Of : 5 * 2 : valued at : 17.98 and Pack Of : 3 * 1 : valued at : 6.99 == Full Price : 24.97";		
		Assert.assertEquals(vspack, expectedvs);
		
		int mborder = 15;
		
		String mbpack = bkMain.packItemsAndReturn(mbmap, mborder);		
		String expectedmb = "Pack Of : 5 * 3 : valued at : 50.849999999999994 == Full Price : 50.849999999999994";
		Assert.assertEquals(mbpack, expectedmb);
		
		int cforder = 17;

		String cfpack = bkMain.packItemsAndReturn(cfmap, cforder);		
		String expectedcf = "Pack Of : 9 * 1 : valued at : 16.99 and Pack Of : 5 * 1 : valued at : 9.95 and Pack Of : 3 * 1 : valued at : 5.95 == Full Price : 32.89";
		Assert.assertEquals(cfpack, expectedcf);
		
		System.out.println("Test Order 002 Success");
	}

}
