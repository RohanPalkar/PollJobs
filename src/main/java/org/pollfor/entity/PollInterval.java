package org.pollfor.entity;

import org.pollfor.api.PollAction;

import java.util.concurrent.TimeUnit;

public class PollInterval {

    public Integer pollCount;

    public Integer timeOut;

    public TimeUnit timeOutUnit;

    public Integer timeInterval;

    public TimeUnit timeIntervalUnit;

    public PollInterval(Integer timeOut, TimeUnit timeOutUnit) {
        this.timeOut = timeOut;
        this.timeOutUnit = timeOutUnit;
    }

    public PollInterval(Integer pollCount) {
        this.pollCount = pollCount;
    }

    public PollAction every(Integer timeInterval, TimeUnit timeIntervalUnit){
        this.timeInterval = timeInterval;
        this.timeIntervalUnit = timeIntervalUnit;
        return new PollAction(this);
    }
}
