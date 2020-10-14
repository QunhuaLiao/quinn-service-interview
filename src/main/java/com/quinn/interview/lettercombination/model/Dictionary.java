package com.quinn.interview.lettercombination.model;

import com.quinn.interview.util.BaseUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Structure to store all the mapping relations in the app
 *
 * @author Qunhua.Liao
 * @since 2020-10-14
 */
public class Dictionary {

    private volatile static Dictionary STANDARD = new Dictionary(10);

    static {
        STANDARD.digitMaps[0] = new DigitMap(0);
        STANDARD.digitMaps[1] = new DigitMap(1);
        STANDARD.digitMaps[2] = new DigitMap(2, 'a', 'b', 'c');
        STANDARD.digitMaps[3] = new DigitMap(3, 'd', 'e', 'f');
        STANDARD.digitMaps[4] = new DigitMap(4, 'g', 'h', 'i');
        STANDARD.digitMaps[5] = new DigitMap(5, 'j', 'k', 'l');
        STANDARD.digitMaps[6] = new DigitMap(6, 'm', 'n', 'o');
        STANDARD.digitMaps[7] = new DigitMap(7, 'p', 'q', 'r', 's');
        STANDARD.digitMaps[8] = new DigitMap(8, 't', 'u', 'v');
        STANDARD.digitMaps[9] = new DigitMap(9, 'w', 'x', 'y', 'z');
    }

    public static Dictionary getInstance() {
        return STANDARD;
    }

    private Dictionary(int size) {
        this.digitMaps = new DigitMap[size];
    }

    /**
     * all mapping relation of digit, the digit value is the index (position)
     */
    private DigitMap[] digitMaps;

    /**
     * create an empty Dictionary with init size
     *
     * @param size the Dictionary size
     * @return an empty Dictionary
     */
    public static Dictionary copy(Dictionary src, int size) {
        Dictionary dictionary = new Dictionary(size);
        dictionary.digitMaps = Arrays.copyOf(src.digitMaps, size);
        return dictionary;
    }

    /**
     * add mapping relation
     *
     * @param digit   the digit
     * @param letters the letters current digit maps
     * @return the dictionary itself to support chain operation
     */
    public Dictionary addDigitMap(int digit, char... letters) {
        if (digit > digitMaps.length) {
            throw new IllegalArgumentException("digit over flowed");
        }
        digitMaps[digit] = new DigitMap(digit, letters);
        return this;
    }

    /**
     * combine the mapping letter with all possible ways
     *
     * @param digits the source digits
     * @return all possible letter combinations
     */
    public String[] combine(int[] digits) {
        if (digits == null || digits.length == 0) {
            return null;
        }

        int num = 1;
        int size = 0;
        for (int digit : digits) {
            if (digit >= digitMaps.length) {
                throw new IllegalArgumentException("digit '" + digit + "' over flowed");
            }
            char[] letters = digitMaps[digit].getLetters();
            if (letters != null && letters.length > 1) {
                num *= letters.length;
                size++;
            }
        }

        List<StringBuilder> result = new ArrayList<>(num);
        combine(result, digits, size, 0);

        return result.parallelStream().map(StringBuilder::toString).toArray(String[]::new);
    }

    /**
     * recursion to combine and print
     *
     * @param digits source digits
     * @param size   the size of every query
     * @param index  the current index
     */
    private void combine(List<StringBuilder> result, int[] digits, int size, int index) {
        if (index >= digits.length) {
            return;
        }

        DigitMap digitMap = digitMaps[digits[index]];
        char[] letters;
        if (digitMap == null || BaseUtil.isEmpty(letters = digitMap.getLetters())) {
            combine(result, digits, size, ++index);
            return;
        }

        if (result.size() == 0) {
            for (char c : letters) {
                result.add(new StringBuilder(size).append(c));
            }
        } else {
            int currentSize = result.size();
            for (int i = 1; i < letters.length; i++) {
                for (int j = 0; j < currentSize; j++) {
                    result.add(new StringBuilder(size).append(result.get(j)).append(letters[i]));
                }
            }

            for (int j = 0; j < currentSize; j++) {
                result.get(j).append(letters[0]);
            }
        }
        combine(result, digits, size, ++index);
    }
}
