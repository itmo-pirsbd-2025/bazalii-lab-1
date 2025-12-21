package com.itmo.sorters;

import com.itmo.ParallelSorter;

import java.util.List;
import java.util.ArrayList;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntArrays;


import static com.itmo.sorters.Constants.OversubscriptionRate;

public final class CleverSorter implements ParallelSorter {

    @Override
    public int[] sortArrayAsParallel(int[] array) {
        if (array.length == 0) {
            return new int[0];
        }

        var minElement = Integer.MAX_VALUE;
        var maxElement = Integer.MIN_VALUE;
        for (var v : array) {
            if (v < minElement) {
                minElement = v;
                continue;
            }

            if (v > maxElement) {
                maxElement = v;
            }
        }

        final var cores = Runtime.getRuntime().availableProcessors();
        final var expectedNumberOfThreads = cores * OversubscriptionRate;
        final var numberOfPartitions = expectedNumberOfThreads < array.length ? expectedNumberOfThreads : 1;
        final var range = (long) maxElement - (long) minElement;
        final var elementsInPartitionRangeSize = range / numberOfPartitions + 1L;

        List<IntArrayList> partitions = new ArrayList<>(numberOfPartitions);
        for (var i = 0; i < numberOfPartitions; i++) {
            partitions.add(new IntArrayList());
        }

        for (var element : array) {
            var shifted = (long) element - (long) minElement;
            var partitionNumber = (int) (shifted / elementsInPartitionRangeSize);
            if (partitionNumber < 0) {
                partitionNumber = 0;
            } else if (partitionNumber >= numberOfPartitions) {
                partitionNumber = numberOfPartitions - 1;
            }

            partitions
                    .get(partitionNumber)
                    .add(element);
        }

        partitions
                .parallelStream()
                .forEach(bucket -> {
                    bucket.trim();
                    IntArrays.quickSort(bucket.elements(), 0, bucket.size());
                });

        var result = new int[array.length];
        var pos = 0;

        for (var partition : partitions) {
            var size = partition.size();
            System.arraycopy(partition.elements(), 0, result, pos, size);
            pos += size;
        }

        return result;
    }
}