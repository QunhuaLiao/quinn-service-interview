package com.quinn.interview.breakword.test;

import com.quinn.interview.breakword.enums.TrieNodeTypeEnum;
import com.quinn.interview.breakword.model.BreakResult;
import com.quinn.interview.breakword.model.Dictionary;
import com.quinn.interview.util.BaseUtil;
import com.quinn.interview.util.PrintUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for standard dictionary
 * the standard dictionary is
 * {"i", "like", "sam", "sung", "samsung", "mobile", "ice", "cream", "man go", "and"}
 * so:
 * input "ilikeicecreamandmango" has one case: "i like ice cream and man go"
 * <p>
 * input "ilikesamsungmobile" has two cases:
 * "i like samsung mobile"，"i like sam sung mobile"
 */
public class StandardDictionaryTest {

    /**
     * test case with input parameter that can produce one solution
     */
    @Test
    public void nullInputTest() {
        Dictionary dictionary = Dictionary.create(TrieNodeTypeEnum.ARRAY);
        BreakResult result = dictionary.breakLine(null);
        Assert.assertNull(result);
    }

    /**
     * test case with input parameter that can produce one solution
     */
    @Test
    public void emptyInputTest() {
        Dictionary dictionary = Dictionary.create(TrieNodeTypeEnum.ARRAY);
        String[] expected = new String[]{""};

        BreakResult result = dictionary.breakLine("");
        String[] allCases = result.allCases();
        PrintUtil.print(allCases, "Result of StandardDictionaryTest.oneCaseTest is:");

        Assert.assertTrue(BaseUtil.arrayHasSameElement(expected, allCases));
    }

    /**
     * test case with input parameter that can produce one solution
     */
    @Test
    public void oneCaseTest() {
        Dictionary dictionary = Dictionary.create(TrieNodeTypeEnum.ARRAY);

        String input = "ilikeicecreamandmango";
        String[] expected = new String[]{"i like ice cream and man go"};

        BreakResult result = dictionary.breakLine(input);
        String[] allCases = result.allCases();
        PrintUtil.print(allCases, "Result of StandardDictionaryTest.oneCaseTest is:");

        Assert.assertTrue(BaseUtil.arrayHasSameElement(expected, allCases));
    }

    /**
     * test case with parameter that can produce tow solution in one position
     */
    @Test
    public void twoCaseTest() {
        Dictionary dictionary = Dictionary.create(TrieNodeTypeEnum.ARRAY);

        String input = "ilikesamsungmobile";
        String[] expected = new String[]{"i like samsung mobile", "i like sam sung mobile"};

        BreakResult result = dictionary.breakLine(input);
        String[] allCases = result.allCases();
        PrintUtil.print(allCases, "Result of StandardDictionaryTest.twoCaseTest is:");

        Assert.assertTrue(BaseUtil.arrayHasSameElement(expected, allCases));
    }

    /**
     * test case with parameter that can contains word not exists in the dictionary
     * the app will mark the words with * end if the word is not break correctly
     */
    @Test
    public void wordNotInDictTest() {
        Dictionary dictionary = Dictionary.create(TrieNodeTypeEnum.ARRAY);

        String input = "ilikesun";
        String[] expected = new String[]{"i like sun*"};

        BreakResult result = dictionary.breakLine(input);
        String[] allCases = result.allCases();
        PrintUtil.print(allCases, "Result of StandardDictionaryTest.wordNotInDictCaseTest is:");

        Assert.assertTrue(BaseUtil.arrayHasSameElement(expected, allCases));
    }

    /**
     * test case that load dictionary by file
     */
    @Test
    public void loadDictByFileTest() {
        Dictionary dictionary = Dictionary.create(TrieNodeTypeEnum.ARRAY, "dict_std.dict", false);

        String input = "ilikesun";
        String[] expected = new String[]{"i like sun*"};

        BreakResult result = dictionary.breakLine(input);
        String[] allCases = result.allCases();
        PrintUtil.print(allCases, "Result of StandardDictionaryTest.loadDictByFileCaseTest is:");

        Assert.assertTrue(BaseUtil.arrayHasSameElement(expected, allCases));
    }

    /**
     * test case that contains more words
     * the result is not easy to expect, we can trace back from the result
     */
    @Test
    public void testWithMoreWords() {
        Dictionary dictionary = Dictionary.create(TrieNodeTypeEnum.ARRAY, "dict_001.dict", false);
        String input = "givenavalidsentencewithoutanyspacesbetweenthewordsandadictionaryofvalidenglishwordsfindallpossiblewaystobreakthesentenceinindividualdictionarywords";
        BreakResult result = dictionary.breakLine(input);
        PrintUtil.print(result.allCases(), "Result of StandardDictionaryTest.testWithMoreWords is:");
    }

}
