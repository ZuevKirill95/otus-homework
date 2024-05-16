package ru.otus;

import com.google.common.collect.Sets;

import java.util.Set;

public class HelloOtus {
    public static void main(String[] args) {
        Set<String> set1 = Set.of("s1", "s2", "s3");
        Set<String> set2 = Set.of("s2", "s3", "s4");

        Sets.SetView<String> difference = Sets.difference(set1, set2); // ["s1"]
        System.out.println(difference);
    }
}