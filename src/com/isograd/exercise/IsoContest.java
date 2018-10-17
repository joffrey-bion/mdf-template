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

    private static <T> List<T> readSizedList(Scanner sc, Function<String, T> itemReader) {
        int n = readIntLine(sc);
        return readNLines(sc, n, itemReader);
    }

    private static <T> void fillFromLines(Scanner sc, T[] outArray, Function<String, T> itemReader) {
        for (int i = 0; i < outArray.length; i++) {
            outArray[i] = itemReader.apply(sc.nextLine());
        }
    }
}
