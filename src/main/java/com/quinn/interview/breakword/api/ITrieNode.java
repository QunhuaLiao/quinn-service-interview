package com.quinn.interview.breakword.api;

import com.quinn.interview.breakword.model.BreakResult;
import com.quinn.interview.breakword.model.Dictionary;

/**
 * Data structure to organize the word relation: which worlds can be follow this current one
 * Support different implementation below:
 * 1 use Array to store the relation information, in this way, the code is simple (cost less cpu), but cost more memory
 * 2 use Map to store the relation information, this way is opposite to the 1st way
 * 3 we can also reverse the sequence of letters in the dictionary
 * <p>
 * ** we hide this interface and its implementation by specify its access modifier to "Default"
 * ** to avoid someone operate node directly witch may exists some risk
 * ** usually, we proxy its implementation class by Dictionary
 *
 * @author Qunhua.Liao
 * @see Dictionary
 * @since 2020-10-13
 */
public interface ITrieNode {

    /**
     * the char to mark the word end
     */
    char SPLIT_OF_WORD = '\t';

    /**
     * the char to mark a position could be both broken and not broken
     */
    char SPLIT_OF_POSSIBLE_BREAK = '?';

    /**
     * the char to mark a position can be exactly broken
     */
    char SPLIT_OF_NORMAL_BREAK = ' ';

    /**
     * the char to mark a position is broken wrong
     * (the word before the * does not exist int the dictionary)
     */
    char SPLIT_OF_ERROR_BREAK = '*';

    /**
     * add a word to the node
     * usually invoked just by the root node
     *
     * @param word word to be added
     */
    void addWord(String word);

    /**
     * break a line without any space, be composed of english letters only
     *
     * @param line the line to be break
     * @return result StringBuild with all possible cases
     */
    BreakResult breakLine(String line);

}
