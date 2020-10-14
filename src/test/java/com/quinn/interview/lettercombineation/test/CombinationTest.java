package com.quinn.interview.lettercombineation.test;

import com.quinn.interview.lettercombination.model.Dictionary;
import com.quinn.interview.util.BaseUtil;
import com.quinn.interview.util.PrintUtil;
import org.junit.Assert;
import org.junit.Test;

public class CombinationTest {

    /**
     * test null input
     */
    @Test
    public void nullInputTest() {
        Dictionary instance = Dictionary.getInstance();
        String[] combine = instance.combine(null);
        Assert.assertNull(combine);
    }

    /**
     * test input with element 0 lonely
     */
    @Test
    public void inputWith0Test() {
        int[] input = new int[]{0};

        Dictionary instance = Dictionary.getInstance();
        String[] combine = instance.combine(input);
        Assert.assertArrayEquals(combine, new String[0]);
    }

    /**
     * test input with element 1 lonely
     */
    @Test
    public void inputWith1Test() {
        int[] input = new int[]{1};

        Dictionary instance = Dictionary.getInstance();
        String[] combine = instance.combine(input);
        Assert.assertArrayEquals(combine, new String[0]);
    }

    /**
     * test input with elements more than one and without 1 or 0
     */
    @Test
    public void inputNormal02NTest() {
        int[] input = new int[]{2, 3};
        String[] expected = new String[]{"ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"};

        Dictionary instance = Dictionary.getInstance();
        String[] combine = instance.combine(input);

        PrintUtil.print(combine, "Result of CustomDictionaryTest.bothCustomAndStdDictionaryTestCase is:");
        Assert.assertTrue(BaseUtil.arrayHasSameElement(combine, expected));
    }

    /**
     * test input with one element except 1 or 0
     */
    @Test
    public void inputNormal01NTest() {
        int[] input = new int[]{9};
        String[] expected = new String[]{"w", "x", "y", "z"};

        Dictionary instance = Dictionary.getInstance();
        String[] combine = instance.combine(input);

        PrintUtil.print(combine, "Result of CustomDictionaryTest.bothCustomAndStdDictionaryTestCase is:");
        Assert.assertTrue(BaseUtil.arrayHasSameElement(combine, expected));
    }

    /**
     * test input with elements more than 1 with 1 or 0
     */
    @Test
    public void inputWith1ToNTest() {
        int[] input = new int[]{2, 1, 3, 0};
        String[] expected = new String[]{"ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"};

        Dictionary instance = Dictionary.getInstance();
        String[] combine = instance.combine(input);

        PrintUtil.print(combine, "Result of CustomDictionaryTest.bothCustomAndStdDictionaryTestCase is:");
        Assert.assertTrue(BaseUtil.arrayHasSameElement(combine, expected));
    }

    /**
     * test input and dictionary with additional elements
     */
    @Test
    public void inputWithAdditionalMapTest() {
        int[] input = new int[]{2, 10};
        String[] expected = new String[]{"a!", "a@", "a#", "b!", "b@", "b#", "c!", "c@", "c#"};

        Dictionary instance = Dictionary.copy(Dictionary.getInstance(), 11)
                .addDigitMap(10, '!', '@', '#');
        String[] combine = instance.combine(input);

        PrintUtil.print(combine, "Result of CustomDictionaryTest.bothCustomAndStdDictionaryTestCase is:");
        Assert.assertTrue(BaseUtil.arrayHasSameElement(combine, expected));
    }

}
