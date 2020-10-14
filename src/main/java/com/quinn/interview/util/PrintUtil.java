package com.quinn.interview.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple print utils
 *
 * @author Qunhua.Liao
 * @since 2020-10-13
 */
public final class PrintUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintUtil.class);

    public static <T> void print(T[] array, String title) {
        if (!BaseUtil.isEmpty(title)) {
            LOGGER.debug(title);
        }
        if (array == null) {
            LOGGER.debug("*null");
        } else {
            for (T t : array) {
                LOGGER.debug(String.valueOf(t));
            }
        }
    }

}
