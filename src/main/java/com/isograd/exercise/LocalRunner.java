package com.isograd.exercise;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LocalRunner {

    private static final String testFilesDir = "C:\\Users\\jbion\\projects\\mdf-template\\test-files";

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";

    public static void main(String[] args) throws IOException {
        List<TestCase> testCases = findTestCases();

        PrintStream stdout = System.out;

        List<TestCase> failedTests = new ArrayList<>();
        for (TestCase testCase : testCases) {
            stdout.println("Test case: " + testCase);
            File actualOutputFile = runProgram(testCase.input);
            if (sameContent(testCase.expectedOutput, actualOutputFile)) {
                stdout.println(ANSI_GREEN + "  OK" + ANSI_RESET);
                if (!actualOutputFile.delete()) {
                    stdout.println("Could not delete " + actualOutputFile.getName());
                }
            } else {
                stdout.println(ANSI_RED + "  FAILED" + ANSI_RESET);
                failedTests.add(testCase);
            }
        }
        if (!failedTests.isEmpty()) {
            stdout.println(ANSI_RED + "\nThere were some failed tests:");
            failedTests.forEach(stdout::println);
            stdout.print(ANSI_RESET);
            System.exit(1);
        }
    }

    private static List<TestCase> findTestCases() {
        File dir = new File(testFilesDir);
        if (!dir.isDirectory()) {
            throw new IllegalStateException("Test files directory not found: " + testFilesDir);
        }
        File[] inputFiles = dir.listFiles((d, name) -> name.startsWith("input") && !name.endsWith("out"));
        File[] outputFiles = dir.listFiles((d, name) -> name.startsWith("output"));
        assert inputFiles != null;
        assert outputFiles != null;

        return Arrays.stream(inputFiles)
                     .map(in -> new TestCase(in, getExpectedOutputFile(outputFiles, in)))
                     .collect(Collectors.toList());
    }

    private static File getExpectedOutputFile(File[] outputFiles, File inputFile) {
        String suffix = getEndOfName(inputFile);
        for (File outputFile : outputFiles) {
            if (getEndOfName(outputFile).equals(suffix)) {
                return outputFile;
            }
        }
        throw new IllegalArgumentException("Corresponding output file not found for input: " + inputFile.getName());
    }

    private static File runProgram(File inputFile) throws IOException {
        File actualOutputFile = getActualOutputFile(inputFile);
        try ( //
              FileInputStream in = new FileInputStream(inputFile); //
              PrintStream out = new PrintStream(actualOutputFile) //
        ) {
            System.setIn(in);
            System.setOut(out);
            IsoContest.main(new String[0]);
            return actualOutputFile;
        }
    }

    private static File getActualOutputFile(File inputFile) {
        return new File(inputFile.getAbsolutePath() + ".out");
    }

    private static boolean sameContent(File file1, File file2) throws IOException {
        List<String> expectedLines = Files.readAllLines(file1.toPath());
        List<String> actualLines = Files.readAllLines(file2.toPath());
        return expectedLines.equals(actualLines);
    }

    private static String getEndOfName(File file) {
        String name = file.getName();
        return name.substring(name.length() - 5);
    }

    private static class TestCase {
        final File input;
        final File expectedOutput;

        private TestCase(File input, File expectedOutput) {
            this.input = input;
            this.expectedOutput = expectedOutput;
        }

        @Override
        public String toString() {
            return input.getName();
        }
    }
}
