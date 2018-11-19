package com.jp.bakery.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BakeryMain {

	public BakeryMain() {

	}

	public static void main(String[] args) {
		int requiredVS = 10;
		int requiredMB = 14;
		int requiredCF = 13;
		System.out.println("Enter the number of Vegemite Scroll: ");
		Scanner scanner = new Scanner(System.in);
		try {
			
			requiredVS = scanner.nextInt();
			System.out.println("Enter the Blueberry Muffin: ");
			requiredMB = scanner.nextInt();
			System.out.println("Enter the Croissant: ");
			requiredCF = scanner.nextInt();
			
		} finally {
			scanner.close();
		}		
		Map<String, Double> vs5map = new HashMap<>();
		Map<String, Double> mb11map = new HashMap<>();
		Map<String, Double> cfmap = new HashMap<>();
		
		vs5map.put("packOf3", 6.99);
		vs5map.put("packOf5", 8.99);

		mb11map.put("packOf2", 9.95);
		mb11map.put("packOf5", 16.95);
		mb11map.put("packOf8", 24.95);

		cfmap.put("packOf3", 5.95);
		cfmap.put("packOf5", 9.95);
		cfmap.put("packOf9", 16.99);
		
		BakeryMain bkMain = new BakeryMain();
		List<String> vslist = bkMain.packItemsAndReturn(vs5map, requiredVS);
		System.out.println("VS5 list is : "+vslist);
		List<String> mblist = bkMain.packItemsAndReturn(mb11map, requiredMB);
		System.out.println("MB11 list is : "+mblist);
		List<String> cflist = bkMain.packItemsAndReturn(cfmap, requiredCF);
		System.out.println("CF list is : "+cflist);
	}

	private Map<String, Double> sortMap(Map<String, Double> input) {
		Map<String, Double> result = input.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByKey())).collect(Collectors.toMap(
						Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		
		return result;
	}

	private Map<String, Integer> sortPackMap(Map<String, Integer> input) {
		Map<String, Integer> result = input.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByKey())).collect(Collectors.toMap(
						Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		
		return result;
	}

	private HashMap<String, Integer> putRawItemsIntoPack(Map<String, Double> map, int order) {
		HashMap<String, Integer> completePackage = new HashMap<>();
		map.forEach((k, v) -> {
			int itemsInPack = new Integer(k.substring(6, 7)).intValue();

			int modmb11 = order % itemsInPack;
			int packCount = order / itemsInPack;
			completePackage.put(itemsInPack + ":" + packCount, modmb11);

		});

		return completePackage;

	}

	public Map.Entry<String, Integer> getRemovableEntry(Map<String, Integer> sortedPackage) {
		boolean isRemovable = true;
		Map.Entry<String, Integer> removableEntry = null;
		for (Map.Entry<String, Integer> entry : sortedPackage.entrySet()) {
			String[] entryTokens = entry.getKey().split(":");
			int itemsInEntry = new Integer(entryTokens[0]).intValue();
			Map.Entry<String, Integer> entryWithMaxValue = maxValueInBundle(sortedPackage);
			int maxValue = entryWithMaxValue.getValue();
			if (maxValue % itemsInEntry > 0) {
				isRemovable = true;
				removableEntry = entryWithMaxValue;
			} else {
				isRemovable = false;
			}
		}

		if (isRemovable) {
			return removableEntry;
		} else {
			return null;
		}
	}

	public List<String> packItemsAndReturn(Map<String, Double> map, int order) {

		List<String> fullPackList = new ArrayList<String>();

		HashMap<String, Integer> completePackage = putRawItemsIntoPack(map, order);

		Map<String, Integer> sortedPackage = sortPackMap(completePackage);

		Map.Entry<String, Integer> removableEntry = getRemovableEntry(sortedPackage);
		Map.Entry<String, Integer> entryWithMaxValue = maxValueInBundle(sortedPackage);

		// one of the values is zero, so the maxvalue will not be removed from the
		// actual package
		if (removableEntry == null) {
			Map.Entry<String, Integer> entry = getEntryForValueFromMap(sortedPackage, entryWithMaxValue.getValue());

			sortedPackage.remove(entry.getKey(), entry.getValue());

		} else {
			sortedPackage.remove(removableEntry.getKey(), removableEntry.getValue());
		}
		
		Map.Entry<String, Integer> entryWithLatestMaxValue = maxValueInBundle(sortedPackage);
		
		String[] tokensInMaxEntry = entryWithLatestMaxValue.getKey().split(":");
		int itemsInMaxEntry = new Integer(tokensInMaxEntry[0]).intValue();
		int packCountInMaxEntry = new Integer(tokensInMaxEntry[1]).intValue();
		
		Double priceOfMax = map.get("packOf"+String.valueOf(itemsInMaxEntry));
		Double totalPriceOfMax = priceOfMax * packCountInMaxEntry;
		
		if(entryWithLatestMaxValue.getValue() > 0) {
			int packCount = 0;
			int itemsInEntry = 0;
			for (Map.Entry<String, Integer> entry : sortedPackage.entrySet()) {
				
				if (!entry.getKey().equals(entryWithLatestMaxValue.getKey())) {
					String[] tokens = entry.getKey().split(":");
					itemsInEntry = new Integer(tokens[0]).intValue();				
					packCount = entryWithLatestMaxValue.getValue() / itemsInEntry;
				}
			}

			Double price = map.get("packOf"+String.valueOf(itemsInEntry));
			Double totalPrice = price * packCount;
			fullPackList.add("Pack Of : "+itemsInMaxEntry+" * "+packCountInMaxEntry+" : valed at : "+totalPriceOfMax+" and Pack Of : "+itemsInEntry+" * "+packCount+" : valued at : "+totalPrice + " == full price is : "+(totalPriceOfMax+totalPrice));
		}
		else {
			fullPackList.add("Pack Of : "+itemsInMaxEntry+" * "+packCountInMaxEntry+" : valed at : "+totalPriceOfMax);
		}
		
		return fullPackList;
	}

	private HashMap<String, Integer> getPackageMap(Map<String, Double> map, int cf) {
		HashMap<String, Integer> singleItemPack = new HashMap<>();
		map.forEach((k, v) -> {
			int itemsInPack = new Integer(k.substring(6, 7)).intValue();
			int modcf = cf % itemsInPack;
			int packCount = cf / itemsInPack;
			singleItemPack.put(itemsInPack + ":" + packCount, modcf);
		});
		return singleItemPack;
	}

	private Map.Entry<String, Integer> maxValueInBundle(Map<String, Integer> map) {
		Map.Entry<String, Integer> maxEntry = null;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
				maxEntry = entry;
			}
		}
		return maxEntry;
	}

	private Map.Entry<String, Integer> getEntryForValueFromMap(Map<String, Integer> map, Integer maxValue) {
		Map.Entry<String, Integer> entryForValue = null;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			int toBeRemoved = entry.getValue();
			if (entryForValue == null && toBeRemoved != maxValue && toBeRemoved != 0) {
				entryForValue = entry;
			}
		}
		return entryForValue;
	}
}
