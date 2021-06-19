package org.pollfor.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;


public class PollAction extends Executor {

    private static final Logger LOGGER = LogManager.getLogger(PollAction.class);

    private static final Predicate<Long> isTimedOut = t -> t <= 0L;



    private String initialElapsedTime;

    public PollAction(PollConfig pollConfig) {
        super(pollConfig);
    }

    public <T> PollAction holdUntil(Predicate<? super T> entryCriterion, Supplier<? super T> action){
        this.def.setEntryCriterion(entryCriterion);
        this.def.setEntryAction(action);
        return this;
    }



    public PollAction holdFor(Integer initialDelay, TimeUnit initialDelayUnit){

        this.def.getPollConfig()
                .setInitialDelayTimeOut(new PollConfig.TimeValue(initialDelay, initialDelayUnit));

        this.def.setInitialDelayMillis(initialDelay != null ?
                initialDelayUnit.toMillis(initialDelay) : null);

        return this;
    }

    public<T> PollResult<T> until(Predicate<? super T> exitCriterion, Supplier<? super T> action){
        this.def.setExitCriteria(exitCriterion);
        this.def.setExitActions(action);

        return null;
    }

    public<T> PollResult<T> until(Predicate<? super T> exitCriterion, Supplier<? super T>... actions){
        this.def.setExitCriteria(exitCriterion);
        this.def.setExitActions(actions);

        return null;
    }

    public<T> PollResult<T> until(Predicate<? super T>[] exitCriteria, Supplier<? super T>[] actions){
        this.def.setExitCriteria(exitCriteria);
        this.def.setExitActions(actions);

        return null;
    }
}
