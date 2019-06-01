package com.fpt.edu.config;

import com.bigchaindb.builders.BigchainDbConfigBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class BigchainDbConfig {

    private static final int TIMEOUT = 10000;

    public BigchainDbConfig(String host) {
        setupConfig(host);
    }

    /**
     * setup configuration of bigchaindb with default timeout
     *
     * @param host
     */
    private static void setupConfig(String host) {
        setupConfig(host, TIMEOUT);
    }

    /**
     * setup configuration of bigchaindb with specific timeout
     *
     * @param host
     * @param timeout
     */
    private static void setupConfig(String host, int timeout) {
        BigchainDbConfigBuilder
                .baseUrl(host)
                .addToken("app_id", "")
                .addToken("app_key", "")
                .setTimeout(timeout)
                .setup();
    }
}
