package org.pollfor.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.pollfor.entities.PollConfig;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private static final ThreadLocal<SimpleDateFormat> format =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS", Locale.US));

    public static String debugStamp(){
        return format.get().format(new Date()).split(" ")[1];
    }

    public static synchronized void println(String message, String... tags){
        if(System.getenv("POLL_DEBUG") != null &&
                System.getenv("POLL_DEBUG").equalsIgnoreCase("true")){

            List<String> fishTags = new ArrayList<>();
            fishTags.add(debugStamp());
            fishTags.add(   Thread.currentThread().getName());
            fishTags.addAll(Stream.of(tags).collect(Collectors.toList()));

            String tagString = fishTags
                                    .stream()
                                    .map(t -> "["+t+"]")
                                    .collect(Collectors.joining());

            System.out.println(tagString+" "+message);
        }
    }
}
