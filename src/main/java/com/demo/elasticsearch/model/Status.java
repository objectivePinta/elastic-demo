package com.demo.elasticsearch.model;

import java.util.concurrent.ThreadLocalRandom;

public enum Status {
    PUBLISHED,
    DRAFT,
    FINAL;

    public static Status getRandom() {

        return values()[ThreadLocalRandom.current().nextInt(values().length - 1)];
    }
}
