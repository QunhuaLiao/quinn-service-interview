package com.quinn.interview.breakword.api;

/**
 * Interface to handle a text file(usually very large) part by part
 *
 * @author Qunhua.Liao
 * @since 2020-02-12
 */
public interface StringFilePartHandler {

    /**
     * handle
     *
     * @param string a line of the file
     */
    void handle(String string);

}
