package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        Map<String, Double> collect = data.stream()
                .collect(Collectors.groupingBy(
                        Measurement::name,
                        Collectors.summingDouble(Measurement::value)
                ));

        // Иначе коллекция возвращается в обратном порядке и тест не проходит =(
        // "{"val3":33.0,"val2":30.0,"val1":3.0}"
        return new TreeMap<>(collect);
    }
}
