package com.ktviv.pointpoker.domain.service;

import java.util.concurrent.ExecutorService;

public abstract class BaseStrategy implements SessionEventBroadcastStrategy {

    private final ExecutorService executorService;

    public BaseStrategy(ExecutorService executorService) {

        this.executorService = executorService;
    }

    public ExecutorService getExecutorService() {

        return executorService;
    }
}
