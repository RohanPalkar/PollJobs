package org.pollfor.tests.sync;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.pollfor.api.Poll;
import org.pollfor.api.PollResult;
import org.pollfor.tests.MockJobSystem;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.pollfor.api.ResultStatus.*;

public class BasicPollingTests {

    @Test
    public void basicSyncTimeOutPollingTest1() throws ExecutionException, InterruptedException {
        MockJobSystem tc = new MockJobSystem();
        ExecutorService customExecutor = Executors.newFixedThreadPool(2, new ThreadFactoryBuilder()
                .setNameFormat("BSTOPT1-%d")
                .setThreadFactory(Executors.defaultThreadFactory())
                .build());
        CompletableFuture<String> c = CompletableFuture.supplyAsync(tc::call, customExecutor);

        CompletableFuture<PollResult<String>> r = CompletableFuture.supplyAsync(() ->
                Poll.withName(null)
                .pollFor(5, SECONDS)
                .every(1, SECONDS)
                .until(s -> s.equals("COMPLETED"), tc::getStatus), customExecutor);

        CompletableFuture<Object> combinedFuture = CompletableFuture.anyOf(c,r);
        combinedFuture.get();

        PollResult<String> p = r.get();
        tc.cancelJob();
        System.out.println("Result : "+p);

        assertTrue(p.getPollResult().isTimedOut(), "Failed : Timed-Out check");
        assertEquals("0:0:4", p.getPollResult().getPollDuration(), "Failed: Duration check");
        assertEquals(5, p.getPollResult().getPollIterations(), "Failed : No. of iterations check");
        assertEquals("SUBMITTED", p.getPollResult().getLastResponse(), "Failed : Last response check");
        assertEquals(TIMED_OUT, p.getPollStatus(), "FAILED: Result status check");
        assertTrue(combinedFuture.isDone(),"FAILED: Process still running");
    }

    @Test
    public void basicSyncTimeOutPollingTest2() throws ExecutionException, InterruptedException {
        MockJobSystem tc = new MockJobSystem();
        ExecutorService customExecutor = Executors.newFixedThreadPool(2, new ThreadFactoryBuilder()
                .setNameFormat("BSTOPT2-%d")
                .setThreadFactory(Executors.defaultThreadFactory())
                .build());
        CompletableFuture<String> c = CompletableFuture.supplyAsync(tc::call, customExecutor);

        CompletableFuture<PollResult<String>> r = CompletableFuture.supplyAsync(() ->
                Poll.withName(null)
                        .pollFor(5000, MILLISECONDS)
                        .every(1, SECONDS)
                        .until(s -> s.equals("COMPLETED"), tc::getStatus), customExecutor);

        CompletableFuture<Object> combinedFuture = CompletableFuture.anyOf(c,r);
        combinedFuture.get();

        PollResult<String> p = r.get();
        tc.cancelJob();
        System.out.println("Result : "+p);

        assertTrue(p.getPollResult().isTimedOut(), "Failed : Timed-Out check");
        assertEquals("0:0:5", p.getPollResult().getPollDuration(), "Failed: Duration check");
        assertEquals(6, p.getPollResult().getPollIterations(), "Failed : No. of iterations check");
        assertEquals("SUBMITTED", p.getPollResult().getLastResponse(), "Failed : Last response check");
        assertEquals(TIMED_OUT, p.getPollStatus(), "FAILED: Result status check");
        assertTrue(combinedFuture.isDone(),"FAILED: Process still running");
    }

    @Test
    public void basicSyncCompletedPollingTest1() throws ExecutionException, InterruptedException {
        MockJobSystem tc = new MockJobSystem();
        ExecutorService customExecutor = Executors.newFixedThreadPool(2, new ThreadFactoryBuilder()
                .setNameFormat("BSCPT1-%d")
                .setThreadFactory(Executors.defaultThreadFactory())
                .build());
        CompletableFuture<String> c = CompletableFuture.supplyAsync(tc::call, customExecutor);

        CompletableFuture<PollResult<String>> r = CompletableFuture.supplyAsync(() ->
                Poll.withName(null)
                .pollFor(40, SECONDS)
                .every(3, SECONDS)
                .until(s -> s.equals("COMPLETED"), tc::getStatus), customExecutor);

        CompletableFuture<Object> combinedFuture = CompletableFuture.anyOf(c,r);
        combinedFuture.get();

        PollResult<String> p = r.get();
        tc.cancelJob();
        System.out.println(p);

        assertFalse(p.getPollResult().isTimedOut(), "Failed : Timed-Out check");
        assertEquals("0:0:30", p.getPollResult().getPollDuration(), "Failed: Duration check");
        assertEquals(11, p.getPollResult().getPollIterations(), "Failed : No. of iterations check");
        assertEquals("COMPLETED", p.getPollResult().getLastResponse(),  "Failed : Last response check");
        assertEquals(SUCCESS, p.getPollStatus(), "FAILED: Result status check");
        assertTrue(combinedFuture.isDone(),"FAILED: Process still running");
    }

    @Test
    public void testPollingCompletedPollingTest2 () throws ExecutionException, InterruptedException {
        MockJobSystem tc = new MockJobSystem();
        ExecutorService customExecutor = Executors.newFixedThreadPool(2, new ThreadFactoryBuilder()
                .setNameFormat("TPCPT2-%d")
                .setThreadFactory(Executors.defaultThreadFactory())
                .build());
        CompletableFuture<String> c = CompletableFuture.supplyAsync(tc::call, customExecutor);

        CompletableFuture<PollResult<String>> r = CompletableFuture.supplyAsync(() ->
                Poll.withName(null)
                .pollFor(40, SECONDS)
                .every(2, SECONDS)
                .until(s -> s.equals("COMPLETED"), tc::getStatus), customExecutor);

        CompletableFuture<Object> combinedFuture = CompletableFuture.anyOf(c,r);
        combinedFuture.get();

        PollResult<String> p = r.get();
        tc.cancelJob();
        System.out.println("Result : "+p);

        assertFalse(p.getPollResult().isTimedOut(), "Failed : Timed-Out check");
        assertEquals("0:0:28", p.getPollResult().getPollDuration(), "Failed: Duration check");
        assertEquals(15, p.getPollResult().getPollIterations(),  "Failed : No. of iterations check");
        assertEquals("COMPLETED", p.getPollResult().getLastResponse(), "Failed : Last response check");
        assertEquals(SUCCESS, p.getPollStatus(), "FAILED: Result status check");
        assertTrue(combinedFuture.isDone(),"FAILED: Process still running");
    }

    @Test
    public void testPollingDefaultsPollingTest1() throws ExecutionException, InterruptedException {
        MockJobSystem tc = new MockJobSystem();
        ExecutorService customExecutor = Executors.newFixedThreadPool(2, new ThreadFactoryBuilder()
                .setNameFormat("TPDPT1-%d")
                .setThreadFactory(Executors.defaultThreadFactory())
                .build());
        CompletableFuture<String> c = CompletableFuture.supplyAsync(tc::call, customExecutor);

        CompletableFuture<PollResult<String>> r = CompletableFuture.supplyAsync(() ->
                Poll.withName(null)
                        .pollWithDefaults()
                        .defaultInterval()
                        .until(s -> s.equals("COMPLETED"), tc::getStatus), customExecutor);

        CompletableFuture<Object> combinedFuture = CompletableFuture.anyOf(c,r);
        combinedFuture.get();

        PollResult<String> p = r.get();
        tc.cancelJob();
        System.out.println("Result : "+p);

        assertFalse(p.getPollResult().isTimedOut(), "Failed : Timed-Out check");
        assertEquals("0:0:30", p.getPollResult().getPollDuration(), "Failed: Duration check");
        assertEquals(3, p.getPollResult().getPollIterations(),  "Failed : No. of iterations check");
        assertEquals("COMPLETED", p.getPollResult().getLastResponse(), "Failed : Last response check");
        assertEquals(SUCCESS, p.getPollStatus(), "FAILED: Result status check");
        assertTrue(combinedFuture.isDone(),"FAILED: Process still running");
    }

    @Test
    public void testPollingDefaultsPollingTest2() throws ExecutionException, InterruptedException {
        MockJobSystem tc = new MockJobSystem();
        ExecutorService customExecutor = Executors.newFixedThreadPool(2, new ThreadFactoryBuilder()
                .setNameFormat("TPDPT2-%d")
                .setThreadFactory(Executors.defaultThreadFactory())
                .build());
        CompletableFuture<String> c = CompletableFuture.supplyAsync(tc::call, customExecutor);

        CompletableFuture<PollResult<String>> r = CompletableFuture.supplyAsync(() ->
                Poll.withName(null)
                        .pollWithDefaults()
                        .every(10, SECONDS)
                        .until(s -> s.equals("COMPLETED"), tc::getStatus), customExecutor);

        CompletableFuture<Object> combinedFuture = CompletableFuture.anyOf(c,r);
        combinedFuture.get();

        PollResult<String> p = r.get();
        tc.cancelJob();
        System.out.println("Result : "+p);

        assertFalse(p.getPollResult().isTimedOut(), "Failed : Timed-Out check");
        assertEquals("0:0:30", p.getPollResult().getPollDuration(), "Failed: Duration check");
        assertEquals(4, p.getPollResult().getPollIterations(),  "Failed : No. of iterations check");
        assertEquals("COMPLETED", p.getPollResult().getLastResponse(), "Failed : Last response check");
        assertEquals(SUCCESS, p.getPollStatus(), "FAILED: Result status check");
        assertTrue(combinedFuture.isDone(),"FAILED: Process still running");
    }

    @Test
    public void testPollingDefaultsPollingTest3() throws ExecutionException, InterruptedException {
        MockJobSystem tc = new MockJobSystem();
        ExecutorService customExecutor = Executors.newFixedThreadPool(2, new ThreadFactoryBuilder()
                .setNameFormat("TPDPT3-%d")
                .setThreadFactory(Executors.defaultThreadFactory())
                .build());
        CompletableFuture<String> c = CompletableFuture.supplyAsync(tc::call, customExecutor);

        CompletableFuture<PollResult<String>> r = CompletableFuture.supplyAsync(() ->
                Poll.withName(null)
                        .pollFor(10, SECONDS)
                        .defaultInterval()
                        .until(s -> s.equals("COMPLETED"), tc::getStatus), customExecutor);

        CompletableFuture<Object> combinedFuture = CompletableFuture.anyOf(c,r);
        combinedFuture.get();

        PollResult<String> p = r.get();
        tc.cancelJob();
        System.out.println("Result : "+p);

        assertTrue(p.getPollResult().isTimedOut(), "Failed : Timed-Out check");
        assertEquals("0:0:15", p.getPollResult().getPollDuration(), "Failed: Duration check");
        assertEquals(2, p.getPollResult().getPollIterations(),  "Failed : No. of iterations check");
        assertEquals("IN PROGRESS", p.getPollResult().getLastResponse(), "Failed : Last response check");
        assertEquals(TIMED_OUT, p.getPollStatus(), "FAILED: Result status check");
        assertTrue(combinedFuture.isDone(),"FAILED: Process still running");
    }
}
