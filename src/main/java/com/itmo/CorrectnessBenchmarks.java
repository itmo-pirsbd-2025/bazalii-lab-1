package com.itmo;

import com.itmo.sorters.CleverSorter;
import com.itmo.sorters.NaiveSorter;

import java.util.Arrays;

public final class CorrectnessBenchmarks {
    private static final NaiveSorter NaiveSorter = new NaiveSorter();
    private static final CleverSorter CleverSorter = new CleverSorter();

    public static void runCorrectnessBenchmarks() {
        runCorrectnessBenchmark(NaiveSorter);
        runCorrectnessBenchmark(CleverSorter);
    }

    private static void runCorrectnessBenchmark(IParallelSorter sorter) {
        int[] reference = ArraysCreator.createArray(10_000);
        int[] input = ArraysCreator.createArray(10_000);
        Arrays.sort(reference);
        int[] sorted = sorter.sortArrayAsParallel(input);
        boolean isSorted = isSorted(sorted);
        boolean isEqual = Arrays.equals(reference, sorted);
        System.out.printf("Sorter %-7s => sorted=%s, matches reference=%s%n",
                sorter.getClass().getSimpleName(), isSorted, isEqual);
    }

    private static boolean isSorted(int[] array) {
        if (array == null || array.length < 2) {
            return true;
        }
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }
}