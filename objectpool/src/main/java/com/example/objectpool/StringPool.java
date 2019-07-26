package com.example.objectpool;

import java.util.UUID;

public class StringPool extends ObjectPoolImpl<String> {

    StringPool(int minSize, int maxSize, boolean autoInit) {
        super(minSize, maxSize, autoInit);
    }

    @Override
    protected String create() {
        return UUID.randomUUID().toString();
    }

    @Override
    protected void doProcess(String s) {
        System.out.println(s);
    }
}