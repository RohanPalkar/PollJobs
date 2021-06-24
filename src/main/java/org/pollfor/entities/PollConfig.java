package org.pollfor.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class PollConfig {

    @JsonIgnore
    private Integer iterations;
    private TimeValue timeOut;
    private TimeValue timeInterval;
    private TimeValue initialDelayTimeOut;
    private TimeValue initialDelayTimeInterval;

    public Integer getIterations() {
        return iterations;
    }

    public void setIterations(Integer iterations) {
        this.iterations = iterations;
    }

    public TimeValue getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(TimeValue timeOut) {
        this.timeOut = timeOut;
    }

    public TimeValue getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(TimeValue timeInterval) {
        this.timeInterval = timeInterval;
    }

    public TimeValue getInitialDelayTimeOut() {
        return initialDelayTimeOut;
    }

    public void setInitialDelayTimeOut(TimeValue initialDelayTimeOut) {
        this.initialDelayTimeOut = initialDelayTimeOut;
    }

    public TimeValue getInitialDelayTimeInterval() {
        return initialDelayTimeInterval;
    }

    public void setInitialDelayTimeInterval(TimeValue initialDelayTimeInterval) {
        this.initialDelayTimeInterval = initialDelayTimeInterval;
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
