package ru.otus.config;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.repository.SomeRepository1;
import ru.otus.repository.SomeRepository1Impl;
import ru.otus.repository.SomeRepository2;
import ru.otus.repository.SomeRepository2Impl;
import ru.otus.services.GameProcessor;
import ru.otus.services.SomeService;
import ru.otus.services.SomeServiceImpl;

@AppComponentsContainerConfig(order = 2)
public class AppConfig2 {

    @AppComponent(order = 0, name = "someRepository1")
    public SomeRepository1 someRepository1() {
        return new SomeRepository1Impl();
    }

    @AppComponent(order = 0, name = "someRepository2")
    public SomeRepository2 someRepository2() {
        return new SomeRepository2Impl();
    }

    @AppComponent(order = 1, name = "someService")
    public SomeService someService(
            SomeRepository1 someRepository1, SomeRepository2 someRepository2, GameProcessor gameProcessor) {
        return new SomeServiceImpl(someRepository1, someRepository2, gameProcessor);
    }
}
