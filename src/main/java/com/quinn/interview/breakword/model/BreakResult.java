package com.quinn.interview.breakword.model;

import com.quinn.interview.breakword.api.ITrieNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Result of the break word
 *
 * @author Qunhua.Liao
 * @since 2020-10-13
 */
public class BreakResult {

    /**
     * possible cases of result
     */
    private StringBuilder query;

    /**
     * we suppose that a word contains 8 letters in average
     * and we init with a bigger size than the line length to decrease the num of "resize"
     *
     * @param length the length of line
     */
    public BreakResult(int length) {
        this.query = new StringBuilder(length + length >> 3);
    }

    /**
     * append a char to the result
     *
     * @param c char to be appended
     * @return return it self to enable chain operation
     */
    public BreakResult append(char c) {
        this.query.append(c);
        return this;
    }

    /**
     * all possible cases of result
     *
     * @return all possible cases
     */
    public String[] allCases() {
        List<StringBuilder> list = new ArrayList<>();
        StringBuilder res = new StringBuilder();
        list.add(res);

        for (int i = 0; i < query.length(); i++) {
            char c = query.charAt(i);
            if (c == ITrieNode.SPLIT_OF_POSSIBLE_BREAK) {
                List<StringBuilder> cpList = new ArrayList<>();
                for (StringBuilder q : list) {
                    StringBuilder cp = new StringBuilder(q);
                    cp.append(ITrieNode.SPLIT_OF_NORMAL_BREAK);
                    cpList.add(cp);
                }
                list.addAll(cpList);
            } else {
                for (StringBuilder q : list) {
                    q.append(c);
                }
            }
        }

        return list.parallelStream().map(StringBuilder::toString).toArray(String[]::new);
    }
}
