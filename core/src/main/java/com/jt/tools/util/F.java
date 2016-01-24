package com.jt.tools.util;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * since 2016/1/21.
 */
public class F {


    public static String readAllLine(String file) {
        try {
            return FileUtils.readFileToString(new File(F.class.getResource(file).toURI()), Charsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
