package org.pollfor.sdk;

import org.pollfor.api.PollResult;

import java.util.List;

@FunctionalInterface
public interface ActionExecutor {

    <T> List<PollResult.PollInfo<T>> executePoll();
}
