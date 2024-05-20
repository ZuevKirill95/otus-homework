package ru.otus;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {
    private final NavigableMap<Customer, String> customers = new TreeMap<>(
            Comparator.comparing(Customer::getScores)
    );

    public Map.Entry<Customer, String> getSmallest() {
        var smallestScores = customers.firstEntry();

        return newEntry(smallestScores);
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        var next = customers.higherEntry(customer);

        return next == null ? null : newEntry(next);
    }

    private static AbstractMap.SimpleEntry<Customer, String> newEntry(Map.Entry<Customer, String> entry) {
        var id = entry.getKey().getId();
        var name = entry.getKey().getName();
        var scores = entry.getKey().getScores();
        var newCustomer = new Customer(id, name, scores);

        return new AbstractMap.SimpleEntry<>(newCustomer, entry.getValue());
    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }
}
