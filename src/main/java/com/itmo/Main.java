package com.itmo;

public class Main {
    public static void main(String[] args) {
        System.out.println("Running correctness benchmarks...");
        CorrectnessBenchmarks.runCorrectnessBenchmarks();
        System.out.println();
        System.out.println("Running performance benchmarks...");
        CorrectnessBenchmarks.runCorrectnessBenchmarks();
    }
}