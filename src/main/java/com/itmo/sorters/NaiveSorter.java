package com.itmo.sorters;

import com.itmo.IParallelSorter;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class NaiveSorter implements IParallelSorter {

    @Override
    public int[] sortArrayAsParallel(int[] array) {
        if (array == null || array.length == 0) {
            return new int[0];
        }

        final int cores = Runtime.getRuntime().availableProcessors();
        final int oversubscriptionRate = 3;
        int partitionSize = array.length / (cores * oversubscriptionRate);
        if (partitionSize <= 0) {
            partitionSize = 1;
        }

        List<int[]> segments = new ArrayList<>();
        for (int start = 0; start < array.length; start += partitionSize) {
            int end = Math.min(start + partitionSize, array.length);
            int[] part = Arrays.copyOfRange(array, start, end);
            segments.add(part);
        }

        Queue<int[]> queue = new ConcurrentLinkedQueue<>();

        int sortThreads = Math.min(segments.size(), cores * oversubscriptionRate);
        ExecutorService sortExecutor = Executors.newFixedThreadPool(sortThreads);
        for (final int[] part : segments) {
            sortExecutor.submit(() -> {
                Arrays.sort(part);
                queue.add(part);
            });
        }

        sortExecutor.shutdown();

        try {
            if (!sortExecutor.awaitTermination(100, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Sorting phase did not finish in time.");
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while sorting partitions", ie);
        }

        ExecutorService mergeExecutor = Executors.newFixedThreadPool(sortThreads);
        AtomicInteger workingMerges = new AtomicInteger(0);

        while (true) {
            if (queue.size() == 1 && workingMerges.get() == 0) {
                break;
            }

            if (queue.size() >= 2) {
                final int[] first = queue.poll();
                final int[] second = queue.poll();

                workingMerges.incrementAndGet();
                mergeExecutor.submit(() -> {
                    int[] merged = mergeArrays(first, second);
                    queue.add(merged);
                    workingMerges.decrementAndGet();
                });
            } else {
                try {
                    Thread.sleep(Duration.ofNanos(10));
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        mergeExecutor.shutdown();

        try {
            if (!mergeExecutor.awaitTermination(100, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Merge phase did not finish in time.");
            }
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        }

        return queue.poll();
    }

    private static int[] mergeArrays(int[] firstArray, int[] secondArray) {
        int[] result = new int[firstArray.length + secondArray.length];
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