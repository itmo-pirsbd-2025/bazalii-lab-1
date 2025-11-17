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
@Fork(1)
@Warmup(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 500, timeUnit = TimeUnit.MILLISECONDS)
public class Benchmarks {
    @Param({"100", "10000", "1000000"})
    public int size;

    private final NaiveSorter naiveSorter = new NaiveSorter();
    private final CleverSorter cleverSorter = new CleverSorter();
    private int[] baselineData;

    @Setup(Level.Iteration)
    public void setup() {
        baselineData = ArraysCreator.createArray(size);
    }

    @Benchmark
    public void builtInSort(Blackhole blackhole) {
        int[] copy = Arrays.copyOf(baselineData, baselineData.length);
        Arrays.sort(copy);
        blackhole.consume(copy);
    }

    @Benchmark
    public void naiveParallelSort(Blackhole blackhole) {
        int[] copy = Arrays.copyOf(baselineData, baselineData.length);
        int[] result = naiveSorter.sortArrayAsParallel(copy);
        blackhole.consume(result);
    }

    @Benchmark
    public void cleverParallelSort(Blackhole blackhole) {
        int[] copy = Arrays.copyOf(baselineData, baselineData.length);
        int[] result = cleverSorter.sortArrayAsParallel(copy);
        blackhole.consume(result);
    }
}
