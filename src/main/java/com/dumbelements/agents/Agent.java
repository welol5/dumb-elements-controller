package com.dumbelements.agents;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public abstract class Agent {
    protected static ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);
}
