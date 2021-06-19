package org.pollfor.api;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PollResult<R> {

    private int iterations;

    private String initialDelay = "0";

    private String duration;

    private boolean timedOut;

    private R lastResponse;

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isTimedOut() {
        return timedOut;
    }

    public void setTimedOut(boolean timedOut) {
        this.timedOut = timedOut;
    }

    public R getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(R lastResponse) {
        this.lastResponse = lastResponse;
    }

    public String getInitialDelay() {

        return initialDelay;
    }

    public void setInitialDelay(String initialDelay) {
        this.initialDelay = initialDelay;
    }

    @Override
    public String toString() {
        try{
            return new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(this);
        } catch (com.fasterxml.jackson.core.JsonProcessingException j) {
            throw new RuntimeException(j);
        }
    }
}
