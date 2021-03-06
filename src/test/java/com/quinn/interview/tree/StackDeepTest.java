package com.quinn.interview.tree;

import java.util.*;

/**
 * 栈深测试 使用六种括号组成的字符串{[()]}
 * 判断它们的深度，如{} 为1；[()] 为2；({[]}) 为3；不匹配则为0
 *
 * @author Quinn.Liao
 * @since 2020-12-06
 */
public class StackDeepTest {

    private static final char OPEN_MARK1 = '(';

    private static final char OPEN_MARK2 = '{';

    private static final char OPEN_MARK3 = '[';

    private static final char CLOSE_MARK1 = ')';

    private static final char CLOSE_MARK2 = '}';

    private static final char CLOSE_MARK3 = ']';

    /**
     * 对应关系
     */
    private static final Map<Character, Character> matches = new HashMap<>(16);

    /**
     * 范围内的字符串
     */
    private static final Set<Character> marks = new HashSet<>();

    static {
        matches.put(CLOSE_MARK1, OPEN_MARK1);
        matches.put(CLOSE_MARK2, OPEN_MARK2);
        matches.put(CLOSE_MARK3, OPEN_MARK3);

        marks.add(OPEN_MARK1);
        marks.add(OPEN_MARK2);
        marks.add(OPEN_MARK3);
        marks.add(CLOSE_MARK1);
        marks.add(CLOSE_MARK2);
        marks.add(CLOSE_MARK3);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Stack<Character> charStack = new Stack<>();

        while (in.hasNext()) {
            int deep = 0;
            int maxDeep = 0;
            String line = in.nextLine();
            char[] chars = line.toCharArray();
            for (char c : chars) {
                if (!marks.contains(c)) {
                    break;
                }

                if (matches.containsKey(c)) {
                    if (charStack.isEmpty()) {
                        break;
                    }

                    Character pop = charStack.pop();
                    if (!pop.equals(matches.get(c))) {
                        break;
                    }
                    deep--;
                } else {
                    charStack.push(c);
                    deep++;
                    if (deep >= maxDeep) {
                        maxDeep = deep;
                    }
                }
            }

            if (deep != 0) {
                System.out.println(0);
            } else {
                System.out.println(maxDeep);
            }
        }
    }
}
