package com.jt.tools.util;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * since 2016/1/21.
 */
public class F {

    public static String readLine(String file) {
        try {
            return Files.readFirstLine(new File(F.class.getResource(file).toURI()), Charsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String readAllLine(String file) {
          try {
            return FileUtils.readFileToString(new File(F.class.getResource(file).toURI()), Charsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
