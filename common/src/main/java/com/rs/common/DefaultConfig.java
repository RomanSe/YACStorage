package com.rs.common;

public class DefaultConfig {
    public static final String HOST = "127.0.0.1";//"192.168.1.42";
    public static final int PORT = 3000;
    public static final int NETWORK_QUEUE_SIZE = 1;
    public static final int MAX_OBJ_SIZE = 1024 * 1024 * 10;
    public static final int FILE_CHANK_SIZE = 1024 * 1024 * 9;
    //server
    public static final int WAITING_CONNECTION_REQUESTS = 128;
    public static final String SERVER_ROOT_PATH = "C:\\cloud\\server";
    public static final String PART_FILE_EXT = ".part";
}
