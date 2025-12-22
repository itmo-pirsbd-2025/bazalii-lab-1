package com.itmo;

import com.itmo.sorters.CleverSorter;
import com.itmo.sorters.NaiveSorter;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class Benchmarks {
    @Param({"100", "10000", "1000000"})
    public int size;

    private final NaiveSorter naiveSorter = new NaiveSorter();
    private final CleverSorter cleverSorter = new CleverSorter();
    private int[] baseline;
    private int[] invocationArray;

    @Setup(Level.Iteration)
    public void setupIteration() {
        baseline = ArraysCreator.createShuffledArrayOfPositiveNumbers(size);
        invocationArray = new int[size];
    }

    @Setup(Level.Invocation)
    public void setupInvocation() {
        System.arraycopy(baseline, 0, invocationArray, 0, size);
    }

    @Benchmark
    public void builtInSort(Blackhole blackhole) {
        Arrays.sort(invocationArray);
        blackhole.consume(invocationArray);
    }

    @Benchmark
    public void naiveParallelSort(Blackhole blackhole) {
        var result = naiveSorter.sortArrayAsParallel(invocationArray);
        blackhole.consume(result);
    }

    @Benchmark
    public void cleverParallelSort(Blackhole blackhole) {
        var result = cleverSorter.sortArrayAsParallel(invocationArray);
        blackhole.consume(result);
    }
}
