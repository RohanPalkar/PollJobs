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
    public void basicSyncPollingTest1() {
        MockJobSystem tc = new MockJobSystem();
        CompletableFuture<String> c = CompletableFuture.supplyAsync(tc::call);

        PollResult<String> p =
                new Poll()
                .pollFor(5, SECONDS)
                .every(1, SECONDS)
                .until(s -> s.equals("COMPLETED"), () -> tc.getStatus());

        System.out.println(p);

        tc.cancelJob();

        System.out.println(c.isCancelled());

        assertTrue(p.getPollResult().isTimedOut(), "Failed : Timed-Out check");
        assertEquals(5, p.getPollResult().getPollIterations(), "Failed : No. of iterations check");
        assertEquals("SUBMITTED", p.getPollResult().getLastResponse(), "Failed : Last response check");
    }

    @Test
    public void basicSyncPollingTest2() {
        MockJobSystem tc = new MockJobSystem();
        CompletableFuture<String> c = CompletableFuture.supplyAsync(tc::call);

        PollResult<String> p =
                new Poll()
                        .pollFor(5000, MILLISECONDS)
                        .every(1, SECONDS)
                        .until(s -> s.equals("COMPLETED"), () -> tc.getStatus());

        System.out.println(p);

        tc.cancelJob();

        assertTrue(p.getPollResult().isTimedOut(), "Failed : Timed-Out check");
        assertEquals(6, p.getPollResult().getPollIterations(), "Failed : No. of iterations check");
        assertEquals("SUBMITTED", p.getPollResult().getLastResponse(), "Failed : Last response check");
    }
}
