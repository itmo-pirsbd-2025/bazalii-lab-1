package com.itmo;

import java.util.stream.IntStream;

public final class ArraysCreator {
    public static int[] createArray(int length) {
        return IntStream.range(0, length)
                .map(i -> length - 1 - i)
                .toArray();
    }
}