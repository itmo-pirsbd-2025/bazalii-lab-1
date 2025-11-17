package com.itmo.sorters;

import com.itmo.IParallelSorter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CleverSorter implements IParallelSorter {

    @Override
    public int[] sortArrayAsParallel(int[] array) {
        if (array == null || array.length == 0) {
            return new int[0];
        }

        int maxElement = Arrays.stream(array).max().getAsInt();
        if (maxElement < 0) {
            maxElement = 0;
        }

        final int cores = Runtime.getRuntime().availableProcessors();
        final int oversubscriptionRate = 3;
        final int expectedNumberOfThreads = cores * oversubscriptionRate;
        final int numberOfPartitions = expectedNumberOfThreads < array.length ? expectedNumberOfThreads : 1;
        final int elementsInPartitionRangeSize = maxElement / numberOfPartitions + 1;

        List<List<Integer>> partitions = new ArrayList<>(numberOfPartitions);
        for (int i = 0; i < numberOfPartitions; i++) {
            partitions.add(new ArrayList<>());
        }

        for (int element : array) {
            var intervalStart = 0;
            var partitionNumber = 0;
            while (element < intervalStart || element > intervalStart + elementsInPartitionRangeSize) {
                intervalStart += elementsInPartitionRangeSize;
                partitionNumber++;
            }

            partitions
                    .get(partitionNumber)
                    .add(element);
        }

        ExecutorService executor = Executors.newFixedThreadPool(numberOfPartitions);
        List<Future<List<Integer>>> futures = new ArrayList<>(numberOfPartitions);
        for (final List<Integer> bucket : partitions) {
            futures.add(executor.submit(() -> {
                Collections.sort(bucket);

                return bucket;
            }));
        }
        executor.shutdown();

        List<Integer>[] sortedBuckets = new ArrayList[numberOfPartitions];
        for (int i = 0; i < futures.size(); i++) {
            try {
                var sorted = futures
                        .get(i)
                        .get();
                sortedBuckets[i] = sorted;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while sorting buckets", e);
            } catch (ExecutionException e) {
                throw new RuntimeException("Exception while sorting buckets", e.getCause());
            }
        }

        int[] result = new int[array.length];
        int pos = 0;
        for (List<Integer> sorted : sortedBuckets) {
            for (Integer currentElement : sorted) {
                result[pos] = currentElement;
                pos++;
            }
        }

        return result;
    }
}