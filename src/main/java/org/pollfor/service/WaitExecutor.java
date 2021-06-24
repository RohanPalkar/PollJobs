package org.pollfor.service;

import org.pollfor.entities.PollConfig;
import org.pollfor.api.PollResult;
import org.pollfor.common.Utils;
import org.pollfor.sdk.ActionExecutor;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WaitExecutor extends PollExecutor implements ActionExecutor {

    protected final Integer waitTime;
    protected final TimeUnit waitTimeUnit;
    protected final long waitTimeInMillis;

    public WaitExecutor(Integer waitTime, TimeUnit waitTimeUnit, Long waitTimeInMillis){
        this.waitTime = waitTime;
        this.waitTimeUnit = waitTimeUnit;
        this.waitTimeInMillis= waitTimeInMillis;
    }

    @Override
    public <T> List<PollResult.PollInfo<T>> executePoll() {
        LocalTime startTime = LocalTime.now();

        hardAwait(this.waitTimeInMillis);

        LocalTime endTime = LocalTime.now();
        String elapsedTime = Utils.getElapsedTime(startTime,endTime);

        PollResult.PollInfo<T> pollInfo = new PollResult.PollInfo<>();
        pollInfo.setHardWait(String.join(" ", waitTime.toString(), waitTimeUnit.toString()));
        pollInfo.setPollDuration(elapsedTime);
        return Arrays.asList(pollInfo);
    }
}
