package org.pollfor.api;

import org.apache.commons.lang3.StringUtils;
import org.pollfor.common.Utils;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.pollfor.api.PollDefaults.TIME_INTERVAL;
import static org.pollfor.api.PollDefaults.TIME_OUT;

abstract class Executor {

    protected final InternalPollDef def;

    protected Executor(PollConfig pollConfig) {

        PollConfig.TimeValue timeInterval = pollConfig.getTimeInterval();
        long timeIntervalMillis = timeInterval != null ?
                timeInterval.getUnit().toMillis(timeInterval.getValue()) :
                TIME_INTERVAL.getTimeUnitValue().toMillis(TIME_INTERVAL.getTimeValue());

        //Determining the Time Out Millis
        PollConfig.TimeValue timeOut = pollConfig.getTimeOut();
        long timeOutMillis = timeOut != null ?
                timeOut.getUnit().toMillis(timeOut.getValue()) :
                pollConfig.getIterations() != null ?
                        timeIntervalMillis * pollConfig.getIterations() :
                        TIME_OUT.getTimeUnitValue().toMillis(TIME_OUT.getTimeValue());

        this.def = InternalPollDef.create()
                .setPollConfig(pollConfig)
                .setTimeOutMillis(timeOutMillis)
                .setTimeIntervalMillis(timeIntervalMillis)
                .build();
    }


    protected void internalAwait(Long milliSeconds){
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {}
    }

    /*public <T> PollAction holdUntil(Predicate<? super T> entryCriteria, Supplier<? super T> action,
                                    Integer initialDelayTimeOut, TimeUnit initialDelayTimeOutUnit,
                                    Integer initialDelayTimeInterval, TimeUnit initialDelayTimeIntervalUnit){
        //this.entryCriteria = entryCriteria;

    *//*this.initialDelayTimeOutMillis = initialDelayTimeOutUnit.toMillis(initialDelayTimeOut);
    this.initialDelayTimeIntervalMillis = initialDelayTimeIntervalUnit.toMillis(initialDelayTimeInterval);*//*
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
    }*/


}
