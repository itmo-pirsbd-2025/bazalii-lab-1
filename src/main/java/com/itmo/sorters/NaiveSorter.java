package com.itmo.sorters;

import com.itmo.ParallelSorter;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static com.itmo.sorters.Constants.OversubscriptionRate;

public final class NaiveSorter implements ParallelSorter {

    @Override
    public int[] sortArrayAsParallel(int[] array) {
        if (array.length == 0) {
            return new int[0];
        }

        final var cores = Runtime.getRuntime().availableProcessors();
        var partitionSize = array.length / (cores * OversubscriptionRate);
        if (partitionSize <= 0) {
            partitionSize = 1;
        }

        List<int[]> segments = new ArrayList<>();
        for (var start = 0; start < array.length; start += partitionSize) {
            var end = Math.min(start + partitionSize, array.length);
            var part = Arrays.copyOfRange(array, start, end);
            segments.add(part);
        }

        var queue = segments
                .parallelStream()
                .peek(Arrays::sort)
                .toArray(int[][]::new);

        while (queue.length > 1) {
            var pairs = queue.length / 2;
            var remainder = queue.length % 2;

            var next = new int[pairs + remainder][];

            var current = queue;
            IntStream
                    .range(0, pairs)
                    .parallel()
                    .forEach(i -> next[i] = mergeArrays(current[2 * i], current[2 * i + 1]));

            if (remainder == 1) {
                next[pairs] = current[current.length - 1];
            }

            queue = next;
        }

        return queue[0];
    }

    private static int[] mergeArrays(int[] firstArray, int[] secondArray) {
        var result = new int[firstArray.length + secondArray.length];
        int firstArrayIndex = 0, secondArrayIndex = 0, resultIndex = 0;
        while (firstArrayIndex < firstArray.length || secondArrayIndex < secondArray.length) {
            if (firstArrayIndex == firstArray.length) {
                result[resultIndex++] = secondArray[secondArrayIndex++];
            } else if (secondArrayIndex == secondArray.length) {
                result[resultIndex++] = firstArray[firstArrayIndex++];
            } else if (firstArray[firstArrayIndex] <= secondArray[secondArrayIndex]) {
                result[resultIndex++] = firstArray[firstArrayIndex++];
            } else {
                result[resultIndex++] = secondArray[secondArrayIndex++];
            }
        }

        return result;
    }
}