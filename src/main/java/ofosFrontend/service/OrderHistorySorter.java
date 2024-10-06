package ofosFrontend.service;
import ofosFrontend.model.OrderHistory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderHistorySorter {

    public static Map<Integer, List<OrderHistory>> sortOrderHistoryByDate(Map<Integer, List<OrderHistory>> orderHistoryMap) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Create a list of the map's entries and sort them by the most recent date in each order history list
        List<Map.Entry<Integer, List<OrderHistory>>> entries = new ArrayList<>(orderHistoryMap.entrySet());

        entries.sort((entry1, entry2) -> {
            LocalDate date1 = LocalDate.parse(entry1.getValue().get(0).getOrderDate(), formatter);
            LocalDate date2 = LocalDate.parse(entry2.getValue().get(0).getOrderDate(), formatter);
            return date2.compareTo(date1);
        });

        Map<Integer, List<OrderHistory>> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, List<OrderHistory>> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
