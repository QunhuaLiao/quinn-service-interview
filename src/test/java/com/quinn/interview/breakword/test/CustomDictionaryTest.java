package com.quinn.interview.breakword.test;

import com.quinn.interview.breakword.enums.TrieNodeTypeEnum;
import com.quinn.interview.breakword.model.BreakResult;
import com.quinn.interview.breakword.model.Dictionary;
import com.quinn.interview.util.BaseUtil;
import com.quinn.interview.util.PrintUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for custom dictionary
 * the custom dictionary can be participation when create the dictionary by an array or a file
 * <p>
 * the only difference between the custom dictionary and standard dictionary is how to create the dictionary
 *
 * @author Qunhua.Liao
 * @since 2020-10-14
 */
public class CustomDictionaryTest {

    /**
     * test case that only use custom dictionary 1
     */
    @Test
    public void onlyCustomDictionaryTest() {
        String[] customDic = new String[]{"i", "like", "sam", "sung", "mobile", "icecream", "man go", "mango", "and"};
        Dictionary dictionary = Dictionary.create(TrieNodeTypeEnum.ARRAY, customDic, false);

        String input = "ilikeicecreamandmango";
        String[] expected = new String[]{"i like icecream and man go", "i like icecream and mango"};

        BreakResult result = dictionary.breakLine(input);
        String[] allCases = result.allCases();
        PrintUtil.print(allCases, "Result of CustomDictionaryTest.onlyCustomDictionaryTestCase is:");

        Assert.assertTrue(BaseUtil.arrayHasSameElement(expected, allCases));
    }

    /**
     * test case that only use custom dictionary 2
     */
    @Test
    public void onlyCustomDictionaryTest2() {
        String[] customDic = new String[]{"i", "like", "sam", "sung", "mobile", "icecream", "man go", "mango", "and"};
        Dictionary dictionary = Dictionary.create(TrieNodeTypeEnum.ARRAY, customDic, false);

        String input = "ilikesamsungmobile";
        String[] expected = new String[]{"i like sam sung mobile"};

        BreakResult result = dictionary.breakLine(input);
        String[] allCases = result.allCases();
        PrintUtil.print(allCases, "Result of CustomDictionaryTest.onlyCustomDictionaryTestCase2 is:");

        Assert.assertTrue(BaseUtil.arrayHasSameElement(expected, allCases));
    }

    /**
     * test case that use both custom dictionary and standard dictionary 1
     */
    @Test
    public void bothCustomAndStdDictionaryTest() {
        String[] customDic = new String[]{"i", "like", "sam", "sung", "mobile", "icecream", "man go", "mango", "and"};
        Dictionary dictionary = Dictionary.create(TrieNodeTypeEnum.ARRAY, customDic, true);

        String input = "ilikeicecreamandmango";
        String[] expected = new String[]{"i like icecream and man go", "i like icecream and mango", "i like ice cream and man go", "i like ice cream and mango"};

        BreakResult result = dictionary.breakLine(input);
        String[] allCases = result.allCases();
        PrintUtil.print(allCases, "Result of CustomDictionaryTest.bothCustomAndStdDictionaryTestCase is:");

        Assert.assertTrue(BaseUtil.arrayHasSameElement(expected, allCases));
    }

    /**
     * test case that use both custom dictionary and standard dictionary 2
     */
    @Test
    public void bothCustomAndStdDictionaryTest2() {
        String[] customDic = new String[]{"i", "like", "sam", "sung", "mobile", "icecream", "man go", "mango", "and"};
        Dictionary dictionary = Dictionary.create(TrieNodeTypeEnum.ARRAY, customDic, true);

        String input = "ilikesamsungmobile";
        String[] expected = new String[]{"i like sam sung mobile", "i like samsung mobile"};

        BreakResult result = dictionary.breakLine(input);
        String[] allCases = result.allCases();
        PrintUtil.print(allCases, "Result of CustomDictionaryTest.bothCustomAndStdDictionaryTestCase2 is:");

        Assert.assertTrue(BaseUtil.arrayHasSameElement(expected, allCases));
    }

}
