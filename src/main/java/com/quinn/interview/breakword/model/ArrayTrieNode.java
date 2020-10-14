package com.quinn.interview.breakword.model;

import com.quinn.interview.breakword.api.ITrieNode;
import com.quinn.interview.breakword.api.ITrieNodeSupplier;
import com.quinn.interview.breakword.enums.TrieNodeTypeEnum;

/**
 * the min unit of a dictionary to store information, and it can be structured as a tree
 * which contains the current char and a char array that can append to the current char to make up a word
 * <p>
 * when a word can be broken, we place SPLIT_OF_WORD('\t') at the 1st position of the array (array[0])
 * and if a word can be break by blank itself (e.g "man go"), then we place ' ' at the 2nd position of the array
 * and we place 'a' at the 3th position, 'b' at the 4th position and so on (if the char can follow after the current)
 * <p>
 * so the path of "hello" in the tree is '\t'(root) -> 'h' -> 'e' -> 'l' -> 'l' -> 'o' -> '\t'
 * and so when the dictionary has two words "hell", "hello", then the tree structure is like blow (...)
 *
 * @author Qunhua.Liao
 * @since 2020-10-13
 */
public class ArrayTrieNode implements ITrieNode {

    /**
     * the min offset of the array length,
     * we use 'z' - 2 MIN_CHAR_OF_ENGLISH to get the length of array (2 space more than the size of english letter)
     * we use c - MIN_CHAR_OF_ENGLISH to get the offset of c in the array
     * position 0 is used to locate '\t'
     * position 1 is used to locate ' '
     */
    private static final char MIN_CHAR_OF_ENGLISH = 'a' - 2;

    /**
     * Constructor
     *
     * @param data current char
     */
    public ArrayTrieNode(char data) {
        this.data = data;
    }

    /**
     * the char that current node holds
     */
    @SuppressWarnings("unused")
    private final char data;

    /**
     * array to locate all possible chars that can follow after current char
     * TODO this may produce to many null element in the array (so we can use Map Implement instead)
     */
    private final ArrayTrieNode[] nextChars = new ArrayTrieNode['z' - MIN_CHAR_OF_ENGLISH];

    @Override
    public void addWord(String word) {
        addNext(word.toCharArray(), 0);
    }

    @Override
    public BreakResult breakLine(String line) {
        char[] chars = line.toCharArray();
        BreakResult result = new BreakResult(chars.length);
        breakNext(chars, result, 0, this);
        return result;
    }

    /**
     * 添加下一级节点
     *
     * @param array 字典中单条记录
     * @param index 对应的下标
     */
    private void addNext(char[] array, int index) {
        if (index >= array.length) {
            if (nextChars[0] == null) {
                nextChars[0] = new ArrayTrieNode(SPLIT_OF_WORD);
            }
            return;
        }

        char c = array[index];
        int pos = c - MIN_CHAR_OF_ENGLISH;

        // TODO 此处如果特殊字符不止有空格：需要改变方案
        if (pos <= 0) {
            pos = 1;
        }

        ArrayTrieNode nextChar = nextChars[pos];
        if (nextChar == null) {
            nextChar = new ArrayTrieNode(c);
            nextChars[pos] = nextChar;
        }
        nextChar.addNext(array, ++index);
    }

    /**
     * 断下一个
     *
     * @param chars  原句子字符数字
     * @param result 拼接结果
     * @param index  下标
     */
    private void breakNext(char[] chars, BreakResult result, int index, ArrayTrieNode root) {
        if (index >= chars.length) {
            return;
        }

        char currChar = chars[index];
        result.append(currChar);

        int currPos = currChar - MIN_CHAR_OF_ENGLISH;
        if (index == chars.length - 1) {
            if (nextChars[currPos] == null || nextChars[currPos].nextChars[0] == null) {
                result.append(SPLIT_OF_ERROR_BREAK);
            }
            return;
        }

        if (currPos <= 0 || currPos >= nextChars.length || nextChars[currPos] == null) {
            result.append(SPLIT_OF_ERROR_BREAK).append(SPLIT_OF_NORMAL_BREAK);
            root.breakNext(chars, result, ++index, root);
            return;
        }

        ArrayTrieNode currNode = nextChars[currPos];
        // 输入参数里面的下一个字符：要根据他判断是否断句
        char nextChar = chars[++index];
        // 根据下一个单词获取位置
        int nextPos = nextChar - MIN_CHAR_OF_ENGLISH;

        // 如果超出字典范围
        if (nextPos <= 0 || nextPos >= currNode.nextChars.length) {
            result.append(SPLIT_OF_ERROR_BREAK).append(SPLIT_OF_NORMAL_BREAK);
            // 如果当前字符不可能跟着下一个字符
        } else if (currNode.nextChars[nextPos] == null) {
            // 后面也不可以根空格
            if (currNode.nextChars[1] == null) {
                // 如果按照字典，此处没有断词：拼接错误标记
                if (currNode.nextChars[0] == null) {
                    result.append(SPLIT_OF_ERROR_BREAK).append(SPLIT_OF_NORMAL_BREAK);
                } else {
                    result.append(SPLIT_OF_NORMAL_BREAK);
                }
                root.breakNext(chars, result, index, root);
                // 如果后面可以根空格
            } else {
                // 看看空格的nextChars
                switch (followCharCanBeDepend(currNode.nextChars[1], chars, index, root)) {
                    case 0:
                        // 后面字符串拼接不到下一个段词符
                        result.append(SPLIT_OF_NORMAL_BREAK);
                        root.breakNext(chars, result, index, root);
                        return;
                    case 1:
                        // 后面的字符串可以单独成词
                        result.append(SPLIT_OF_POSSIBLE_BREAK);
                        break;
                    default:
                        // 后面胡字符串只能跟在后面
                        result.append(SPLIT_OF_NORMAL_BREAK);
                        break;
                }
                currNode.nextChars[1].breakNext(chars, result, index, root);
            }
            // 如果单词里面跟着这个字符，但是此处也可以断词：判断，标记为疑惑
        } else {
            if (currNode.nextChars[0] != null) {
                switch (followCharCanBeDepend(currNode, chars, index, root)) {
                    case 0:
                        // 后面字符串拼接不到下一个段词符
                        result.append(SPLIT_OF_NORMAL_BREAK);
                        root.breakNext(chars, result, index, root);
                        return;
                    case 1:
                        // 后面的字符串可以单独成词
                        result.append(SPLIT_OF_POSSIBLE_BREAK);
                        break;
                    default:
                        // 后面胡字符串只能跟在后面
                        break;
                }
            } else if (currNode.nextChars[1] != null) {
                // 看看空格的nextChars
                int iOfNormal = followCharCanBeDepend(currNode, chars, index, root);
                // 后面字符串拼接不到下一个段词符
                if (iOfNormal == 0) {
                    result.append(SPLIT_OF_NORMAL_BREAK);
                    root.breakNext(chars, result, index, root);
                    return;
                    // 后面胡字符串只能跟在后面
                } else {
                    result.append(SPLIT_OF_POSSIBLE_BREAK);
                }
                currNode.nextChars[1].breakNext(chars, result, index, root);
                return;
            }

            currNode.breakNext(chars, result, index, root);
            // 如果单词后面可以根空格
        }
    }

    /**
     * 接下来的字符串可不可以独立
     *
     * @param currNode 当前节点
     * @param chars    字符串
     * @param index    下标
     * @return 可以独立成词就是true
     */
    private int followCharCanBeDepend(ArrayTrieNode currNode, char[] chars, int index, ArrayTrieNode root) {
        // 如果连接后面的字符再匹配不到断词符号，优先断开
        StringBuilder query = new StringBuilder();
        currNode.fistWord(chars, index, query);
        if (query.charAt(query.length() - 1) == SPLIT_OF_ERROR_BREAK) {
            return 0;
        }

        // 如果后面胡字符串可以单独成词，返回
        query.setLength(0);
        root.fistWord(chars, index, query);
        if (query.charAt(query.length() - 1) != SPLIT_OF_ERROR_BREAK) {
            return 1;
        }

        return -1;
    }

    /**
     * 找第一词（饱汉模式）
     *
     * @param chars 全字符数组
     * @param index 当前下标
     * @param query 拼接字符串
     */
    private void fistWord(char[] chars, int index, StringBuilder query) {
        char currChar = chars[index];
        query.append(currChar);
        int currPos = currChar - MIN_CHAR_OF_ENGLISH;
        if (currPos <= 0 || currPos >= nextChars.length || nextChars[currPos] == null) {
            query.append(SPLIT_OF_ERROR_BREAK);
            return;
        }

        ArrayTrieNode currNode = nextChars[currPos];
        if (index == chars.length - 1) {
            if (currNode.nextChars[0] == null) {
                query.append(SPLIT_OF_ERROR_BREAK);
            }
            return;
        }

        char nextChar = chars[++index];
        // 根据下一个单词获取位置
        int nextPos = nextChar - MIN_CHAR_OF_ENGLISH;

        if (nextPos <= 0 || nextPos >= currNode.nextChars.length) {
            query.append(SPLIT_OF_ERROR_BREAK);
            return;
        }

        if (currNode.nextChars[nextPos] == null) {
            if (currNode.nextChars[0] == null) {
                query.append(SPLIT_OF_ERROR_BREAK);
            }
            return;
        }
        currNode.fistWord(chars, index, query);
    }

    /**
     * Supplier used to create ArrayTrieNode at runtime
     */
    @SuppressWarnings("unused")
    public static class Supplier implements ITrieNodeSupplier {

        @Override
        public TrieNodeTypeEnum name() {
            return TrieNodeTypeEnum.ARRAY;
        }

        @Override
        public ITrieNode supply(char c) {
            return new ArrayTrieNode(c);
        }
    }
}
