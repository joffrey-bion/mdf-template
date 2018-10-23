package com.isograd.exercise;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.*;

public class IsoContest {

    static class Color {
        int V;

        int R;

        int J;

        int O;

        public Color(int[] colors) {
            V = colors[0];
            R = colors[1];
            J = colors[2];
            O = colors[3];
        }

        private static Color zero() {
            return new Color(new int[] {0, 0, 0, 0});
        }

        public Color plus(Color c) {
            return new Color(new int[] {V + c.V, R + c.R, J + c.J, O + c.O});
        }

        public Color minus(Color c) {
            return new Color(new int[] {
                    Math.max(0, V - c.V), Math.max(0, R - c.R), Math.max(0, J - c.J), Math.max(0, O - c.O)
            });
        }

        public Color times(int dayTotal) {
            return new Color(new int[] {
                    V * dayTotal, R * dayTotal, J * dayTotal, O * dayTotal
            });
        }
    }

    public static void main(String[] argv) {
        Scanner sc = new Scanner(System.in);
        MdfReader reader = new MdfReader(sc);

        Color ref = new Color(reader.readIntArrayLine());
        List<Color> stockPerDay = reader.readNLinesAsList(7, r -> new Color(r.readIntArrayLine()));

        int total = 0;

        for (int i = 0; i < stockPerDay.size(); i++) {
            Color oldStock = i > 0 ? stockPerDay.get(i - 1) : Color.zero();
            Color newStock = stockPerDay.get(i);
            Color stock = oldStock.plus(newStock);
            int dayTotal = count(stock, ref);
            total += dayTotal;
            Color consumed = ref.times(dayTotal);
            Color remainingFreshStock = newStock.minus(consumed.minus(oldStock));
            stockPerDay.set(i, remainingFreshStock);
        }

        System.out.println(total);
    }

    static int count(Color stock, Color ref) {
        int maxV = ref.V == 0 ? Integer.MAX_VALUE : (stock.V / ref.V);
        int maxR = ref.R == 0 ? Integer.MAX_VALUE : (stock.R / ref.R);
        int maxJ = ref.J == 0 ? Integer.MAX_VALUE : (stock.J / ref.J);
        int maxO = ref.O == 0 ? Integer.MAX_VALUE : (stock.O / ref.O);
        return Math.min(Math.min(maxV, maxR), Math.min(maxJ, maxO));
    }

    static class Solver {

        Map<Color, Integer> cache = new HashMap<>();

        static int solve(Color stock, Color ref) {
            int maxV = stock.V / ref.V;
            int maxR = stock.R / ref.R;
            int maxJ = stock.J / ref.J;
            int maxO = stock.O / ref.O;
            return Math.min(Math.min(maxV, maxR), Math.min(maxJ, maxO));
        }
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
