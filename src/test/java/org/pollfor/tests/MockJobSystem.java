package org.pollfor.tests;

import java.util.concurrent.Callable;

import static org.pollfor.common.Utils.println;

public class MockJobSystem implements Callable<String> {

    private int counter = 1;
    private String status = "OPEN";
    private Boolean isJobCancelled = false;

    public void reset(){
        counter = 1;
    }

    public synchronized int getCounter(){
        return counter;
    }

    public synchronized String getStatus(){
        println("GetStatus: "+status, "TEST","counter:" + String.valueOf(counter));
        return status;
    }

    public void cancelJob(){
        this.isJobCancelled = true;
    }


    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public String call()  {
        int iterations = 20;
        long start = System.currentTimeMillis();
        for(; counter < iterations && !Thread.interrupted() && !isJobCancelled; ++counter){
            println("Status : "+status, "TEST","counter:" + counter, isJobCancelled.toString());
            try {
                if(counter >= 15)
                    status = "COMPLETED";
                else if(counter >= 5)
                    status = "IN PROGRESS";
                else if(counter >= 2)
                    status = "SUBMITTED";
                long sleepInterval = 2000;

                if(isJobCancelled){
                    println("Cancelling job");
                    break;
                }
                Thread.sleep(sleepInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            int seconds = (int) ((end - start) / 1000);
            println("Status : "+status, "TEST","counter:" + counter, seconds + " sec elapsed", isJobCancelled.toString());
        }
        return status;
    }
}