package com.quinn.interview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * 分割句子
 *
 * @author Qunhua.Liao
 * @since 2020-10-09
 */
public class SplitLine {

    /**
     * 单词分隔符
     */
    private static final char SPLIT_OF_WORD = '\t';

    /**
     * 可能存在断句的占位符号
     */
    private static final char SPLIT_OF_POSSIBLE_BREAK = '?';

    /**
     * 正常断句的占位符号
     */
    private static final char SPLIT_OF_NORMAL_BREAK = ' ';

    /**
     * 错误断句的占位符号
     */
    private static final char SPLIT_OF_ERROR_BREAK = '*';

    /**
     * 最小字符：可以根据实际情况加入数字（现在情况所有数字单独成词）用于偏移计算
     * 用于计算偏移量：0位用作单词分隔符; 1位用于空格（数组多比字母总数多留两位置）
     */
    private static final char MIN_CHAR_OF_ENGLISH = 'a' - 2;

    /**
     * 字典数据
     * 题目中字典没有 and，为达到效果，添加了单词 and，如果没有，程序会检测出来并标记 *，如下效果
     * i like ice cream a* n* d* man go
     */
    private static final String[] DIC_WORDS = new String[]{"i", "like", "sam", "sung", "samsung", "mobile", "ice", "cream", "man go", "and"};

    /**
     * 字典根节点
     */
    private static TrieNode ROOT;

    /**
     * 程序入口
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        test1();

        test2();

        test3();
    }

    /**
     * 标准字典
     */
    public static void test1() {
        System.out.println("test 1 start: user standard dict");
        initDictRoot(DIC_WORDS);
        breakAndPrint();
        System.out.println("test 1 end \n");
    }

    /**
     * 仅用自定义词典
     */
    public static void test2() {
        System.out.println("test 2 start: user custom dict");
        String[] customDic = new String[]{"i", "like", "sam", "sung", "mobile", "icecream", "man go", "mango", "and"};
        initDictRoot(customDic);
        breakAndPrint();
        System.out.println("test 2 end \n");
    }

    /**
     * 同时使用标准词典自定义词典
     */
    public static void test3() {
        System.out.println("test 3 start: user both dicts");
        String[] customDic = new String[]{"i", "like", "sam", "sung", "mobile", "icecream", "man go", "mango"};
        HashSet<String> dict = new HashSet<>();
        dict.addAll(Arrays.asList(DIC_WORDS));
        dict.addAll(Arrays.asList(customDic));

        initDictRoot(dict.toArray(new String[dict.size()]));
        breakAndPrint();

        System.out.println("test 3 end \n");
    }

    /**
     * 分割并打印
     */
    public static void breakAndPrint() {
        // 不区分大小写
        String input = "ilikesamsungmobile";
        StringBuilder result = ROOT.breakLine(input.toLowerCase());
        printResult(result);

        // 不区分大小写
        input = "ilikeicecreamandmango";
        result = ROOT.breakLine(input.toLowerCase());
        printResult(result);
    }

    /**
     * 打印结果(改进：避免内存过大)
     *
     * @param result 模糊的结果
     */
    public static void printResult(StringBuilder result) {
        List<StringBuilder> list = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        list.add(query);

        for (int i = 0; i < result.length(); i++) {
            char c = result.charAt(i);
            if (c == SPLIT_OF_POSSIBLE_BREAK) {
                List<StringBuilder> cpList = new ArrayList<>();
                for (StringBuilder q : list) {
                    StringBuilder cp = new StringBuilder(q);
                    cp.append(SPLIT_OF_NORMAL_BREAK);
                    cpList.add(cp);
                }
                list.addAll(cpList);
            } else {
                for (StringBuilder q : list) {
                    q.append(c);
                }
            }
        }

        for (StringBuilder q : list) {
            System.out.println(q);
        }
    }

    /**
     * 初始化字典树(不区分大小写)
     *
     * @param dictWords 字典单词
     */
    private static void initDictRoot(String[] dictWords) {
        ROOT = new TrieNode(SPLIT_OF_WORD);
        for (String word : dictWords) {
            ROOT.addWord(word.toLowerCase());
        }
    }

    /**
     * 将字典组织成树：节点
     */
    static class TrieNode {

        TrieNode(char data) {
            this.data = data;
        }

        /**
         * 这个节点对应的字符
         */
        private final char data;

        /**
         * 这个节点可以接什么字符（节点）
         * <p>
         * 使用数组未经严格推敲，使用空间换时间及代码便捷性：可以尝试使用MAP（如果太多其他特殊字符也直接用MAP：现在只有空格）
         */
        private final TrieNode[] nextChars = new TrieNode['z' - MIN_CHAR_OF_ENGLISH];

        /**
         * 添加下一级节点
         *
         * @param array 字典中单条记录
         * @param index 对应的下标
         */
        void addNext(char[] array, int index) {
            if (index >= array.length) {
                if (nextChars[0] == null) {
                    nextChars[0] = new TrieNode(SPLIT_OF_WORD);
                }
                return;
            }

            char c = array[index];
            int pos = c - MIN_CHAR_OF_ENGLISH;

            // TODO 此处如果特殊字符不止有空格：需要改变方案
            if (pos <= 0) {
                pos = 1;
            }

            TrieNode nextChar = nextChars[pos];
            if (nextChar == null) {
                nextChar = new TrieNode(c);
                nextChars[pos] = nextChar;
            }
            nextChar.addNext(array, ++index);
        }

        /**
         * 一般用于根节点调用：添加字典单词
         *
         * @param word 单词（为保证效率不判空，调用时请保证非NULL）
         */
        void addWord(String word) {
            addNext(word.toCharArray(), 0);
        }

        /**
         * 断句
         *
         * @param line 句子
         * @return 断好的句子
         */
        StringBuilder breakLine(String line) {
            char[] chars = line.toCharArray();
            // 假设平均8个字符一个单词：余量初始化长度，减少扩容消耗
            StringBuilder query = new StringBuilder(chars.length + chars.length >> 3);
            breakNext(chars, query, 0);
            return query;
        }

        /**
         * 断下一个
         *
         * @param chars 原句子字符数字
         * @param query 拼接结果
         * @param index 下标
         */
        void breakNext(char[] chars, StringBuilder query, int index) {
            if (index >= chars.length) {
                return;
            }

            char currChar = chars[index];
            query.append(currChar);

            if (index == chars.length - 1) {
                return;
            }

            int currPos = currChar - MIN_CHAR_OF_ENGLISH;
            if (currPos <= 0 || currPos >= nextChars.length || nextChars[currPos] == null) {
                query.append(SPLIT_OF_ERROR_BREAK).append(SPLIT_OF_NORMAL_BREAK);
                ROOT.breakNext(chars, query, ++index);
                return;
            }

            TrieNode currNode = nextChars[currPos];
            // 输入参数里面的下一个字符：要根据他判断是否断句
            char nextChar = chars[++index];
            // 根据下一个单词获取位置
            int nextPos = nextChar - MIN_CHAR_OF_ENGLISH;

            // 如果超出字典范围
            if (nextPos <= 0 || nextPos >= currNode.nextChars.length) {
                query.append(SPLIT_OF_ERROR_BREAK).append(SPLIT_OF_NORMAL_BREAK);
                // 如果当前字符不可能跟着下一个字符
            } else if (currNode.nextChars[nextPos] == null) {
                // 后面也不可以根空格
                if (currNode.nextChars[1] == null) {
                    // 如果按照字典，此处没有断词：拼接错误标记
                    if (currNode.nextChars[0] == null) {
                        query.append(SPLIT_OF_ERROR_BREAK).append(SPLIT_OF_NORMAL_BREAK);
                    } else {
                        query.append(SPLIT_OF_NORMAL_BREAK);
                    }
                    ROOT.breakNext(chars, query, index);
                    // 如果后面可以根空格
                } else {
                    // 看看空格的nextChars
                    switch (followCharCanBeDepend(currNode.nextChars[1], chars, index)) {
                        case 0:
                            // 后面字符串拼接不到下一个段词符
                            query.append(SPLIT_OF_NORMAL_BREAK);
                            ROOT.breakNext(chars, query, index);
                            return;
                        case 1:
                            // 后面的字符串可以单独成词
                            query.append(SPLIT_OF_POSSIBLE_BREAK);
                            break;
                        default:
                            // 后面胡字符串只能跟在后面
                            query.append(SPLIT_OF_NORMAL_BREAK);
                            break;
                    }
                    currNode.nextChars[1].breakNext(chars, query, index);
                }
                // 如果单词里面跟着这个字符，但是此处也可以断词：判断，标记为疑惑
            } else {
                if (currNode.nextChars[0] != null) {
                    switch (followCharCanBeDepend(currNode, chars, index)) {
                        case 0:
                            // 后面字符串拼接不到下一个段词符
                            query.append(SPLIT_OF_NORMAL_BREAK);
                            ROOT.breakNext(chars, query, index);
                            return;
                        case 1:
                            // 后面的字符串可以单独成词
                            query.append(SPLIT_OF_POSSIBLE_BREAK);
                            break;
                        default:
                            // 后面胡字符串只能跟在后面
                            break;
                    }
                } else if (currNode.nextChars[1] != null) {
                    // 看看空格的nextChars
                    int iOfNormal = followCharCanBeDepend(currNode, chars, index);
                    switch (iOfNormal) {
                        case 0:
                            // 后面字符串拼接不到下一个段词符
                            query.append(SPLIT_OF_NORMAL_BREAK);
                            ROOT.breakNext(chars, query, index);
                            return;
                        default:
                            // 后面胡字符串只能跟在后面
                            query.append(SPLIT_OF_POSSIBLE_BREAK);
                            break;
                    }
                    currNode.nextChars[1].breakNext(chars, query, index);
                    return;
                }

                currNode.breakNext(chars, query, index);
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
        int followCharCanBeDepend(TrieNode currNode, char[] chars, int index) {
            // 如果连接后面的字符再匹配不到断词符号，优先断开
            StringBuilder query = new StringBuilder();
            currNode.fistWord(chars, index, query);
            if (query.charAt(query.length() - 1) == SPLIT_OF_ERROR_BREAK) {
                return 0;
            }

            // 如果后面胡字符串可以单独成词，返回
            query.setLength(0);
            ROOT.fistWord(chars, index, query);
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

            TrieNode currNode = nextChars[currPos];
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

    }

}
