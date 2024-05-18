package ru.otus;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class CustomerService {
    private SortedMap<Customer, String> customers = new TreeMap<>(
            Comparator.comparing(Customer::getScores)
    );

    public Map.Entry<Customer, String> getSmallest() {
        var smallestScores = customers.firstEntry();

        return newEntry(smallestScores);
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        long scores = customer.getScores();

        for (Map.Entry<Customer, String> entry : customers.entrySet()) {
            if (entry.getKey().getScores() > scores) {
                return newEntry(entry);
            }

        }

        return null;
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
