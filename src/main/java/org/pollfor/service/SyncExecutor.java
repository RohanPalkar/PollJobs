package org.pollfor.service;

import org.pollfor.entities.PollConfig;
import org.pollfor.api.PollResult;
import org.pollfor.entities.TimeValue;
import org.pollfor.sdk.ActionExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SyncExecutor extends PollExecutor implements ActionExecutor {

    private final Predicate<?> criterion;
    private final Supplier<?> action;
    private final TimeValue timeOut;
    private final long timeOutInMillis;
    private final TimeValue timeInterval;
    private final long timeIntervalInMillis;

    public SyncExecutor(Predicate<?> criterion,
                        Supplier<?> action,
                        TimeValue timeOut,
                        long timeOutInMillis,
                        TimeValue timeInterval,
                        long timeIntervalInMillis) {
        this.criterion = criterion;
        this.action = action;
        this.timeOut = timeOut;
        this.timeOutInMillis = timeOutInMillis;
        this.timeInterval = timeInterval;
        this.timeIntervalInMillis = timeIntervalInMillis;
    }

    @Override
    public <T> List<PollResult.PollInfo<T>> executePoll() {
        return Arrays.asList(actionAwait(this.criterion,
                            this.action,
                            timeOut,
                            timeOutInMillis,
                            timeInterval,
                            timeIntervalInMillis));
    }
}
