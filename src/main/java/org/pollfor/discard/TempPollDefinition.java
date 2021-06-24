package org.pollfor.discard;

import org.pollfor.api.PollInterval;

import java.util.concurrent.TimeUnit;

public class TempPollDefinition {

    public static PollInterval pollFor(Integer pollTimeOut, TimeUnit pollTimeOutUnit){
        return null;
        //return new PollInterval(pollTimeOut, pollTimeOutUnit);
    }

    public static PollInterval pollFor(Integer pollIterations){

        return null;
        //return new PollInterval(pollIterations);
    }
}
