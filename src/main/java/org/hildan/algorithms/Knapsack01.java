package org.hildan.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Knapsack01 {

    private final int capacity;

    private final Item[] items;

    private final int[][] maxValue;

    public Knapsack01(int capacity, Item[] items) {
        this.capacity = capacity;
        this.items = items;
        this.maxValue = new int[items.length + 1][capacity + 1];
    }

    public int solveMaxValue() {
        solveSubProblems();
        return maxValue[items.length][capacity];
    }

    public Solution solveWithItems() {
        solveSubProblems();

        int v = maxValue[items.length][capacity];
        int w = capacity;
        List<Item> solutionItems = new ArrayList<>();

        for (int i = items.length; i > 0 && v > 0; i--) {
            if (v != maxValue[i - 1][w]) {
                Item item = items[i - 1];
                solutionItems.add(item);
                // we remove items value and weight
                v -= item.value;
                w -= item.weight;
            }
        }

        return new Solution(this.maxValue[items.length][capacity], solutionItems);
    }

    private void solveSubProblems() {
        // 0 items always yields 0 value, no matter the capacity
        Arrays.fill(maxValue[0], 0);

        for (int i = 1; i <= items.length; i++) {
            Item item = items[i - 1];
            // item cannot be added if not enough capacity
            int[] prevRow = maxValue[i - 1];
            System.arraycopy(prevRow, 0, maxValue[i], 0, Math.min(item.weight, capacity));
            // item could be added, we can choose to add or not
            for (int c = item.weight; c <= capacity; c++) {
                int valueWithoutItem = prevRow[c];
                int valueWithItem = prevRow[c - item.weight] + item.value;
                maxValue[i][c] = Math.max(valueWithoutItem, valueWithItem);
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

        final int value;
        final List<Item> items;

        Solution(int value, List<Item> items) {
            this.value = value;
            this.items = items;
        }
    }
}
