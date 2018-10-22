package org.hildan.algorithms.knapsack;

import java.util.List;

public class KnapsackUnbounded {

    private final int capacity;

    private final Item[] items;

    private final int[] maxValue;

    public KnapsackUnbounded(int capacity, Item[] items) {
        this.capacity = capacity;
        this.items = items;
        this.maxValue = new int[capacity + 1];
    }

    public int solveMaxValue() {
        solveSubProblems();
        return maxValue[capacity];
    }

    private void solveSubProblems() {
        // 0 capacity yields 0 value
        maxValue[0] = 0;

        for (int c = 1; c <= capacity; c++) {
            maxValue[c] = 0;
            for (Item item : items) {
                if (item.weight <= c) {
                    maxValue[c] = Math.max(maxValue[c], maxValue[c - item.weight] + item.value);
                }
            }
        }
    }

    static class Item {
        final int value;
        final int weight;

        public Item(int value, int weight) {
            this.value = value;
            this.weight = weight;
        }
    }

    static class Solution {
        int value;
        List<Knapsack01.Item> items;

        public Solution(int value, List<Knapsack01.Item> items) {
            this.value = value;
            this.items = items;
        }
    }
}
