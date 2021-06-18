package org.pollfor.common;

import org.apache.logging.log4j.core.lookup.StrSubstitutor;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static synchronized String getElapsedTime(LocalTime startTime, LocalTime endTime){
        Duration d = Duration.between(startTime, endTime);

        if(d.getSeconds() > 0){
            String format = "${hours}:${minutes}:${seconds}";
            Map<String, String> bindings = new HashMap<String, String>(){{
                put("hours", String.valueOf(d.toHours()));
                put("minutes", String.valueOf(d.toMinutes()));
                put("seconds", String.valueOf(d.getSeconds()));
            }};

            return new StrSubstitutor(bindings).replace(format);
        } else {
            return String.join(" ", String.valueOf(d.toMillis()), "ms");
        }
    }
}
