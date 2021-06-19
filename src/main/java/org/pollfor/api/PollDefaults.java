package org.pollfor.api;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

public enum PollDefaults {

    TIME_OUT(2, MINUTES),
    TIME_INTERVAL(15, SECONDS),
    INITIAL_DELAY_TIME_OUT(1, MINUTES),
    INITIAL_DELAY_TIME_INTERVAL(15, SECONDS);

    private Integer time;
    private TimeUnit timeUnit;

    PollDefaults(Integer time, TimeUnit timeUnit) {
        this.time = time;
        this.timeUnit = timeUnit;
    }

    public Integer getTimeValue() {
        return time;
    }

    public TimeUnit getTimeUnitValue() {
        return timeUnit;
    }
}
