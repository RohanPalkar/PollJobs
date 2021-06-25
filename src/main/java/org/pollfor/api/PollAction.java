package org.pollfor.api;

import org.pollfor.entities.TimeValue;
import org.pollfor.service.ServiceType;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.pollfor.api.ResultStatus.*;

public class PollAction {

    private final PollJob pollJob;
    private DelayJob delayJob;

    public PollAction(PollJob pollJob) {
        this.pollJob = pollJob;
    }

    public <T> PollAction holdUntil(Predicate<? super T> entryCriterion, Supplier<? super T> action){
        this.delayJob = new DelayJob(entryCriterion, action, this.pollJob);
        return this;
    }

    public PollAction holdFor(Integer initialDelay, TimeUnit initialDelayUnit){
        TimeValue timeValue = TimeValue.create()
                                        .setValue(initialDelay)
                                        .setUnit(initialDelayUnit)
                                        .build();
        this.delayJob = new DelayJob(timeValue);
        return this;
    }

    public <T> PollResult<T> until(Predicate<? super T> exitCriterion, Supplier<? super T> action){
        this.pollJob.setExitCriterion(exitCriterion);
        this.pollJob.setExitAction(action);

        return exec(ServiceType.SYNC);
    }

    public <T> PollResult<T> until(Predicate<? super T> exitCriterion, Supplier<? super T>... actions){
        this.pollJob.setExitCriterion(exitCriterion);
        this.pollJob.setExitActions(actions);

        return exec(ServiceType.ASYNC);
    }

    public <T> PollResult<T> until(Predicate<? super T>[] exitCriteria, Supplier<? super T>[] actions){
        this.pollJob.setExitCriteria(exitCriteria);
        this.pollJob.setExitActions(actions);

        return exec(ServiceType.ASYNC);
    }

    private <T> PollResult<T> exec(ServiceType serviceType){
        List<PollResult.PollInfo<T>> delayResult = null;
        if(this.delayJob != null)
            delayResult = this.delayJob.executeDelayPollJob();

        List<PollResult.PollInfo<T>> pollResults = this.pollJob.executePollJob(serviceType);

        PollResult<T> result = new PollResult<>();
        result.setDelayResult(delayResult != null ? delayResult.get(0) : null);
        result.setPollResults(pollResults);
        result.setPollStatus(determineStatus(pollResults));
        return result;
    }

    private <T> ResultStatus determineStatus(List<PollResult.PollInfo<T>> pollResults){
        if(pollResults != null && pollResults.size() > 1){
            long nullResponseCount = pollResults.stream()
                                        .filter(r -> r.getLastResponse() == null)
                                        .count();
            if(nullResponseCount > 0)
                return nullResponseCount == pollResults.size() ? ALL_FAILED : ONE_OR_MORE_FAILED;


            long timedOutCount = pollResults.stream()
                                        .filter(PollResult.PollInfo::isTimedOut)
                                        .count();
            if(timedOutCount > 0)
                return timedOutCount == pollResults.size() ? ALL_TIMED_OUT : ONE_OR_MORE_TIMED_OUT;


            return ALL_SUCCESS;
        }

        if(pollResults != null && pollResults.size() == 1) {
            PollResult.PollInfo<T> pollResult = pollResults.get(0);
            T response = pollResult.getLastResponse();
            boolean isTimedOut = pollResult.isTimedOut();

            if (response == null)
                return FAILED;
            else if (isTimedOut)
                return TIMED_OUT;
            else
                return SUCCESS;
        }

        return FAILED;
    }
}
