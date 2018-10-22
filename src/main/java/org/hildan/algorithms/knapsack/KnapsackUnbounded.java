package org.hildan.algorithms.knapsack;

class KnapsackUnbounded {

    private final int capacity;

    private final Item[] items;

    private final int[] maxValue;

    KnapsackUnbounded(int capacity, Item[] items) {
        this.capacity = capacity;
        this.items = items;
        this.maxValue = new int[capacity + 1];
    }

    int solveMaxValue() {
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

        Item(int value, int weight) {
            this.value = value;
            this.weight = weight;
        }
    }
}
