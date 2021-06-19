package org.pollfor.api;

import java.util.function.Predicate;
import java.util.function.Supplier;

class InternalPollDef {

    private final PollConfig pollConfig;

    private final Long timeOutMillis;

    private Long initialDelayMillis;

    private final Long timeIntervalMillis;

    private Predicate<?> entryCriterion;

    private Supplier<?> entryAction;

    private Predicate<?>[] exitCriteria;

    private Supplier<?>[] exitActions;

    private InternalPollDef(InternalPollDefBuilder builder){
        this.pollConfig = builder.pollConfig;
        this.timeOutMillis = builder.timeOutMillis;
        this.timeIntervalMillis = builder.timeIntervalMillis;
        this.entryCriterion = builder.entryCriterion;
        this.entryAction = builder.entryAction;
        this.exitCriteria = builder.exitCriteria;
        this.exitActions = builder.exitActions;
    }

    public static InternalPollDefBuilder create(){
        return new InternalPollDefBuilder();
    }

    public static class InternalPollDefBuilder{

        private PollConfig pollConfig;
        private Long timeOutMillis;
        private Long timeIntervalMillis;
        private Predicate<?> entryCriterion;
        private Supplier<?> entryAction;
        private Predicate<?>[] exitCriteria;
        private Supplier<?>[] exitActions;

        public InternalPollDefBuilder setPollConfig(PollConfig pollConfig){
            this.pollConfig = pollConfig;
            return this;
        }

        public InternalPollDefBuilder setTimeOutMillis(Long timeOutMillis){
            this.timeOutMillis = timeOutMillis;
            return this;
        }

        public InternalPollDefBuilder setTimeIntervalMillis(Long timeIntervalMillis){
            this.timeIntervalMillis = timeIntervalMillis;
            return this;
        }

        public InternalPollDefBuilder setEntryCriterion(Predicate<?> entryCriterion){
            this.entryCriterion = entryCriterion;
            return this;
        }

        public InternalPollDefBuilder setEntryAction(Supplier<?> entryAction){
            this.entryAction = entryAction;
            return this;
        }

        public InternalPollDefBuilder setExitCriteria(Predicate<?>[] exitCriteria){
            this.exitCriteria = exitCriteria;
            return this;
        }

        public InternalPollDefBuilder setExitActions(Supplier<?>[] exitActions){
            this.exitActions = exitActions;
            return this;
        }

        public InternalPollDef build(){
            return new InternalPollDef(this);
        }
    }

    public PollConfig getPollConfig() {
        return pollConfig;
    }

    public Long getTimeOutMillis() {
        return timeOutMillis;
    }

    public Long getTimeIntervalMillis() {
        return timeIntervalMillis;
    }

    public Predicate<?> getEntryCriterion() {
        return entryCriterion;
    }

    public Supplier<?> getEntryAction() {
        return entryAction;
    }

    public Predicate<?>[] getExitCriteria() {
        return exitCriteria;
    }

    public Supplier<?>[] getExitActions() {
        return exitActions;
    }

    public void setEntryCriterion(Predicate<?> entryCriterion) {
        this.entryCriterion = entryCriterion;
    }

    public void setEntryAction(Supplier<?> entryAction) {
        this.entryAction = entryAction;
    }

    public void setExitCriteria(Predicate<?>... exitCriteria) {
        this.exitCriteria = exitCriteria;
    }

    public void setExitActions(Supplier<?>... exitActions) {
        this.exitActions = exitActions;
    }

    public Long getInitialDelayMillis() {
        return initialDelayMillis;
    }

    public void setInitialDelayMillis(Long initialDelayMillis) {
        this.initialDelayMillis = initialDelayMillis;
    }
}
