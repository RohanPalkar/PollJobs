package org.pollfor.service;

import org.pollfor.entities.PollConfig;
import org.pollfor.api.PollResult;
import org.pollfor.entities.TimeValue;
import org.pollfor.sdk.ActionExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AsyncExecutor extends PollExecutor implements ActionExecutor {

    private final Predicate<?>[] criteria;
    private final Supplier<?>[] actions;
    private final TimeValue timeOut;
    private final long timeOutInMillis;
    private final TimeValue timeInterval;
    private final long timeIntervalInMillis;

    public AsyncExecutor(Predicate<?>[] criteria,
                         Supplier<?>[] actions,
                         TimeValue timeOut,
                         long timeOutInMillis,
                         TimeValue timeInterval,
                         long timeIntervalInMillis){
        this.criteria = criteria;
        this.actions = actions;
        this.timeOut = timeOut;
        this.timeOutInMillis = timeOutInMillis;
        this.timeInterval = timeInterval;
        this.timeIntervalInMillis = timeIntervalInMillis;
    }

    @Override
    public <T> List<PollResult.PollInfo<T>> executePoll() {
        List<CompletableFuture<PollResult.PollInfo<T>>> futureList = new ArrayList<>();

        for(int i = 0 ; i < actions.length ; ++i){
            Predicate<?> criterion = this.criteria.length > 1 ? this.criteria[i] : this.criteria[0];
            Supplier<?> action = this.actions[i];
            CompletableFuture<PollResult.PollInfo<T>> future =
                    CompletableFuture.supplyAsync(() ->
                            actionAwait(criterion, action, timeOut, timeOutInMillis, timeInterval, timeIntervalInMillis));
            futureList.add(future);
        }

        return futureList.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }
}
