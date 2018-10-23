package com.isograd.exercise;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.*;

public class IsoContest {

    public static void main(String[] argv) {
        Scanner sc = new Scanner(System.in);
        MdfReader reader = new MdfReader(sc);

        // TODO

        System.out.println();
    }

}

@SuppressWarnings({"WeakerAccess", "unused"})
class MdfReader {

    final Scanner sc;

    MdfReader(Scanner sc) {
        this.sc = sc;
    }

    int readIntLine() {
        return Integer.parseInt(sc.nextLine());
    }

    int[] readIntArrayLine() {
        String line = sc.nextLine();
        return parseIntArray(line);
    }

    static int[] parseIntArray(String spacedNumbers) {
        String[] lineArray = spacedNumbers.split("\\s");
        int[] ints = new int[lineArray.length];
        for (int i = 0; i < lineArray.length; i++) {
            ints[i] = Integer.parseInt(lineArray[i]);
        }
        return ints;
    }

    <T> List<T> readNLinesAsList(int n, Function<MdfReader, T> itemReader) {
        List<T> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            list.add(itemReader.apply(this));
        }
        return list;
    }

    <T> T[] readNLinesAsArray(int n, Function<MdfReader, T> itemReader, Function<Integer, T[]> arrayCreator) {
        T[] outArray = arrayCreator.apply(n);
        fillFromLines(itemReader, outArray);
        return outArray;
    }

    <T> void fillFromLines(Function<MdfReader, T> itemReader, T[] outArray) {
        for (int i = 0; i < outArray.length; i++) {
            outArray[i] = itemReader.apply(this);
        }
    }

    /**
     * Reads a line containing the number N of elements, and then reads N lines from the input as a list.
     */
    <T> List<T> readCountAndList(Function<MdfReader, T> itemReader) {
        int n = readIntLine();
        return readNLinesAsList(n, itemReader);
    }

    /**
     * Reads a line containing the number N of elements, and then reads N lines from the input as an array.
     */
    <T> T[] readCountAndArray(Function<MdfReader, T> itemReader, Function<Integer, T[]> arrayCreator) {
        int n = readIntLine();
        return readNLinesAsArray(n, itemReader, arrayCreator);
    }
}

@SuppressWarnings({"WeakerAccess", "unused"})
class MdfUtils {

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    static int min(List<Integer> ints) {
        return ints.stream().reduce(Integer::min).get();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    static int max(List<Integer> ints) {
        return ints.stream().reduce(Integer::max).get();
    }

    static int sum(List<Integer> ints) {
        return ints.stream().reduce(0, Integer::sum);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    static double avg(List<Integer> ints) {
        return ints.stream().mapToInt(i -> i).average().getAsDouble();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    static double variance(List<Integer> ints) {
        double avg = avg(ints);
        return ints.stream().mapToDouble(i -> i * i - avg).average().getAsDouble();
    }

    static double stddev(List<Integer> ints) {
        return Math.sqrt(variance(ints));
    }

    /**
     * Find all keys mapped to the min value of the given map.
     */
    static <K, V extends Comparable<V>> List<K> findMin(Map<K, V> map, V maxValue) {
        List<K> minKeys = new ArrayList<>();
        V minValue = maxValue;
        for (Entry<K, V> entry : map.entrySet()) {
            K k = entry.getKey();
            V v = entry.getValue();
            int comp = v.compareTo(minValue);
            if (comp < 0) {
                minValue = v;
                minKeys.clear();
                minKeys.add(k);
            } else if (comp == 0) {
                minKeys.add(k);
            }
        }
        return minKeys;
    }

    /**
     * Find all keys mapped to the max value of the given map.
     */
    static <K, V extends Comparable<V>> List<K> findMax(Map<K, V> map, V minValue) {
        List<K> maxKeys = new ArrayList<>();
        V maxValue = minValue;
        for (Entry<K, V> entry : map.entrySet()) {
            K k = entry.getKey();
            V v = entry.getValue();
            int comp = v.compareTo(maxValue);
            if (comp > 0) {
                maxValue = v;
                maxKeys.clear();
                maxKeys.add(k);
            } else if (comp == 0) {
                maxKeys.add(k);
            }
        }
        return maxKeys;
    }

    static <T> Map<T, Integer> occurrences(List<T> elements) {
        Map<T, Integer> occurrences = new HashMap<>();
        elements.forEach(e -> occurrences.merge(e, 1, Integer::sum));
        return occurrences;
    }
}
