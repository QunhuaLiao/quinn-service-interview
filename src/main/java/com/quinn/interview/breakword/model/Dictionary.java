package com.quinn.interview.breakword.model;

import com.quinn.interview.breakword.api.ITrieNode;
import com.quinn.interview.breakword.api.ITrieNodeSupplier;
import com.quinn.interview.breakword.enums.TrieNodeTypeEnum;
import com.quinn.interview.util.BaseUtil;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dictionary: contains all valid English words
 *
 * @author Qunhua.Liao
 * @since 2020-10-13
 */
public class Dictionary {

    /**
     * the split mark of words in a text file
     */
    public static final String WORD_SPLIT = ",";

    /**
     * all words in the standard dictionary
     */
    private static final String[] STANDARD_DIC_WORDS = new String[]{"i", "like", "sam", "sung", "samsung", "mobile",
            "ice", "cream", "man go", "and"};

    /**
     * the ITrieNodeSupplier supported at runtime
     */
    private static Map<TrieNodeTypeEnum, ITrieNodeSupplier> supplierMap = new ConcurrentHashMap<>();

    // Load all ITrieNodeSupplier supported in the application
    static {
        ServiceLoader<ITrieNodeSupplier> suppliers = ServiceLoader.load(ITrieNodeSupplier.class);
        Iterator<ITrieNodeSupplier> iterator = suppliers.iterator();
        while (iterator.hasNext()) {
            ITrieNodeSupplier next = iterator.next();
            supplierMap.put(next.name(), next);
        }
    }

    /**
     * create a standard dictionary
     *
     * @param treeType the tree node type
     * @return Dictionary implementation instance
     */
    public static Dictionary create(TrieNodeTypeEnum treeType) {
        Dictionary dictionary = emptyDict(treeType);
        for (String word : STANDARD_DIC_WORDS) {
            dictionary.addWord(word);
        }
        return dictionary;
    }

    /**
     * create a standard dictionary
     *
     * @param treeType the tree node type
     * @return Dictionary implementation instance
     */
    public static Dictionary create(TrieNodeTypeEnum treeType, String[] customDict, boolean useStdDic) {
        Dictionary dictionary = emptyDict(treeType);
        if (useStdDic) {
            for (String word : STANDARD_DIC_WORDS) {
                dictionary.addWord(word);
            }
        }

        for (String word : customDict) {
            dictionary.addWord(word);
        }
        return dictionary;
    }

    /**
     * create a standard dictionary
     *
     * @param treeType the tree node type
     * @return Dictionary implementation instance
     */
    public static Dictionary create(TrieNodeTypeEnum treeType, String filePath, boolean useStdDic) {
        Dictionary dictionary = emptyDict(treeType);
        if (useStdDic) {
            for (String word : STANDARD_DIC_WORDS) {
                dictionary.addWord(word);
            }
        }

        if (!new File(filePath).exists()) {
            filePath = Dictionary.class.getResource("/" + filePath).getFile();
        }

        BaseUtil.readLargeFile(filePath, (line) -> {
            if (BaseUtil.isEmpty(line)) {
                return;
            }
            String[] split = line.split(WORD_SPLIT);
            for (String world : split) {
                dictionary.addWord(world);
            }
        });

        return dictionary;
    }

    private static Dictionary emptyDict(TrieNodeTypeEnum treeType) {
        ITrieNodeSupplier supplier = supplierMap.get(treeType);
        if (supplier == null) {
            throw new IllegalArgumentException("ITrieNode of type[" + treeType + "] not supported");
        }
        Dictionary dictionary = new Dictionary();
        dictionary.root = supplier.supply(ITrieNode.SPLIT_OF_WORD);
        dictionary.words = new HashSet<>();
        return dictionary;
    }

    /**
     * root node of the dictionary
     */
    private ITrieNode root;

    /**
     * words not repeated
     */
    private Set<String> words;

    /**
     * add a word to the node, static proxy of the root node
     *
     * @param word word to be added
     */
    public void addWord(String word) {
        if (words.contains(word)) {
            return;
        }
        this.root.addWord(word);
        words.add(word);
    }

    /**
     * break a line without any space, be composed of english letters only
     * static proxy of the root node
     *
     * @param line the line to be break
     * @return result StringBuild with all possible cases
     */
    public BreakResult breakLine(String line) {
        if (line == null) {
            return null;
        }

        if ("".equals(line)) {
            return new BreakResult(0);
        }

        return this.root.breakLine(line);
    }

}
