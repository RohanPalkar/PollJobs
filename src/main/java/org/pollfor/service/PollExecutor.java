package org.pollfor.service;

import com.google.common.collect.ImmutableMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pollfor.api.PollResult;
import org.pollfor.common.Utils;
import org.pollfor.entities.TimeValue;

import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.pollfor.common.Utils.println;

abstract class PollExecutor<T> {

    private static final Logger LOGGER = LogManager.getLogger(PollExecutor.class);

    private static final Boolean DEFAULT_CONITNUE_POLL_FLAG = true;

    private static final Predicate<Long> isTimedOut = t -> t <= 0L;

    private static final Function<Long, Long> toMillis = t -> t;

    private static final Function<Long, Long> toSeconds = t -> t / 1000;

    private static final Function<Long, Long> toMinutes = t -> t / 60000;

    private static final Function<Long, Long> toHours = t -> t / 360000;

    private static final Map<TimeUnit, Function<Long, Long>> timeUnitMap = ImmutableMap.of(
            TimeUnit.MILLISECONDS, toMillis,
            TimeUnit.SECONDS, toSeconds,
            TimeUnit.MINUTES, toMinutes,
            TimeUnit.HOURS, toHours
    );

    protected void hardAwait(Long milliSeconds){
        try {
            if(!Thread.interrupted())
                Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected <T> PollResult.PollInfo<T> actionAwait(Predicate<? super T> criterion,
                                                     Supplier<? super T> action,
                                                     TimeValue timeOut,
                                                     long timeOutInMillis,
                                                     TimeValue timeInterval,
                                                     long timeIntervalInMillis){

        LOGGER.debug("Polling with time-out: {} ms, time-interval: {} ms",
                timeOutInMillis, timeIntervalInMillis);
        println("Polling with time-out: "+timeOutInMillis+" ms, time-interval: "+timeIntervalInMillis+" ms");

        PollResult.PollInfo<T> pollInfo = new PollResult.PollInfo<>();

        AtomicInteger counter = new AtomicInteger(0);

        LocalTime startTime = LocalTime.now();
        long idto = timeOutInMillis;
        long deadline = System.currentTimeMillis() + idto;

        Function<Long, Long> convert = timeUnitMap.get(timeOut.getUnit());

        T response = null;
        Boolean continuePoll = DEFAULT_CONITNUE_POLL_FLAG;
        while(continuePoll && !isTimedOut.test(convert.apply(idto))){
            // Iteration as per the counter
            counter.incrementAndGet();

            // Performing the action
            println("Performing the action", "iteration:"+counter);
            response = (T) action.get();

            // Testing the exit-criteria predicate to determine to continue or not.
            println("Testing the exit-criteria", "iteration:"+counter);
            continuePoll = !criterion.test(response);
            //println("Continue Poll: "+continuePoll, "i"+counter);

            // Checking for thread interruption
            if(Thread.interrupted())
                return null;

            // Determining the remaining time for polling
            idto = deadline - System.currentTimeMillis();
            println("Remaining-Time: "+idto+" ms", "iteration:"+counter);

            // If we need to continue for poll, we wait, else we break;
            if(continuePoll && !isTimedOut.test(convert.apply(idto))) {
                println("Hard wait for "+timeIntervalInMillis+" ms", "iteration:"+counter);
                hardAwait(timeIntervalInMillis);
            } else
                break;

            // Checking for thread interruption
            if(Thread.interrupted())
                return null;
        }

        println("Done, preparing result", "iteration:"+counter);
        // Setting the response and iteration counter to the result
        pollInfo.setLastResponse(response);
        pollInfo.setPollIterations(counter.intValue());

        // Determining the timed-out status.
        pollInfo.setTimedOut(isTimedOut.test(convert.apply(idto)));

        // Determining the elapsed-time
        LocalTime fendTime = LocalTime.now();
        String elapsedTime = Utils.getElapsedTime(startTime,fendTime);
        pollInfo.setPollDuration(elapsedTime);

        return pollInfo;
    }
}
