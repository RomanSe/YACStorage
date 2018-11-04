package com.rs.client;

public class ControllerHelper {
    public static String formatSize(String value) {
        long size = Long.parseLong(value);
        if (size < 1000)
            return value + " B";
        else if (size < 1000000)
            return (size/1000) + " kB";
        else if (size < 1000000000)
            return (size/1000000) + " MB";
        else
            return (size/1000000000) + " GB";
    }
}
