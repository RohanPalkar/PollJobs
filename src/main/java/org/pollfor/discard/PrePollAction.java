package org.pollfor.discard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pollfor.api.PollInterval;

import java.util.function.Predicate;

public class PrePollAction<T> {

    private static final Logger LOGGER = LogManager.getLogger(PrePollAction.class);

    private final PollInterval interval;
    private Long initialDelayMillis;
    private Long initialDelayTimeOutMillis;
    private Long initialDelayTimeIntervalMillis;
    private Predicate<?> entryCriteria;

    public PrePollAction(PollInterval interval) {
        this.interval = interval;

    }

    /*public PollAction holdIf(Predicate<? super T> entryCriteria){
        this.entryCriteria = entryCriteria;

        LOGGER.trace("Using the default time-out and time-interval for polling the initial delay");

        this.initialDelayTimeOutMillis = INITIAL_DELAY_TIME_OUT.getTimeUnitValue()
                .toMillis(INITIAL_DELAY_TIME_OUT.getTimeValue());
        this.initialDelayTimeIntervalMillis = INITIAL_DELAY_TIME_INTERVAL.getTimeUnitValue()
                .toMillis(INITIAL_DELAY_TIME_INTERVAL.getTimeValue());
        LOGGER.trace("Using initial delay with time-out {} ms polling-every {} ms",
                this.initialDelayTimeOutMillis, this.initialDelayTimeIntervalMillis);

        return new PollAction(interval);
    }

    public PollAction holdIf(Predicate<? super T> entryCriteria,
                             Integer initialDelayTimeOut, TimeUnit initialDelayTimeOutUnit,
                             Integer initialDelayTimeInterval, TimeUnit initialDelayTimeIntervalUnit){
        this.entryCriteria = entryCriteria;

        this.initialDelayTimeOutMillis = initialDelayTimeOutUnit.toMillis(initialDelayTimeOut);
        this.initialDelayTimeIntervalMillis = initialDelayTimeIntervalUnit.toMillis(initialDelayTimeInterval);
        LOGGER.trace("Using initial delay with time-out {} ms polling-every {} ms",
                this.initialDelayTimeOutMillis, this.initialDelayTimeIntervalMillis);

        return new PollAction(interval);
    }

    public PollAction holdFor(Integer initialDelay, TimeUnit initialDelayUnit){
        this.initialDelayMillis = initialDelay != null ?
                initialDelayUnit.toMillis(initialDelay) : null;
        LOGGER.trace("Initial delay : {}ms", this.initialDelayMillis);
        return new PollAction(interval);
    }*/
}
