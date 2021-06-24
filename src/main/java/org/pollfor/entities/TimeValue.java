package org.pollfor.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.concurrent.TimeUnit;

@JsonDeserialize(builder = TimeValue.TimeValueBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class TimeValue {

    private final Integer value;
    private final TimeUnit unit;

    private TimeValue(TimeValueBuilder builder) {
        this.value = builder.value;
        this.unit = builder.unit;
    }

    public static TimeValueBuilder create(){
        return new TimeValueBuilder();
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class TimeValueBuilder {
        private Integer value;
        private TimeUnit unit;

        public TimeValueBuilder setValue(Integer value){
            this.value = value;
            return this;
        }

        public TimeValueBuilder setUnit(TimeUnit unit){
            this.unit = unit;
            return this;
        }

        public TimeValue build(){
            return new TimeValue(this);
        }
    }

    public Integer getValue() {
        return value;
    }

    public TimeUnit getUnit() {
        return unit;
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
