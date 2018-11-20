package com.jp.bakery.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * This Class takes the input order and provide the number of optimized packs
 * for each items
 * 
 * @author Jeyaprakash Ganesan
 *
 */
public class BakeryMain {

	private static Map<String, Double> vs5map = new HashMap<>();
	private static Map<String, Double> mb11map = new HashMap<>();
	private static Map<String, Double> cfmap = new HashMap<>();

	public BakeryMain() {

		vs5map.put("packOf3", 6.99);
		vs5map.put("packOf5", 8.99);

		mb11map.put("packOf2", 9.95);
		mb11map.put("packOf5", 16.95);
		mb11map.put("packOf8", 24.95);

		cfmap.put("packOf3", 5.95);
		cfmap.put("packOf5", 9.95);
		cfmap.put("packOf9", 16.99);

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

		BakeryMain bkMain = new BakeryMain();
		String vslist = bkMain.packItemsAndReturn(vs5map, requiredVS);
		System.out.println("VS5 list is : " + vslist);
		String mblist = bkMain.packItemsAndReturn(mb11map, requiredMB);
		System.out.println("MB11 list is : " + mblist);
		String cflist = bkMain.packItemsAndReturn(cfmap, requiredCF);
		System.out.println("CF list is : " + cflist);
	}

	/**
	 * @param input map to be sorted in reverse order
	 * @return sorted map
	 */
	private Map<String, Integer> sortPackMap(Map<String, Integer> input) {
		Map<String, Integer> result = input.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByKey())).collect(Collectors.toMap(
						Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

		return result;
	}

	/**
	 * @param map   sorted input map
	 * @param order number of items ordered
	 * @return intermediate map of optmized count of items
	 */
	private HashMap<String, Integer> putRawItemsIntoPack(Map<String, Double> map, int order) {
		HashMap<String, Integer> completePackage = new HashMap<>();
		map.forEach((k, v) -> {
			int itemsInPack = new Integer(k.substring(6, 7)).intValue();

			int modfororder = order % itemsInPack;
			int packCount = order / itemsInPack;
			completePackage.put(itemsInPack + ":" + packCount, modfororder);

		});

		return completePackage;

	}

	/**
	 * @param sortedPackage sorted input map
	 * @return removable entry in the map
	 */
	private List<Map.Entry<String, Integer>> getFinalItemsList(Map<String, Integer> sortedPackage, int order) {
		boolean isAllEntriesNeeded = false;
		List<Map.Entry<String, Integer>> finalItemsList = new ArrayList<>();
		Map.Entry<String, Integer> entryWithMaxValue = findEntryWithMaxValue(sortedPackage);
		String[] maxEntryTokens = entryWithMaxValue.getKey().split(":");
		int itemsInMaxEntry = new Integer(maxEntryTokens[0]).intValue();
		int maxValue = entryWithMaxValue.getValue();
		int maxPackCount = new Integer(maxEntryTokens[1]).intValue();
		int balanceItems = 0;

		finalItemsList.add(finalItemsList.size(), entryWithMaxValue);
		int totalAnalyzedItems = 0;
		for (Map.Entry<String, Integer> entry : sortedPackage.entrySet()) {

			if (entry.getValue() < maxValue) {

				String[] entryTokens = entry.getKey().split(":");
				int itemsInEntry = new Integer(entryTokens[0]).intValue();
				int packCount = new Integer(entryTokens[1]).intValue();
				balanceItems += itemsInEntry;
				totalAnalyzedItems = itemsInEntry * packCount;
				if (entry.getValue() > 0) {
					if (maxValue % itemsInEntry == 0) {
						finalItemsList.add(finalItemsList.size(), entry);
					} else {
						if (maxValue % balanceItems == 0 && balanceItems > itemsInEntry) {
							isAllEntriesNeeded = true;
							finalItemsList.remove(entryWithMaxValue);
						} else {
							if (totalAnalyzedItems + maxPackCount * itemsInMaxEntry > order) {
								if (maxValue % itemsInEntry > 0 && !sortedPackage.values().contains(0)) {
									finalItemsList.remove(entryWithMaxValue);
									if (!finalItemsList.contains(entry)) {
										finalItemsList.add(finalItemsList.size(), entry);
									}
								} else {
									finalItemsList.add(finalItemsList.size(), entry);
								}
							}
						}

					}
				} else {
					if (maxValue % itemsInEntry == 0) {
						if(finalItemsList.size() > 1) {
							finalItemsList.remove(1);
						}
						finalItemsList.add(finalItemsList.size(), entry);
					}
					else {
						if (packCount <= sortedPackage.size() && packCount * itemsInEntry == order) {
							finalItemsList.remove(entryWithMaxValue);
							finalItemsList.add(finalItemsList.size(), entry);							
							return finalItemsList;
						} else {
							finalItemsList.add(entry);
						}
					} 
				}

			} else if (maxValue == 0) {
				return finalItemsList;
			}
		}

		if (isAllEntriesNeeded) {
			for (Map.Entry<String, Integer> entry : sortedPackage.entrySet()) {
				if (!finalItemsList.contains(entry)) {
					finalItemsList.add(finalItemsList.size(), entry);
				}

			}
		}
		return finalItemsList;
	}

	/**
	 * @param map   map of input number of items in each pack and the price per pack
	 * @param order number of order from the customer
	 * @return a String of optimized output
	 */
	public String packItemsAndReturn(Map<String, Double> map, int order) {

		HashMap<String, Integer> completePackage = putRawItemsIntoPack(map, order);

		Map<String, Integer> sortedPackage = sortPackMap(completePackage);

		List<Map.Entry<String, Integer>> finalItemsList = getFinalItemsList(sortedPackage, order);
		
		Map.Entry<String, Integer> listEntryWithMaxValue = findEntryWithMaxValue(finalItemsList);
		String[] tokensInListMaxEntry = listEntryWithMaxValue.getKey().split(":");
		int itemsInMaxEntry = new Integer(tokensInListMaxEntry[0]).intValue();
		int packCountInMaxEntry = new Integer(tokensInListMaxEntry[1]).intValue();

		Double priceOfMax = map.get("packOf" + String.valueOf(itemsInMaxEntry));
		Double totalPriceOfMax = priceOfMax * packCountInMaxEntry;
		
		StringBuilder sbldr = new StringBuilder();
		sbldr.append("Pack Of : ");
		sbldr.append(itemsInMaxEntry);
		sbldr.append(" * ");
		sbldr.append(packCountInMaxEntry);
		sbldr.append(" : valued at : ");
		sbldr.append(totalPriceOfMax);
		
		Double finalPrice = totalPriceOfMax;
		if (finalItemsList.size() > 1) {
			int previousItemPackCount = 0;
			for (Map.Entry<String, Integer> entry : finalItemsList) {
				String[] tokensInEntry = entry.getKey().split(":");
				int itemsInEntry = new Integer(tokensInEntry[0]).intValue();
		
				int packCountInEntry = listEntryWithMaxValue.getValue() / itemsInEntry;
				if(packCountInEntry < 1 && previousItemPackCount > 0 ) {
					String[] diffTokens = listEntryWithMaxValue.getKey().split(":");
					int diffItems = new Integer(diffTokens[0]).intValue();
					packCountInEntry = ((order - itemsInMaxEntry * packCountInMaxEntry) % (diffItems * previousItemPackCount) )/itemsInEntry;
				}
				if (itemsInEntry < itemsInMaxEntry) {					
					Double price = map.get("packOf" + String.valueOf(itemsInEntry));
					Double totalPrice = price * packCountInEntry;
					
					sbldr.append(" and ");
					sbldr.append("Pack Of : ");
					sbldr.append(itemsInEntry);
					sbldr.append(" * ");
					sbldr.append(packCountInEntry);
					sbldr.append(" : valued at : ");
					sbldr.append(totalPrice);
					finalPrice += totalPrice;
					listEntryWithMaxValue = entry;
					previousItemPackCount = packCountInEntry;
				}				
			}
		}
		
		sbldr.append(" == Full Price : ");
		sbldr.append(finalPrice);
		
		return sbldr.toString();
	}

	/**
	 * @param map input map
	 * @return entry with the maxValue in the map
	 */
	private Map.Entry<String, Integer> findEntryWithMaxValue(Map<String, Integer> map) {
		Map.Entry<String, Integer> maxEntry = null;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
				maxEntry = entry;
			}
		}
		return maxEntry;
	}

	/**
	 * @param map input map
	 * @return entry with the maxValue in the map
	 */
	private Map.Entry<String, Integer> findEntryWithMaxValue(List<Map.Entry<String, Integer>> list) {
		Map.Entry<String, Integer> maxEntry = null;
		for (Map.Entry<String, Integer> entry : list) {
			if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
				maxEntry = entry;
			}
		}
		return maxEntry;
	}

}