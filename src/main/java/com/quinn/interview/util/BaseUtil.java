package com.quinn.interview.util;

import com.quinn.interview.breakword.api.StringFilePartHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Simple String utils
 *
 * @author Qunhua.Liao
 * @since 2020-10-13
 */
public final class BaseUtil {

    /**
     * Determine whether the string is empty
     *
     * @param str string to be judged
     * @return true if arg string is null or ""
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    /**
     * Determine whether two set has the same elements
     *
     * @param source source Set
     * @param target target Set
     * @param <T>    generic paradigm to make sure that two sets has the same data type
     * @return true if the two sets has the same element
     */
    public static <T> boolean arrayHasSameElement(T[] source, T[] target) {
        if (source == null && target == null) {
            return true;
        }
        if (source == null || target == null || source.length != target.length) {
            return false;
        }

        List<T> sourceList = Arrays.asList(source);
        List<T> targetList = Arrays.asList(target);
        return sourceList.containsAll(targetList) && sourceList.containsAll(sourceList);
    }


    /**
     * read and handle a text file line by line
     *
     * @param filePath 文件路径
     * @param handler  大文件处理器
     */
    public static boolean readLargeFile(String filePath, StringFilePartHandler handler) {
        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream(filePath);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                handler.handle(line);
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException("file " + filePath + " read failed", e);
        } finally {
            closeQuietly(inputStream);
            closeQuietly(sc);
        }
    }

    /**
     * close input streams quietly
     *
     * @param input the streams
     */
    public static void closeQuietly(InputStream... input) {
        if (input != null) {
            for (InputStream in : input) {
                if (in == null) {
                    continue;
                }
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }

    }

    /**
     * close out streams quietly
     *
     * @param output the streams
     */
    public static void closeQuietly(OutputStream... output) {
        if (output != null) {
            for (OutputStream out : output) {
                if (out == null) {
                    continue;
                }
                try {
                    out.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    /**
     * close out streams quietly
     *
     * @param scanners the scanners
     */
    public static void closeQuietly(Scanner... scanners) {
        if (scanners != null) {
            for (Scanner scanner : scanners) {
                if (scanner == null) {
                    continue;
                }
                scanner.close();
            }
        }

    }

    /**
     * Determine whether the array is empty
     *
     * @param chars array to be judged
     * @return true if arg array is null or length of it is 0
     */
    public static boolean isEmpty(char[] chars) {
        return chars == null || chars.length == 0;
    }
}
