package org.pollfor.api;

import java.util.concurrent.TimeUnit;

public class Poll {

    private final String pollName;

    public Poll() {
        this.pollName = null;
    }

    public Poll(String pollName) {
        this.pollName = pollName;
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
