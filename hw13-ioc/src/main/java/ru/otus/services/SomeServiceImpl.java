package ru.otus.services;

import ru.otus.repository.SomeRepository1;
import ru.otus.repository.SomeRepository2;

public class SomeServiceImpl implements SomeService {
    private final SomeRepository1 someRepository1;
    private final SomeRepository2 someRepository2;
    private final GameProcessor gameProcessor;

    public SomeServiceImpl(SomeRepository1 someRepository1, SomeRepository2 someRepository2, GameProcessor gameProcessor) {
        this.someRepository1 = someRepository1;
        this.someRepository2 = someRepository2;
        this.gameProcessor = gameProcessor;
    }
}
