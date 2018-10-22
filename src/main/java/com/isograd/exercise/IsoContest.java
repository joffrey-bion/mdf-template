package com.isograd.exercise;

import java.util.*;
import java.util.function.Function;

public class IsoContest {

    public static void main(String[] argv) {
        Scanner sc = new Scanner(System.in);

        // TODO

    }

    private static int readIntLine(Scanner sc) {
        return Integer.parseInt(sc.nextLine());
    }

    private static <T> List<T> readNLines(Scanner sc, int n, Function<String, T> itemReader) {
        List<T> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            list.add(itemReader.apply(sc.nextLine()));
        }
        return list;
    }

    private static <T> T[] readNLines(Scanner sc, int n, Function<String, T> itemReader,
            Function<Integer, T[]> arrayCreator) {
        T[] outArray = arrayCreator.apply(n);
        fillFromLines(sc, itemReader, outArray);
        return outArray;
    }

    private static <T> void fillFromLines(Scanner sc, Function<String, T> itemReader, T[] outArray) {
        for (int i = 0; i < outArray.length; i++) {
            outArray[i] = itemReader.apply(sc.nextLine());
        }
    }

    /**
     * Reads a line containing the number N of elements, and then reads N lines from the input as elements.
     */
    private static <T> List<T> readList(Scanner sc, Function<String, T> itemReader) {
        int n = readIntLine(sc);
        return readNLines(sc, n, itemReader);
    }

    /**
     * Reads a line containing the number N of elements, and then reads N lines from the input as elements.
     */
    private static <T> T[] readArray(Scanner sc, Function<String, T> itemReader,
            Function<Integer, T[]> arrayCreator) {
        int n = readIntLine(sc);
        return readNLines(sc, n, itemReader, arrayCreator);
    }
}
