package org.hildan.algorithms.knapsack;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import org.hildan.algorithms.knapsack.Knapsack01.Item;
import org.hildan.algorithms.knapsack.Knapsack01.Solution;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Knapsack01Test {

    @Test
    void solveMaxValue_noItemsIsZero() {
        Item[] items = {};
        Knapsack01 knapsack = new Knapsack01(50, items);
        assertEquals(0, knapsack.solveMaxValue());
    }

    @Test
    void solveMaxValue_noCapacityIsZero() {
        Item[] items = { //
                new Item(100, 1), //
        };
        Knapsack01 knapsack = new Knapsack01(0, items);
        assertEquals(0, knapsack.solveMaxValue());
    }

    @Test
    void solveMaxValue_pickOne() {
        Item[] items = { //
                new Item(30, 10), // ok, but there's better
                new Item(50, 10), // best one
        };
        Knapsack01 knapsack = new Knapsack01(12, items);
        assertEquals(50, knapsack.solveMaxValue());
    }

    @Test
    void solveMaxValue_heavyHighValueItem() {
        Item[] items = {
                new Item(100, 100), // very nice, but too heavy
                new Item(1, 1),
        };
        Knapsack01 knapsack = new Knapsack01(20, items);
        assertEquals(1, knapsack.solveMaxValue());
    }

    @Test
    void solveMaxValue_optimizes() {
        Item[] items = { //
                new Item(30, 10), // part of the solution
                new Item(70, 11), // should be left out
                new Item(50, 10), // part of the solution
        };
        Knapsack01 knapsack = new Knapsack01(20, items);
        assertEquals(80, knapsack.solveMaxValue());
    }

    @Test
    void solveWithItems_noItemsIsZero() {
        Item[] items = {};
        Knapsack01 knapsack = new Knapsack01(50, items);
        Solution expected = new Solution(0, Collections.emptyList());
        assertEquals(expected, knapsack.solveWithItems());
    }

    @Test
    void solveWithItems_noCapacityIsZero() {
        Item[] items = {new Item(100, 1)};
        Knapsack01 knapsack = new Knapsack01(0, items);
        Solution expected = new Solution(0, Collections.emptyList());
        assertEquals(expected, knapsack.solveWithItems());
    }

    @Test
    void solveWithItems_pickOne() {
        Item cheap = new Item(30, 10);
        Item best = new Item(50, 10);
        Item[] items = {cheap, best};
        Knapsack01 knapsack = new Knapsack01(12, items);

        Solution expected = new Solution(50, Collections.singletonList(best));
        assertEquals(expected, knapsack.solveWithItems());
    }

    @Test
    void solveWithItems_heavyHighValueItem() {
        Item hugeStatue = new Item(100, 100);
        Item marble = new Item(1, 1);
        Item[] items = {hugeStatue, marble};
        Knapsack01 knapsack = new Knapsack01(20, items);

        Solution expected = new Solution(1, Collections.singletonList(marble));
        assertEquals(expected, knapsack.solveWithItems());
    }

    @Test
    void solveWithItems_optimizes() {
        Item soloItem = new Item(70, 11);
        Item combinable1 = new Item(30, 10);
        Item combinable2 = new Item(50, 10);
        Item[] items = {combinable1, soloItem, combinable2};
        Knapsack01 knapsack = new Knapsack01(20, items);

        Solution expected = new Solution(80, Arrays.asList(combinable1, combinable2));
        Solution actual = knapsack.solveWithItems();
        actual.items.sort(Comparator.comparingInt(i -> i.value));
        assertEquals(expected, actual);
    }
}
