package org.pollfor.api;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pollfor.common.Utils;
import org.pollfor.entity.PollInterval;
import org.pollfor.entity.PollResult;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.pollfor.entity.PollDefaults.*;

public class PollAction {

    private static final Logger LOGGER = LogManager.getLogger(PollAction.class);

    private static final Predicate<Long> isTimedOut = t -> t <= 0L;

    private final PollInterval interval;
    private final Long timeOutMillis;
    private final Long timeIntervalMillis;
    private String initialElapsedTime;
/*private Long initialDelayMillis;
private Long initialDelayTimeOutMillis;
private Long initialDelayTimeIntervalMillis;
private Predicate<?> entryCriteria;
private Predicate<?> exitCriteria;*/

    public PollAction(PollInterval interval) {
        this.interval = interval;

    /*
    Determining the Time Interval Millis
     */
        if(this.interval.timeInterval != null){
            this.timeIntervalMillis = this.interval.timeIntervalUnit.toMillis(this.interval.timeInterval);
            LOGGER.trace("Using user-input time-interval : {}ms", this.timeIntervalMillis);
        } else {
            this.timeIntervalMillis = TIME_INTERVAL.getTimeUnitValue()
                    .toMillis(TIME_INTERVAL.getTimeValue());
            LOGGER.trace("Using the default time-interval : {}ms", this.timeIntervalMillis);
        }

    /*
    Determining the Time Out Millis
     */
        if(this.interval.timeOut != null) {
            this.timeOutMillis = this.interval.timeOutUnit.toMillis(this.interval.timeOut);
            LOGGER.trace("Using user-input time-out : {}ms", this.timeOutMillis);
        } else if (this.interval.pollCount != null){
            this.timeOutMillis = timeIntervalMillis * this.interval.pollCount;
            LOGGER.trace("Computing time-out using poll-count {} : {}ms",
                    this.interval.pollCount, this.timeOutMillis);
        } else {
            this.timeOutMillis = TIME_OUT.getTimeUnitValue()
                    .toMillis(TIME_OUT.getTimeValue());
            LOGGER.trace("Using default time-out : {}ms", this.timeOutMillis);
        }
    }

    public <T> PollAction holdIf(Predicate<? super T> entryCriteria, Supplier<? super T> action){
        //this.entryCriteria = entryCriteria;

        LOGGER.trace("Using the default time-out and time-interval for polling the initial delay");

    /*this.initialDelayTimeOutMillis = INITIAL_DELAY_TIME_OUT.getTimeUnitValue()
            .toMillis(INITIAL_DELAY_TIME_OUT.getTimeValue());
    this.initialDelayTimeIntervalMillis = INITIAL_DELAY_TIME_INTERVAL.getTimeUnitValue()
            .toMillis(INITIAL_DELAY_TIME_INTERVAL.getTimeValue());
    LOGGER.trace("Using initial delay with time-out {} ms polling-every {} ms",
            this.initialDelayTimeOutMillis, this.initialDelayTimeIntervalMillis);*/

        return holdIf(entryCriteria, action,
                INITIAL_DELAY_TIME_OUT.getTimeValue(), INITIAL_DELAY_TIME_OUT.getTimeUnitValue(),
                INITIAL_DELAY_TIME_INTERVAL.getTimeValue(), INITIAL_DELAY_TIME_INTERVAL.getTimeUnitValue());
    }

    public <T> PollAction holdIf(Predicate<? super T> entryCriteria, Supplier<? super T> action,
                                 Integer initialDelayTimeOut, TimeUnit initialDelayTimeOutUnit,
                                 Integer initialDelayTimeInterval, TimeUnit initialDelayTimeIntervalUnit){
        //this.entryCriteria = entryCriteria;

    /*this.initialDelayTimeOutMillis = initialDelayTimeOutUnit.toMillis(initialDelayTimeOut);
    this.initialDelayTimeIntervalMillis = initialDelayTimeIntervalUnit.toMillis(initialDelayTimeInterval);*/
        Long initialDelayTimeOutMillis = initialDelayTimeOutUnit.toMillis(initialDelayTimeOut);
        Long initialDelayTimeIntervalMillis = initialDelayTimeIntervalUnit.toMillis(initialDelayTimeInterval);
        LOGGER.trace("Using initial delay with time-out {} ms polling-every {} ms",
                initialDelayTimeOutMillis, initialDelayTimeIntervalMillis);

        final LocalTime startTime = LocalTime.now();
        long idto = initialDelayTimeOutMillis;
        final long deadline = System.currentTimeMillis() + idto;

        T response = null;
        boolean continueDelay = true;
        while(continueDelay && !isTimedOut.test(idto)){
            response = (T) action.get();

            continueDelay = !entryCriteria.test(response);

            if(Thread.interrupted())
                return null;

            idto = deadline - System.currentTimeMillis();

            if(continueDelay)
                internalAwait(initialDelayTimeIntervalMillis);
            else
                break;
        }

        if(idto <= 0L)
            LOGGER.debug("Initial time-out for the entry-criteria was completely exhausted");


        final LocalTime endTime = LocalTime.now();
        this.initialElapsedTime = Utils.getElapsedTime(startTime, endTime);
        LOGGER.trace("Initial delay-elapsed-time : {}", this.initialElapsedTime);
        return this;
    }

    public PollAction holdFor(Integer initialDelay, TimeUnit initialDelayUnit){
        Long initialDelayMillis = initialDelay != null ?
                initialDelayUnit.toMillis(initialDelay) : null;
        LOGGER.trace("Initial delay of {} ms present", initialDelayMillis);
        internalAwait(initialDelayMillis);
        LOGGER.trace("Initial wait of {} ms completed", initialDelayMillis);
        return this;
    }

    public<T> PollResult<T> until(Predicate<? super T> exitCriteria, Supplier<? super T> action){
        PollResult<T> result = new PollResult();
        final LocalTime startTime = LocalTime.now();

        if(StringUtils.isNotEmpty(this.initialElapsedTime))
            result.setInitialDelay(initialElapsedTime);

        LOGGER.trace("Final polling for exit-criteria");
        AtomicInteger counter = new AtomicInteger(0);
        final LocalTime fstartTime = LocalTime.now();
        long to = this.timeOutMillis;
        final long todeadline = System.currentTimeMillis() + to;

        T response = null;
        boolean continuePoll = true;
        while(continuePoll && !isTimedOut.test(to)){

            // Iteration as per the counter
            counter.incrementAndGet();

            // Performing the action
            response = (T) action.get();

            // Testing the exit-criteria predicate to determine to continue or not.
            continuePoll = !exitCriteria.test(response);

            // Checking for thread interruption
            if(Thread.interrupted())
                return null;

            // Determining the remaining time for polling
            to = todeadline - System.currentTimeMillis();
            LOGGER.debug("Polling iteration #{}\nLast-response : {}\nRemaining-time : {}ms",
                    counter.intValue(), response.toString(), to);

            // If we need to continue for poll, we wait, else we break;
            if(continuePoll)
                internalAwait(this.timeIntervalMillis);
            else
                break;
        }

        // Setting the response and iteration counter to the result
        result.setLastResponse(response);
        result.setIterations(counter.intValue());

        // Determining the timed-out status.
        if(to <= 0L) {
            LOGGER.debug("Polling time-out for the exit-criteria was completely exhausted. Timed-out : true");
            result.setTimedOut(true);
        } else {
            LOGGER.debug("Polling completed with the time-out-limit with remaining-time : {}ms", to);
            result.setTimedOut(false);
        }

        // Determining the elapsed-time
        final LocalTime fendTime = LocalTime.now();
        final String elapsedTime = Utils.getElapsedTime(fstartTime,fendTime);
        LOGGER.debug("Polling-elapsed-time : {}", elapsedTime);
        result.setDuration(elapsedTime);

        LOGGER.debug("Polling Result : {}", result);
        return result;
    }

    private void internalAwait(Long milliSeconds){
        LOGGER.debug("Waiting for {}ms", milliSeconds);
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {}
    }
}
