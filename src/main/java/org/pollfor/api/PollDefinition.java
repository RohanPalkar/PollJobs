package org.pollfor.api;

import java.util.concurrent.TimeUnit;

public class PollDefinition {

    public static PollInterval pollFor(Integer pollTimeOut, TimeUnit pollTimeOutUnit){
        return new PollInterval(pollTimeOut, pollTimeOutUnit);
    }

    public static PollInterval pollFor(Integer pollIterations){
        return new PollInterval(pollIterations);
    }
}
