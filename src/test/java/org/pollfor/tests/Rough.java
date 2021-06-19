package org.pollfor.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pollfor.api.PollConfig;
import org.pollfor.common.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Rough {

    public static void main(String[] args) throws IOException {

        File file = Utils.getResourceFilePath("pollconfig.json");
        PollConfig pollConfig = new ObjectMapper().readValue(file, PollConfig.class);
        System.out.println(pollConfig);

        System.out.println(pollConfig.getTimeOut().getSummary());


        List<Supplier<Integer>> suppliers = new ArrayList<>();

        for(int i = 0 ; i < 10 ; ++i){
            Integer counter = i;
            Supplier<Integer> s = () -> counter;
            suppliers.add(s);
        }

        System.out.println("Hello");
        suppliers.forEach(s -> System.out.println(s.get()));
    }
}
