package org.pollfor.tests;

import org.junit.jupiter.api.Test;
import org.pollfor.api.Poll;
import org.pollfor.api.PollResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.*;
import static org.junit.jupiter.api.Assertions.*;

public class SyncPollingTests {

    @Test
    public void basicSyncPollingTest1() throws InterruptedException, ExecutionException {
        MockJobSystem tc = new MockJobSystem();
        CompletableFuture<String> c = CompletableFuture.supplyAsync(tc::call);

        PollResult<String> p =
                new Poll()
                .pollFor(5, SECONDS)
                .every(1, SECONDS)
                .until(s -> s.equals("COMPLETED"), () -> tc.getStatus());

        System.out.println(p);

        assertTrue(p.getPollResult().isTimedOut(), "Failed : Timed-Out check");
        assertEquals(p.getPollResult().getPollIterations(), 5, "Failed : No. of iterations check");
        assertEquals(p.getPollResult().getLastResponse(), "SUBMITTED", "Failed : Last response check");
    }

    @Test
    public void basicSyncPollingTest2() throws InterruptedException, ExecutionException {
        MockJobSystem tc = new MockJobSystem();
        CompletableFuture<String> c = CompletableFuture.supplyAsync(tc::call);

        PollResult<String> p =
                new Poll()
                        .pollFor(5000, MILLISECONDS)
                        .every(1, SECONDS)
                        .until(s -> s.equals("COMPLETED"), () -> tc.getStatus());

        System.out.println(p);

        assertTrue(p.getPollResult().isTimedOut(), "Failed : Timed-Out check");
        assertEquals(p.getPollResult().getPollIterations(), 5, "Failed : No. of iterations check");
        assertEquals(p.getPollResult().getLastResponse(), "SUBMITTED", "Failed : Last response check");
    }
}
