package org.hildan.algorithms.knapsack;

import org.hildan.algorithms.knapsack.KnapsackUnbounded.Item;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KnapsackUnboundedTest {

    @Test
    void solveMaxValue_noItemsIsZero() {
        Item[] items = {};
        KnapsackUnbounded knapsack = new KnapsackUnbounded(50, items);
        assertEquals(0, knapsack.solveMaxValue());
    }

    @Test
    void solveMaxValue_noCapacityIsZero() {
        Item[] items = { //
                new Item(100, 1), //
        };
        KnapsackUnbounded knapsack = new KnapsackUnbounded(0, items);
        assertEquals(0, knapsack.solveMaxValue());
    }

    @Test
    void solveMaxValue_pickOne() {
        Item[] items = { //
                new Item(30, 10), // ok, but there's better
                new Item(50, 10), // best one
        };
        KnapsackUnbounded knapsack = new KnapsackUnbounded(12, items);
        assertEquals(50, knapsack.solveMaxValue());
    }

    @Test
    void solveMaxValue_heavyHighValueItem() {
        Item[] items = {
                new Item(100, 100), // very nice, but too heavy
                new Item(1, 1), // should be taken 20 times
        };
        KnapsackUnbounded knapsack = new KnapsackUnbounded(20, items);
        assertEquals(20, knapsack.solveMaxValue());
    }

    @Test
    void solveMaxValue_optimizes() {
        Item[] items = { //
                new Item(30, 10), // should be left out
                new Item(70, 11), // should be left out
                new Item(50, 10), // should be taken twice
        };
        KnapsackUnbounded knapsack = new KnapsackUnbounded(20, items);
        assertEquals(100, knapsack.solveMaxValue());
    }
}
