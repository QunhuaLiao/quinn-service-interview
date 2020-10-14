package com.quinn.interview.breakword.model;

import com.quinn.interview.breakword.api.ITrieNode;
import com.quinn.interview.breakword.api.ITrieNodeSupplier;
import com.quinn.interview.breakword.enums.TrieNodeTypeEnum;

/**
 * ITrieNode implementation which store the relation information by Map
 * to be continued
 *
 * @author Qunhua.Liao
 * @since 2020-10-13
 */
public class MapTrieNode implements ITrieNode {

    public MapTrieNode(char data) {
        this.data = data;
    }

    @SuppressWarnings("unused")
    private final char data;

    @Override
    public void addWord(String word) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BreakResult breakLine(String line) {
        throw new UnsupportedOperationException();
    }

    /**
     * Supplier used to create ArrayTrieNode at runtime
     */
    public static class Supplier implements ITrieNodeSupplier {

        @Override
        public TrieNodeTypeEnum name() {
            return TrieNodeTypeEnum.MAP;
        }

        @Override
        public ITrieNode supply(char c) {
            return new MapTrieNode(c);
        }
    }
}
