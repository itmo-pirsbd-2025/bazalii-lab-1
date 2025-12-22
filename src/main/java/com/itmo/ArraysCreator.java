package com.itmo;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class ArraysCreator {
    public static int[] createShuffledArrayOfPositiveNumbers(int length) {
        var list = IntStream
                .range(0, length)
                .map(i -> length - 1 - i)
                .boxed()
                .collect(Collectors.toList());

        return CreateShuffledArray(list);
    }

    public static int[] createShuffledArrayOfNegativeNumbers(int length) {
        var list = IntStream
                .range(0, length)
                .map(i -> -(length - 1 - i))
                .boxed()
                .collect(Collectors.toList());

        return CreateShuffledArray(list);
    }

    public static int[] createShuffledArrayWithPositivesAndNegatives(int length) {
        var array = new int[length];

        var rnd = ThreadLocalRandom.current();
        for (var i = 0; i < length; i++) {
            array[i] = rnd.nextBoolean()
                    ? i
                    : -i;
        }

        return array;
    }

    private static int[] CreateShuffledArray(List<Integer> input) {
        Collections.shuffle(input);

        return input
                .stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }
}