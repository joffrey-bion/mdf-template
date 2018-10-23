package org.hildan.algorithms.words;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WordsTest {

    @Test
    void levenshtein() {
        assertEquals(1, Words.levenshtein("a", "b"));
        assertEquals(1, Words.levenshtein("a", "aa"));
        assertEquals(1, Words.levenshtein("aa", "a"));
        assertEquals(2, Words.levenshtein("b", "abc"));
        assertEquals(2, Words.levenshtein("short", "shop"));
    }
}
