package com.rs.common;

import org.sqlite.JDBC;

public class DefaultConfig {
    public static final String HOST = "127.0.0.1";//"192.168.1.42";
    public static final int PORT = 3000;
    public static final int NETWORK_QUEUE_SIZE = 1;
    public static final int MAX_OBJ_SIZE = 1024 * 1024 * 10;
    public static final int FILE_CHANK_SIZE = 1024 * 1024 * 9;
    //client
    public static final String CLIENT_ROOT_PATH = "E:\\cloud\\client";
    //server
    public static final int WAITING_CONNECTION_REQUESTS = 128;
    public static final String SERVER_ROOT_PATH = "E:\\cloud\\server";
    public static final String PART_FILE_EXT = ".part";
    public static final int POOL = 5;
    //server DB
    public static final String DRIVER_NAME = "org.sqlite.JDBC";
    public static final String DB_NAME = JDBC.PREFIX + "users.db";
    public static final int TRY_NUM = 10;
}
