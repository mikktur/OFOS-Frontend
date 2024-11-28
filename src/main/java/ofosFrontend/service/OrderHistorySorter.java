package ofosFrontend.service;

import ofosFrontend.model.OrderHistory;

import java.util.*;

public final class OrderHistorySorter {


    private OrderHistorySorter() {
    }
    public static Map<Integer, List<OrderHistory>> sortOrderHistoryById(Map<Integer, List<OrderHistory>> orderHistoryMap, boolean ascending) {
        List<Map.Entry<Integer, List<OrderHistory>>> entries = new ArrayList<>(orderHistoryMap.entrySet());
        entries.sort((entry1, entry2) -> ascending
                ? entry1.getKey().compareTo(entry2.getKey())
                : entry2.getKey().compareTo(entry1.getKey()));

        return toLinkedHashMap(entries);
    }


    public static Map<Integer, List<OrderHistory>> sortOrderHistoryByRestaurant(Map<Integer, List<OrderHistory>> orderHistoryMap, boolean ascending) {
        List<Map.Entry<Integer, List<OrderHistory>>> entries = new ArrayList<>(orderHistoryMap.entrySet());
        entries.sort((entry1, entry2) -> {
            String restaurant1 = entry1.getValue().get(0).getRestaurantName();
            String restaurant2 = entry2.getValue().get(0).getRestaurantName();
            return ascending ? restaurant1.compareTo(restaurant2) : restaurant2.compareTo(restaurant1);
        });

        return toLinkedHashMap(entries);
    }

    public static Map<Integer, List<OrderHistory>> sortOrderHistoryByPrice(Map<Integer, List<OrderHistory>> orderHistoryMap, boolean ascending) {
        List<Map.Entry<Integer, List<OrderHistory>>> entries = new ArrayList<>(orderHistoryMap.entrySet());
        entries.sort((entry1, entry2) -> {
            double totalPrice1 = entry1.getValue().stream().mapToDouble(o -> o.getOrderPrice() * o.getQuantity()).sum();
            double totalPrice2 = entry2.getValue().stream().mapToDouble(o -> o.getOrderPrice() * o.getQuantity()).sum();
            return ascending ? Double.compare(totalPrice1, totalPrice2) : Double.compare(totalPrice2, totalPrice1);
        });

        return toLinkedHashMap(entries);
    }

    public static Map<Integer, List<OrderHistory>> sortOrderHistoryByDate(Map<Integer, List<OrderHistory>> orderHistoryMap, boolean ascending) {
        List<Map.Entry<Integer, List<OrderHistory>>> entries = new ArrayList<>(orderHistoryMap.entrySet());
        entries.sort((entry1, entry2) -> {
            String date1 = entry1.getValue().get(0).getOrderDate();
            String date2 = entry2.getValue().get(0).getOrderDate();
            return ascending ? date1.compareTo(date2) : date2.compareTo(date1);
        });

        return toLinkedHashMap(entries);
    }

    private static Map<Integer, List<OrderHistory>> toLinkedHashMap(List<Map.Entry<Integer, List<OrderHistory>>> entries) {
        Map<Integer, List<OrderHistory>> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, List<OrderHistory>> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}
