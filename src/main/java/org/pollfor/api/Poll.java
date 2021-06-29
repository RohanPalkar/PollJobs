package org.pollfor.api;

import java.util.concurrent.TimeUnit;

public class Poll {

    private static final Poll instance = new Poll();
    private String pollName;

    private Poll() {}

    public static Poll withName(String pollName){
        instance.pollName = pollName;
        return instance;
    }

    public PollInterval pollFor(Integer pollTimeOut, TimeUnit pollTimeOutUnit){
        return new PollInterval(pollTimeOut, pollTimeOutUnit, this.pollName);
    }

    public PollInterval pollFor(Integer pollIterations){
        return new PollInterval(pollIterations, this.pollName);
    }

    public PollInterval pollWithDefaults(){
        return new PollInterval(null, this.pollName);
    }
}
