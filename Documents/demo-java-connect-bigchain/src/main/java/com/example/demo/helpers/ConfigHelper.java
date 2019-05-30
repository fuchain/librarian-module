package com.example.demo.helpers;

import com.bigchaindb.builders.BigchainDbConfigBuilder;

public class ConfigHelper {

    public static void setupConfig(String host) {
        BigchainDbConfigBuilder
                .baseUrl("http://testnet.bigchaindb.com")
                .addToken("app_id", "")
                .addToken("app_key", "")
                .setTimeout(10000)
                .setup();
    }
}
