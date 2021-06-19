package org.pollfor.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

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

    public static class TimeValue {

        private Integer value;
        private TimeUnit unit;

        public TimeValue(){}

        public TimeValue(Integer value, TimeUnit unit) {
            this.value = value;
            this.unit = unit;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public TimeUnit getUnit() {
            return unit;
        }

        public void setUnit(TimeUnit unit) {
            this.unit = unit;
        }

        @JsonIgnore
        public String getSummary(){
            return String.join(" ", value.toString(), unit.toString());
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
