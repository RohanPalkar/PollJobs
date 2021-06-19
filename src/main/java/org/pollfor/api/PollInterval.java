package org.pollfor.api;

import java.util.concurrent.TimeUnit;

public class PollInterval {

    final PollConfig pollConfig;

    public PollInterval(Integer timeOut, TimeUnit timeOutUnit) {
        this.pollConfig = new PollConfig();
        pollConfig.setTimeOut(new PollConfig.TimeValue(timeOut, timeOutUnit));
    }

    public PollInterval(Integer pollCount) {
        this.pollConfig = new PollConfig();
        this.pollConfig.setIterations(pollCount);
    }

    public PollAction every(Integer timeInterval, TimeUnit timeIntervalUnit){
        this.pollConfig.setTimeInterval(new PollConfig.TimeValue(timeInterval, timeIntervalUnit));
        return new PollAction(this.pollConfig);
    }
}
