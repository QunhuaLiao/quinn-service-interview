package com.quinn.interview.breakword.api;

import com.quinn.interview.breakword.enums.TrieNodeTypeEnum;

/**
 * this interface is designed to create different ITrieNode at runtime according by arguments
 *
 * @author Qunhua.Liao
 * @since 2020-10-13
 */
public interface ITrieNodeSupplier {

    /**
     * return the name of this Supplier, which used to match the argument and get the Supplier wanted at runtime
     *
     * @return name of Supplier
     */
    TrieNodeTypeEnum name();

    /**
     * create the special implementation instance of ITrieNode
     *
     * @param c the current char that this node holds
     * @return instance of ITrieNode
     */
    ITrieNode supply(char c);

}
