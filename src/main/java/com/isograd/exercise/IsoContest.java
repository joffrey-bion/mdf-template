package com.isograd.exercise;

import java.util.*;
import java.util.function.*;

public class IsoContest {

    public static void main(String[] argv) {
        Scanner sc = new Scanner(System.in);
        MdfReader reader = new MdfReader(sc);

        // TODO

    }

}

@SuppressWarnings("WeakerAccess")
class MdfReader {

    private final Scanner sc;

    MdfReader(Scanner sc) {
        this.sc = sc;
    }

    int readIntLine() {
        return Integer.parseInt(sc.nextLine());
    }

    int[] readIntArrayLine() {
        String line = sc.nextLine();
        String[] lineArray = line.split("\\s");
        int[] ints = new int[lineArray.length];
        for (int i = 0; i < lineArray.length; i++) {
            ints[i] = Integer.parseInt(lineArray[i]);
        }
        return ints;
    }

    <T> List<T> readNLinesAsList(int n, Function<String, T> itemReader) {
        List<T> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            list.add(itemReader.apply(sc.nextLine()));
        }
        return list;
    }

    <T> T[] readNLinesAsArray(int n, Function<String, T> itemReader, Function<Integer, T[]> arrayCreator) {
        T[] outArray = arrayCreator.apply(n);
        fillFromLines(itemReader, outArray);
        return outArray;
    }

    <T> void fillFromLines(Function<String, T> itemReader, T[] outArray) {
        for (int i = 0; i < outArray.length; i++) {
            outArray[i] = itemReader.apply(sc.nextLine());
        }
    }

    /**
     * Reads a line containing the number N of elements, and then reads N lines from the input as elements.
     */
    <T> List<T> readCountAndList(Function<String, T> itemReader) {
        int n = readIntLine();
        return readNLinesAsList(n, itemReader);
    }

    /**
     * Reads a line containing the number N of elements, and then reads N lines from the input as elements.
     */
    <T> T[] readCountAndArray(Function<String, T> itemReader, Function<Integer, T[]> arrayCreator) {
        int n = readIntLine();
        return readNLinesAsArray(n, itemReader, arrayCreator);
    }
}
