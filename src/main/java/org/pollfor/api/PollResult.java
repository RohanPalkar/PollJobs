package org.pollfor.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class PollResult<T> {

    private String pollName;
    private ResultStatus pollStatus;
    private PollInfo<T> delayResult;
    private List<PollInfo<T>> pollResults;

    public String getPollName() {
        return pollName;
    }

    public void setPollName(String pollName) {
        this.pollName = pollName;
    }

    public ResultStatus getPollStatus() {
        return pollStatus;
    }

    public void setPollStatus(ResultStatus pollStatus) {
        this.pollStatus = pollStatus;
    }

    public PollInfo<T> getDelayResult() {
        return delayResult;
    }

    public void setDelayResult(PollInfo<T> delayResult) {
        this.delayResult = delayResult;
    }

    public List<PollInfo<T>> getPollResults() {
        return pollResults;
    }

    public PollInfo<T> getPollResult() {
        return pollResults != null && !pollResults.isEmpty() ? pollResults.get(0) : new PollInfo<>();
    }

    public void addPollResult(PollInfo<T> pollResult){
        if(this.pollResults == null)
            this.pollResults = new ArrayList<>();
        this.pollResults.add(pollResult);
    }

    public void setPollResults(List<PollInfo<T>> pollResults) {
        this.pollResults = pollResults;
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

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
            getterVisibility = JsonAutoDetect.Visibility.NONE,
            setterVisibility = JsonAutoDetect.Visibility.NONE)
    public static class PollInfo<R> {

        private String pollTimeOut;
        private String pollInterval;
        private String hardWait;
        private int pollIterations;
        private String pollDuration;
        private boolean isTimedOut;
        private R lastResponse;

        public String getPollTimeOut() {
            return pollTimeOut;
        }

        public void setPollTimeOut(String pollTimeOut) {
            this.pollTimeOut = pollTimeOut;
        }

        public String getPollInterval() {
            return pollInterval;
        }

        public void setPollInterval(String pollInterval) {
            this.pollInterval = pollInterval;
        }

        public String getHardWait() {
            return hardWait;
        }

        public void setHardWait(String hardWait) {
            this.hardWait = hardWait;
        }

        public int getPollIterations() {
            return pollIterations;
        }

        public void setPollIterations(int pollIterations) {
            this.pollIterations = pollIterations;
        }

        public String getPollDuration() {
            return pollDuration;
        }

        public void setPollDuration(String pollDuration) {
            this.pollDuration = pollDuration;
        }

        @JsonProperty("isTimedOut")
        public boolean isTimedOut() {
            return isTimedOut;
        }

        public void setTimedOut(boolean timedOut) {
            isTimedOut = timedOut;
        }

        public R getLastResponse() {
            return lastResponse;
        }

        public void setLastResponse(R lastResponse) {
            this.lastResponse = lastResponse;
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
}
