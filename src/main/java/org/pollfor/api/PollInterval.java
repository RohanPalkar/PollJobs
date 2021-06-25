package org.pollfor.api;

import org.pollfor.entities.TimeValue;

import java.util.concurrent.TimeUnit;

public class PollInterval {

    private final String pollName;
    private final TimeValue pollTimeValue;
    private final Integer pollCount;

    public PollInterval(Integer timeOut, TimeUnit timeOutUnit, String name) {
        this.pollName = name;
        this.pollCount = null;
        this.pollTimeValue = TimeValue.create()
                                        .setValue(timeOut)
                                        .setUnit(timeOutUnit)
                                        .build();


    }

    public PollInterval(Integer pollCount, String name) {
        this.pollName = name;
        this.pollCount = pollCount;
        this.pollTimeValue = null;
    }

    public PollAction every(Integer timeInterval, TimeUnit timeIntervalUnit){
        TimeValue pollIntervalTimeValue = TimeValue.create()
                                                    .setValue(timeInterval)
                                                    .setUnit(timeIntervalUnit)
                                                    .build();

        PollJob pollJob = PollJob.createJob()
                                    .setName(this.pollName)
                                    .setIterations(this.pollCount)
                                    .setTimeOut(this.pollTimeValue)
                                    .setTimeInterval(pollIntervalTimeValue)
                                    .build();

        return new PollAction(pollJob);
    }

    public PollAction defaultInterval(){
        return new PollAction(PollJob.createJob()
                                    .setName(this.pollName)
                                    .build());
    }
}
