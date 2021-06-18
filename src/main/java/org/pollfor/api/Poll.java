package org.pollfor.api;

import org.pollfor.entity.PollInterval;

import java.util.concurrent.TimeUnit;

public class Poll {

    public PollInterval pollFor(Integer pollTimeOut, TimeUnit pollTimeOutUnit){
        return new PollInterval(pollTimeOut, pollTimeOutUnit);
    }

    public PollInterval pollTimes(Integer pollCount){
        return new PollInterval(pollCount);
    }
}
