package com.itmo;

import static org.assertj.core.api.Assertions.assertThat;

import com.itmo.sorters.CleverSorter;
import com.itmo.sorters.NaiveSorter;
import org.junit.jupiter.api.Test;

public class SortersTests {

    @Test
    void NaiveSorterSortArrayAsParallel_SortShuffledArrayOfPositiveNumbers_ArrayIsCorrectlySorted() {
        // Arrange
        var input = ArraysCreator.createShuffledArrayOfPositiveNumbers(10_000);
        var sorter = new NaiveSorter();

        // Act
        var sorted = sorter.sortArrayAsParallel(input);

        // Assert
        assertThat(sorted).isSorted();
    }

    @Test
    void NaiveSorterSortArrayAsParallel_SortShuffledArrayOfNegativeNumbers_ArrayIsCorrectlySorted() {
        // Arrange
        var input = ArraysCreator.createShuffledArrayOfNegativeNumbers(10_000);
        var sorter = new NaiveSorter();

        // Act
        var sorted = sorter.sortArrayAsParallel(input);

        // Assert
        assertThat(sorted).isSorted();
    }

    @Test
    void NaiveSorterSortArrayAsParallel_SortShuffledArrayOfPositivesNegatives_ArrayIsCorrectlySorted() {
        // Arrange
        var input = ArraysCreator.createShuffledArrayWithPositivesAndNegatives(10_000);
        var sorter = new NaiveSorter();

        // Act
        var sorted = sorter.sortArrayAsParallel(input);

        // Assert
        assertThat(sorted).isSorted();
    }

    @Test
    void CleverSorterSortArrayAsParallel_SortShuffledArrayOfPositiveNumbers_ArrayIsCorrectlySorted() {
        // Arrange
        var input = ArraysCreator.createShuffledArrayOfPositiveNumbers(10_000);
        var sorter = new CleverSorter();

        // Act
        var sorted = sorter.sortArrayAsParallel(input);

        // Assert
        assertThat(sorted).isSorted();
    }

    @Test
    void CleverSorterSortArrayAsParallel_SortShuffledArrayOfNegativeNumbers_ArrayIsCorrectlySorted() {
        // Arrange
        var input = ArraysCreator.createShuffledArrayOfNegativeNumbers(10_000);
        var sorter = new CleverSorter();

        // Act
        var sorted = sorter.sortArrayAsParallel(input);

        // Assert
        assertThat(sorted).isSorted();
    }

    @Test
    void CleverSorterSortArrayAsParallel_SortShuffledArrayOfPositivesNegatives_ArrayIsCorrectlySorted() {
        // Arrange
        var input = ArraysCreator.createShuffledArrayWithPositivesAndNegatives(10_000);
        var sorter = new CleverSorter();

        // Act
        var sorted = sorter.sortArrayAsParallel(input);

        // Assert
        assertThat(sorted).isSorted();
    }
}
