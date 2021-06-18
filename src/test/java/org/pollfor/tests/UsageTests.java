package org.pollfor.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.pollfor.api.Poll;
import org.pollfor.api.PollDefinition;
import org.pollfor.entity.PollResult;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

import static java.util.concurrent.TimeUnit.*;
import static org.junit.jupiter.api.Assertions.*;

public class UsageTests {

    private static final Logger logger = LogManager.getLogger(UsageTests.class);


    private static Predicate<String> pred = s -> s.equals("Hello");
    private static Predicate<Integer> predOn = i -> i == 100;
    private static Predicate<Integer> predtimeOut = i -> i == 150;

    @Test //(description = "Timed Out Case")
    public void testPollingTimeOut() throws ExecutionException, InterruptedException {
        TestCounter tc = new TestCounter();
        tc.getStatus();
        CompletableFuture<String> c = CompletableFuture.supplyAsync(() -> tc.call());

        CompletableFuture<PollResult<String>> r
                = CompletableFuture.supplyAsync(() -> new Poll()
                .pollFor(5, SECONDS)
                .every(1, SECONDS)
                .until(s -> s.equals("COMPLETED"), () -> tc.getStatus()));

        CompletableFuture<Object> combinedFuture = CompletableFuture.anyOf(c,r);
        combinedFuture.get();

        PollResult<String> p = r.get();
        assertTrue(p.isTimedOut(), "Failed : Timed-Out check");
        assertEquals(p.getIterations(), 6, "Failed : No. of iterations check");
        assertEquals(p.getLastResponse(), "SUBMITTED", "Failed : Last response check");
    }


    @Test //(description = "Completed case")
    public void testPollingCompleted() throws ExecutionException, InterruptedException {
        TestCounter tc = new TestCounter();
        tc.getStatus();
        CompletableFuture<String> c = CompletableFuture.supplyAsync(() -> tc.call());

        CompletableFuture<PollResult<String>> r
                = CompletableFuture.supplyAsync(() -> new Poll()
                .pollFor(40, SECONDS)
                .every(3, SECONDS)
                .until(s -> s.equals("COMPLETED"), () -> tc.getStatus()));

        CompletableFuture<Object> combinedFuture = CompletableFuture.anyOf(c,r);
        combinedFuture.get();

        PollResult<String> p = r.get();
        System.out.println("Result : "+p);
        assertFalse(p.isTimedOut(), "Failed : Timed-Out check");
        assertEquals(p.getIterations(), 11, "Failed : No. of iterations check");
        assertEquals(p.getLastResponse(), "COMPLETED", "Failed : Last response check");
    }

    @Test //(description = "Completed case")
    public void testPollingCompletedSameInterval () throws ExecutionException, InterruptedException {
        TestCounter tc = new TestCounter();
        tc.getStatus();
        CompletableFuture<String> c = CompletableFuture.supplyAsync(() -> tc.call());

        CompletableFuture<PollResult<String>> r
                = CompletableFuture.supplyAsync(() -> new Poll()
                .pollFor(40, SECONDS)
                .every(2, SECONDS)
                .until(s -> s.equals("COMPLETED"), () -> tc.getStatus()));

        CompletableFuture<Object> combinedFuture = CompletableFuture.anyOf(c,r);
        combinedFuture.get();

        PollResult<String> p = r.get();
        System.out.println("Result : "+p);
        assertFalse(p.isTimedOut(), "Failed : Timed-Out check");
        assertEquals(p.getIterations(), 15, "Failed : No. of iterations check");
        assertEquals(p.getLastResponse(), "COMPLETED", "Failed : Last response check");
    }

    @Test //(description = "Completed case")
    public void testPollingCompletedHoldPredicate () throws ExecutionException, InterruptedException {
        TestCounter tc = new TestCounter();
        tc.getStatus();
        CompletableFuture<String> c = CompletableFuture.supplyAsync(() -> tc.call());

        CompletableFuture<PollResult<String>> r
                = CompletableFuture.supplyAsync(() -> new Poll()
                .pollFor(40, SECONDS)
                .every(2, SECONDS)
                .holdIf(s -> s.equals("IN PROGRESS"), () -> tc.getStatus())
                .until(s -> s.equals("COMPLETED"), () -> tc.getStatus()));

        CompletableFuture<Object> combinedFuture = CompletableFuture.anyOf(c,r);
        combinedFuture.get();

        PollResult<String> p = r.get();
        System.out.println("Result : "+p);
        assertFalse(p.isTimedOut(), "Failed : Timed-Out check");
        assertEquals(p.getIterations(), 8, "Failed : No. of iterations check");
        assertEquals(p.getLastResponse(), "COMPLETED", "Failed : Last response check");
    }

    @Test //(description = "Completed case")
    public void testPollingCompletedHoldPredicateCustomDelay () throws ExecutionException, InterruptedException {
        TestCounter tc = new TestCounter();
        tc.getStatus();
        CompletableFuture<String> c = CompletableFuture.supplyAsync(() -> tc.call());

        CompletableFuture<PollResult<String>> r
                = CompletableFuture.supplyAsync(() -> new Poll()
                .pollFor(40, SECONDS)
                .every(2, SECONDS)
                .holdIf(s -> s.equals("IN PROGRESS"), () -> tc.getStatus(),1, MINUTES, 2, SECONDS)
                .until(s -> s.equals("COMPLETED"), () -> tc.getStatus()));

        CompletableFuture<Object> combinedFuture = CompletableFuture.anyOf(c,r);
        combinedFuture.get();

        PollResult<String> p = r.get();
        System.out.println("Result : "+p);
        assertFalse(p.isTimedOut(), "Failed : Timed-Out check");
        assertEquals(p.getIterations(), 11, "Failed : No. of iterations check");
        assertEquals(p.getInitialDelay(), "0:0:8", "Failed : Initial delay check failed");
        assertEquals(p.getLastResponse(), "COMPLETED", "Failed : Last response check");
    }

    @Test
    public void testAsyncPolling(){
        TestCounter tc = new TestCounter();
        PollDefinition<String> def =
                PollDefinition
                        .pollTimes(4)
                        .every(5, SECONDS)
                        .until(s -> s.equals("COMPLETED"), () -> tc.getStatus());
    }

    public static class TestCounter implements Callable<String> {

        private int counter = 1;
        private String status = "OPEN";
        private final long sleepInterval = 2000;
        private final int iterations = 20;

        public void reset(){
            counter = 1;
        }

        public synchronized int getCounter(){
            return counter;
        }

        public synchronized String getStatus(){
            return status;
        }


        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * @return computed result
         * @throws Exception if unable to compute a result
         */
        @Override
        public String call()  {
            for(;counter < this.iterations ; ++counter){
                try {
                    logger.info("Counter : {}", counter);
                    if(counter >= 15)
                        status = "COMPLETED";
                    else if(counter >= 5)
                        status = "IN PROGRESS";
                    else
                        status = "SUBMITTED";
                    Thread.sleep(this.sleepInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return status;
        }
    }

}
