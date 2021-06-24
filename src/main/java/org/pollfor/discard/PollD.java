package org.pollfor.discard;

import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.pollfor.discard.PollDefaults.*;

public class PollD<T> {

    private final AsyncPollAction<T> action;

    private PollD(AsyncPollAction<T> action) {
        this.action = action;
    }

    public static synchronized <T> AsyncPollInterval<T> pollFor(Integer pollTimeOut, TimeUnit pollTimeOutUnit){
        return new AsyncPollInterval(pollTimeOut, pollTimeOutUnit);
    }

    public static synchronized <T> AsyncPollInterval<T>  pollTimes(Integer pollCount){
        return new AsyncPollInterval(pollCount);
    }

    public static class AsyncPollInterval<T> {

        Integer pollCount;

        Integer timeOut;

        TimeUnit timeOutUnit;

        Integer timeInterval;

        TimeUnit timeIntervalUnit;

        public AsyncPollInterval(Integer timeOut, TimeUnit timeOutUnit) {
            this.timeOut = timeOut;
            this.timeOutUnit = timeOutUnit;
        }

        public AsyncPollInterval(Integer pollCount) {
            this.pollCount = pollCount;
        }

        public <T> AsyncPollAction<T> every(Integer timeInterval, TimeUnit timeIntervalUnit){
            this.timeInterval = timeInterval;
            this.timeIntervalUnit = timeIntervalUnit;
            return new AsyncPollAction(this);
        }
    }

    public static class AsyncPollAction<T> {

        AsyncPollInterval<T> interval;
        private final Long timeOutMillis;
        private final Long timeIntervalMillis;
        Predicate<?> entryCriteria;
        Predicate<?> exitCriteria;
        Supplier<?> action;
        Integer initialDelayTimeOut;
        TimeUnit initialDelayTimeOutUnit;
        Integer initialDelayTimeInterval;
        TimeUnit initialDelayTimeIntervalUnit;
        Integer initialDelay;
        TimeUnit initialDelayUnit;

        public AsyncPollAction(AsyncPollInterval<T> interval) {
            this.interval = interval;

        /*
        Determining the Time Interval Millis
         */
            this.timeIntervalMillis = this.interval.timeInterval != null ?
                    this.interval.timeIntervalUnit.toMillis(this.interval.timeInterval) :
                    TIME_INTERVAL.getTimeUnitValue().toMillis(TIME_INTERVAL.getTimeValue());

        /*
        Determining the Time Out Millis
        */
            this.timeOutMillis = this.interval.timeOut != null ?
                    this.interval.timeOutUnit.toMillis(this.interval.timeOut) :
                    this.interval.pollCount != null ?
                            timeIntervalMillis * this.interval.pollCount :
                            TIME_OUT.getTimeUnitValue().toMillis(TIME_OUT.getTimeValue());

        }

        public <T> AsyncPollAction holdIf(Predicate<? super T> entryCriteria){
            this.entryCriteria = entryCriteria;
            return this;
        }

        public <T> AsyncPollAction holdIf(Predicate<? super T> entryCriteria,
                                          Integer initialDelayTimeOut, TimeUnit initialDelayTimeOutUnit,
                                          Integer initialDelayTimeInterval, TimeUnit initialDelayTimeIntervalUnit){
            this.entryCriteria = entryCriteria;
            this.initialDelayTimeOut = initialDelayTimeOut;
            this.initialDelayTimeOutUnit = initialDelayTimeOutUnit;
            this.initialDelayTimeInterval = initialDelayTimeInterval;
            this.initialDelayTimeIntervalUnit = initialDelayTimeIntervalUnit;
            return this;
        }

        public AsyncPollAction holdFor(Integer initialDelay, TimeUnit initialDelayUnit){
            this.initialDelay = initialDelay;
            this.initialDelayUnit = initialDelayUnit;
            return this;
        }

        public<T> PollD<T> until(Predicate<? super T> exitCriteria, Supplier<? super T> action){
            this.exitCriteria = exitCriteria;
            this.action = action;
            return new PollD(this);
        }
    }
}
