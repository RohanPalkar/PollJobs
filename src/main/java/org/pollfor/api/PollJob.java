package org.pollfor.api;

import org.pollfor.common.Utils;
import org.pollfor.entities.PollConfig;
import org.pollfor.entities.TimeValue;
import org.pollfor.sdk.ActionExecutor;
import org.pollfor.service.AsyncExecutor;
import org.pollfor.service.ServiceType;
import org.pollfor.service.SyncExecutor;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class PollJob {

    private ActionExecutor pollJobExecutor;

    private final String name;
    private final Integer iterations;
    private final TimeValue timeOut;
    private final TimeValue timeInterval;
    private final Long timeOutMillis;
    private final Long timeIntervalMillis;
    private Predicate<?>[] exitCriteria;
    private Supplier<?>[] exitActions;

    private PollJob(PollJobBuilder builder){
        PollConfig pollConfig = Utils.getPollConfig();
        boolean useDefaults = builder.iterations == null && builder.timeOut == null;

        this.name = builder.name;
        this.iterations = builder.iterations;
        this.timeOut = useDefaults ? pollConfig.getTimeOut() : builder.timeOut;
        this.timeInterval = builder.timeInterval == null ?
                pollConfig.getTimeInterval() : builder.timeInterval;

        this.timeIntervalMillis = this.timeInterval.getUnit().toMillis(this.timeInterval.getValue());

        this.timeOutMillis = this.iterations != null ? this.timeIntervalMillis * this.iterations :
                            this.timeOut.getUnit().toMillis(this.timeOut.getValue());
    }

    public static PollJobBuilder createJob(){
        return new PollJobBuilder();
    }

    public static class PollJobBuilder{
        private String name;
        private Integer iterations;
        private TimeValue timeOut;
        private TimeValue timeInterval;

        public PollJobBuilder setName(String name){
            this.name = name;
            return this;
        }

        public PollJobBuilder setIterations(Integer iterations){
            this.iterations = iterations;
            return this;
        }

        public PollJobBuilder setTimeOut(TimeValue timeOut){
            this.timeOut = timeOut;
            return this;
        }

        public PollJobBuilder setTimeInterval(TimeValue timeInterval){
            this.timeInterval = timeInterval;
            return this;
        }

        public PollJob build(){
            return new PollJob(this);
        }
    }

    public <T> List<PollResult.PollInfo<T>> executePollJob(ServiceType serviceType){
        switch (serviceType){
            case SYNC: return execSync();
            case ASYNC: return execASync();
        }
        return null;
    }

    private <T> List<PollResult.PollInfo<T>> execSync(){
        this.pollJobExecutor = new SyncExecutor(this.exitCriteria[0],
                                                this.exitActions[0],
                                                this.timeOut,
                                                this.timeOutMillis,
                                                this.timeInterval,
                                                this.timeIntervalMillis);
        return this.pollJobExecutor.executePoll();
    }

    private <T> List<PollResult.PollInfo<T>>execASync(){
        this.pollJobExecutor = new AsyncExecutor(this.exitCriteria,
                                                    this.exitActions,
                                                    this.timeOut,
                                                    this.timeOutMillis,
                                                    this.timeInterval,
                                                    this.timeIntervalMillis);
        return this.pollJobExecutor.executePoll();
    }


    public String getName() {
        return name;
    }

    public Integer getIterations() {
        return iterations;
    }

    public TimeValue getTimeOut() {
        return timeOut;
    }

    public TimeValue getTimeInterval() {
        return timeInterval;
    }

    public Long getTimeOutMillis() {
        return timeOutMillis;
    }

    public Long getTimeIntervalMillis() {
        return timeIntervalMillis;
    }

    public Predicate<?>[] getExitCriteria() {
        return exitCriteria;
    }

    public void setExitCriterion(Predicate<?> exitCriteria) {
        this.exitCriteria = new Predicate[]{exitCriteria};
    }

    public void setExitCriteria(Predicate<?>[] exitCriteria) {
        this.exitCriteria = exitCriteria;
    }

    public Supplier<?>[] getExitActions() {
        return exitActions;
    }

    public void setExitActions(Supplier<?>[] exitActions) {
        this.exitActions = exitActions;
    }

    public void setExitAction(Supplier<?> exitAction) {
        this.exitActions = new Supplier[]{exitAction};
    }
}
