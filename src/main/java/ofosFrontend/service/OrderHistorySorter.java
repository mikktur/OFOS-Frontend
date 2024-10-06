package ofosFrontend.service;

import ofosFrontend.model.OrderHistory;

import java.util.*;

public class OrderHistorySorter {

    public static Map<Integer, List<OrderHistory>> sortOrderHistoryById(Map<Integer, List<OrderHistory>> orderHistoryMap) {

        // Create a list of the map's entries and sort them by the order ID (map key)
        List<Map.Entry<Integer, List<OrderHistory>>> entries = new ArrayList<>(orderHistoryMap.entrySet());

        entries.sort((entry1, entry2) -> entry2.getKey().compareTo(entry1.getKey()));

        Map<Integer, List<OrderHistory>> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, List<OrderHistory>> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
