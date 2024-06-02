# Домашнее задание по GC

## Вычисляем оптимальный размер heap

### 1 запуск

```
-Xms256m
-Xmx256m
```

Результат:

```
Exception in thread "main" java.lang.OutOfMemoryError: Пространство кучи Java
	at java.base/java.util.Arrays.copyOf(Arrays.java:3544)
	at java.base/java.util.Arrays.copyOf(Arrays.java:3513)
	at java.base/java.util.ArrayList.grow(ArrayList.java:237)
	at java.base/java.util.ArrayList.grow(ArrayList.java:244)
	at java.base/java.util.ArrayList.add(ArrayList.java:483)
	at java.base/java.util.ArrayList.add(ArrayList.java:496)
	at ru.calculator.Summator.calc(Summator.java:16)
	at ru.calculator.CalcDemo.main(CalcDemo.java:23)
```

### 2 запуск

```
-Xms2048m 
-Xmx2048m
```

Результат:

```
spend msec:7583, sec:7
```

### 3 запуск

Пробуем увеличить с шагом 256m

```
-Xms2304m 
-Xms2304m 
```

Результат:

```
spend msec:6245, sec:6
```

### 4 запуск

Пробуем увеличить с шагом 256m

```
-Xms2560m 
-Xms2560m 
```

Результат:

```
spend msec:6379, sec:6
```

### Вывод

Скорость выполнения не уменьшилась при последнем запуске, поэтому оставляем предыдущее значение `2304m`

## Оптимизация

До оптимизации

```
spend msec:6379, sec:6
```

Заменил `Integer` на `int` в классах `Data` и `Summator`

Результат

```
spend msec:1711, sec:1
```

После оптимизации скорость выполнения увеличилась в **6 раз**.