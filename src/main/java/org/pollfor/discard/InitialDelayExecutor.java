package org.pollfor.discard;

import org.pollfor.entities.PollConfig;
import org.pollfor.service.WaitExecutor;

import java.util.concurrent.TimeUnit;

public class InitialDelayExecutor {

    public InitialDelayExecutor(PollConfig pollConfig, Integer waitTime, TimeUnit waitTimeUnit) {
        //super(pollConfig, waitTime, waitTimeUnit);
        /*this.pollDefinition
                .getPollConfig()
                .setInitialDelayTimeOut(new PollConfig.TimeValue(waitTime, waitTimeUnit));*/

        /*this.pollDefinition
                .setInitialDelayMillis(waitTimeInMillis);*/
    }
}
