package com.quinn.interview.lettercombination.model;


/**
 * Model bean to store the mapping relation between digit and letters
 *
 * @author Qunhua.Liao
 * @since 2020-10-14
 */
public class DigitMap {

    DigitMap(int digit, char... letters) {
        this.digit = digit;
        this.letters = letters;
    }

    /**
     * the digit
     */
    private int digit;

    /**
     * the chars which the current digit maps
     */
    private char[] letters;

    public char[] getLetters() {
        return letters;
    }
}
