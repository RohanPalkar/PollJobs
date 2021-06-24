package org.pollfor.service;

import com.google.common.collect.ImmutableMap;
import org.pollfor.api.PollResult;
import org.pollfor.common.Utils;
import org.pollfor.entities.TimeValue;

import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.pollfor.common.Utils.println;

abstract class PollExecutor<T> {

    private static final Boolean DEFAULT_CONITNUE_POLL_FLAG = true;

    private static final Predicate<Long> isTimedOutMillis = t -> t <= 0L;

    private static final Predicate<Long> isTimedOutSeconds = t -> t / 1000 <= 0L;

    private static final Predicate<Long> isTimedOutMinutes = t -> t / 60000 <= 0L;

    private static final Predicate<Long> isTimedOutHours = t -> t / 360000 <= 0L;

    private static final Map<TimeUnit, Predicate<Long>> isTimedOut = ImmutableMap.of(
            TimeUnit.MILLISECONDS, isTimedOutMillis,
            TimeUnit.SECONDS, isTimedOutSeconds,
            TimeUnit.MINUTES, isTimedOutMinutes,
            TimeUnit.HOURS, isTimedOutHours
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

        PollResult.PollInfo<T> pollInfo = new PollResult.PollInfo<>();

        AtomicInteger counter = new AtomicInteger(0);

        LocalTime startTime = LocalTime.now();
        long idto = timeOutInMillis;
        long deadline = System.currentTimeMillis() + idto;

        Predicate<Long> timeOutCheck = isTimedOut.get(timeOut.getUnit());

        T response = null;
        Boolean continuePoll = DEFAULT_CONITNUE_POLL_FLAG;
        while(continuePoll && !timeOutCheck.test(idto)){
            // Iteration as per the counter
            counter.incrementAndGet();

            // Performing the action
            response = (T) action.get();
            if(response == null)
                break;

            // Testing the exit-criteria predicate to determine to continue or not.
            continuePoll = !criterion.test(response);

            // Checking for thread interruption
            if(Thread.interrupted())
                return null;

            // Determining the remaining time for polling
            idto = deadline - System.currentTimeMillis();
            println(idto/1000 + " seconds left", "toPoll:" + continuePoll, idto + "ms left");

            // If we need to continue for poll, we wait, else we break;
            if(continuePoll)
                hardAwait(timeIntervalInMillis);
            else
                break;

            // Checking for thread interruption
            if(Thread.interrupted())
                return null;
        }

        // Setting the response and iteration counter to the result
        pollInfo.setLastResponse(response);
        pollInfo.setPollIterations(counter.intValue());

        // Determining the timed-out status.
        pollInfo.setTimedOut(timeOutCheck.test(idto));

        // Determining the elapsed-time
        LocalTime fendTime = LocalTime.now();
        String elapsedTime = Utils.getElapsedTime(startTime,fendTime);
        pollInfo.setPollDuration(elapsedTime);

        return pollInfo;
    }
}
