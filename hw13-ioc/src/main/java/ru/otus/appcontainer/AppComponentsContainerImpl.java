package ru.otus.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.exceptions.ContainerException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(String packageName) {
        Set<Class<?>> allClassesInPackage = findAllClassesInPackage(packageName);

        List<Class<?>> configs = allClassesInPackage.stream()
                .filter(config -> config.isAnnotationPresent(AppComponentsContainerConfig.class))
                .toList();

        List<Class<?>> configsSortedByOrder = configs.stream()
                .filter(config -> config.isAnnotationPresent(AppComponentsContainerConfig.class))
                .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponentsContainerConfig.class).order()))
                .toList();

        for (Class<?> configClass : configsSortedByOrder) {
            processConfig(configClass);
        }
    }

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        checkConfigClass(initialConfigClass);

        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClass) {
        for (Class<?> configClass : initialConfigClass) {
            checkConfigClass(configClass);
        }

        List<Class<?>> configsSortedByOrder = Arrays.stream(initialConfigClass)
                .filter(config -> config.isAnnotationPresent(AppComponentsContainerConfig.class))
                .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponentsContainerConfig.class).order()))
                .toList();

        for (Class<?> configClass : configsSortedByOrder) {
            processConfig(configClass);
        }
    }

    public static Set<Class<?>> findAllClassesInPackage(String packageName) {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        return reflections.getSubTypesOf(Object.class)
                .stream()
                .collect(Collectors.toSet());
    }

    private void processConfig(Class<?> configClass) {

        List<Method> sortedMethodByOrder = Arrays.stream(configClass.getMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                .toList();

        sortedMethodByOrder.forEach(method -> {
            AppComponent annotation = method.getAnnotation(AppComponent.class);
            String name = annotation.name();
            int order = annotation.order();
            Object config = createConfigClass(configClass);

            if (appComponentsByName.containsKey(name)) {
                throw new ContainerException("duplicate app component name: " + name);
            }

            Object component;
            if (order == 0) {
                component = createComponent(method, config);
            } else {
                List<Object> componentParams = new ArrayList<>();
                Parameter[] parameters = method.getParameters();
                for (Parameter parameter : parameters) {
                    Class<?> type = parameter.getType();

                    Object componentParam = appComponents.stream().filter(
                            appComponent -> type.isAssignableFrom(appComponent.getClass())
                    ).findAny().orElseThrow(() -> new ContainerException("No component found for " + type));

                    componentParams.add(componentParam);
                }

                component = createComponent(method, config, componentParams.toArray());
            }

            appComponentsByName.put(name, component);
            appComponents.add(component);
        });
    }

    private Object createComponent(Method method, Object config, Object... args) {
        try {
            return method.invoke(config, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ContainerException("Error creating component " + method.getName(), e);
        }
    }

    private static Object createConfigClass(Class<?> configClass) {
        try {
            return configClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ContainerException("Error creating config " + configClass, e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> components = appComponents.stream()
                .filter(component -> componentClass.isAssignableFrom(component.getClass()))
                .toList();

        if (components.isEmpty()) {
            throw new ContainerException("No component found for class " + componentClass.getName());
        }

        if (components.size() > 1) {
            throw new ContainerException("Multiple component found for class " + componentClass.getName());
        }

        return (C) components.getFirst();
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        Object component = appComponentsByName.get(componentName);

        if (component == null) {
            throw new ContainerException("No component found for name " + componentName);
        }

        return (C) component;
    }
}
