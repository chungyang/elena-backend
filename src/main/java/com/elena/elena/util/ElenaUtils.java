package com.elena.elena.util;

import java.net.URL;

public class ElenaUtils {

    public static String getFilePath(String fileName) {
        URL res = ElenaUtils.class.getClassLoader().getResource(fileName);
        return res.getPath();
    }
}
