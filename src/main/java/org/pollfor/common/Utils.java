package org.pollfor.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.pollfor.api.PollConfig;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    private static final String DEFAULT_CONFIG_FILE = "defaultPollConfig.json";

    private static final String OVERRIDDEN_CONFIG_FILE = "pollconfig.json";

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

    public static File getResourceFilePath(String path){
        URL url = Thread.currentThread()
                .getContextClassLoader()
                .getResource(path);
        if(url != null && StringUtils.isNotBlank(url.getFile())) {
            return new File(url
                    .getFile()
                    .trim()
                    .replaceAll("%20", " "));
        }
        return null;
    }

    public static synchronized PollConfig getPollConfig(){
        File overridenFile = getResourceFilePath(OVERRIDDEN_CONFIG_FILE);
        File defaultFile = getResourceFilePath(DEFAULT_CONFIG_FILE);

        File configFile = overridenFile.exists() ? overridenFile : defaultFile;

        try {
            return new ObjectMapper().readValue(configFile, PollConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
