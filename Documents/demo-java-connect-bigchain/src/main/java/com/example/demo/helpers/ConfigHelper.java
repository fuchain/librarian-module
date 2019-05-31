package com.example.demo.helpers;

import com.bigchaindb.builders.BigchainDbConfigBuilder;

public class ConfigHelper {

    private static final int TIMEOUT = 10000;

    /**
     * setup configuration of bigchaindb with default timeout
     * @param host
     */
    public static void setupConfig(String host) {
        setupConfig(host, TIMEOUT);
    }

    /**
     * setup configuration of bigchaindb with specific timeout
     * @param host
     * @param timeout
     */
    public static void setupConfig(String host, int timeout) {
        BigchainDbConfigBuilder
                .baseUrl(host)
                .addToken("app_id", "")
                .addToken("app_key", "")
                .setTimeout(timeout)
                .setup();
    }
}
