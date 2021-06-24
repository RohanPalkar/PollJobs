package org.pollfor.api;

import org.pollfor.entities.TimeValue;
import org.pollfor.sdk.ActionExecutor;
import org.pollfor.service.ServiceType;
import org.pollfor.service.SyncExecutor;
import org.pollfor.service.WaitExecutor;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class DelayJob {

    private final TimeValue initialDelayTimeOut;
    private final TimeValue initialDelayTimeInterval;
    private final Long timeOutMillis;
    private final Long timeIntervalMillis;
    private final Predicate<?> entryCriterion;
    private final Supplier<?> entryAction;

    private final TimeValue initialDelay;
    private final Long initialDelayMillis;

    private ActionExecutor executorService;

    public DelayJob(Predicate<?> entryCriterion, Supplier<?> entryAction, PollJob pollJob){
        this.entryCriterion = entryCriterion;
        this.entryAction = entryAction;

        this.initialDelayTimeOut = pollJob.getTimeOut();
        this.initialDelayTimeInterval = pollJob.getTimeInterval();

        this.timeOutMillis = pollJob.getTimeOutMillis();
        this.timeIntervalMillis = pollJob.getTimeIntervalMillis();

        this.initialDelay = null;
        this.initialDelayMillis = null;

        this.executorService = new SyncExecutor(this.entryCriterion,
                this.entryAction,
                this.initialDelayTimeOut,
                this.timeOutMillis,
                this.initialDelayTimeInterval,
                this.timeIntervalMillis);
    }

    public DelayJob(TimeValue initialDelay){
        this.entryCriterion = null;
        this.entryAction = null;

        this.initialDelayTimeOut = null;
        this.initialDelayTimeInterval = null;

        this.timeOutMillis = null;
        this.timeIntervalMillis = null;

        this.initialDelay = initialDelay;
        this.initialDelayMillis = this.initialDelay != null ?
                this.initialDelay.getUnit().toMillis(this.initialDelay.getValue()) : null;

        this.executorService = new WaitExecutor(this.initialDelay.getValue(), this.initialDelay.getUnit(), this.initialDelayMillis);
    }

    public <T> List<PollResult.PollInfo<T>> executeDelayPollJob(){
        return this.executorService.executePoll();
    }

    public TimeValue getInitialDelayTimeOut() {
        return initialDelayTimeOut;
    }

    public TimeValue getInitialDelayTimeInterval() {
        return initialDelayTimeInterval;
    }

    public Long getTimeOutMillis() {
        return timeOutMillis;
    }

    public Long getTimeIntervalMillis() {
        return timeIntervalMillis;
    }

    public Predicate<?> getEntryCriterion() {
        return entryCriterion;
    }

    public Supplier<?> getEntryAction() {
        return entryAction;
    }

    public TimeValue getInitialDelay() {
        return initialDelay;
    }

    public Long getInitialDelayMillis() {
        return initialDelayMillis;
    }
}
