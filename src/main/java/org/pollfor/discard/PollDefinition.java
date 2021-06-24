package org.pollfor.discard;

import org.pollfor.entities.PollConfig;

import java.util.function.Predicate;
import java.util.function.Supplier;

class PollDefinition {

    private final PollConfig pollConfig;

    private final Long timeOutMillis;

    private Long initialDelayMillis;

    private final Long timeIntervalMillis;

    private Predicate<?> entryCriterion;

    private Supplier<?> entryAction;

    private Predicate<?>[] exitCriteria;

    private Supplier<?>[] exitActions;

    private PollDefinition(DefinitionBuilder builder){
        this.pollConfig = builder.pollConfig;
        this.timeOutMillis = builder.timeOutMillis;
        this.timeIntervalMillis = builder.timeIntervalMillis;
        this.entryCriterion = builder.entryCriterion;
        this.entryAction = builder.entryAction;
        this.exitCriteria = builder.exitCriteria;
        this.exitActions = builder.exitActions;
    }

    public static DefinitionBuilder create(){
        return new DefinitionBuilder();
    }

    public static class DefinitionBuilder{

        private PollConfig pollConfig;
        private Long timeOutMillis;
        private Long timeIntervalMillis;
        private Predicate<?> entryCriterion;
        private Supplier<?> entryAction;
        private Predicate<?>[] exitCriteria;
        private Supplier<?>[] exitActions;

        public DefinitionBuilder setPollConfig(PollConfig pollConfig){
            this.pollConfig = pollConfig;
            return this;
        }

        public DefinitionBuilder setTimeOutMillis(Long timeOutMillis){
            this.timeOutMillis = timeOutMillis;
            return this;
        }

        public DefinitionBuilder setTimeIntervalMillis(Long timeIntervalMillis){
            this.timeIntervalMillis = timeIntervalMillis;
            return this;
        }

        public DefinitionBuilder setEntryCriterion(Predicate<?> entryCriterion){
            this.entryCriterion = entryCriterion;
            return this;
        }

        public DefinitionBuilder setEntryAction(Supplier<?> entryAction){
            this.entryAction = entryAction;
            return this;
        }

        public DefinitionBuilder setExitCriteria(Predicate<?>[] exitCriteria){
            this.exitCriteria = exitCriteria;
            return this;
        }

        public DefinitionBuilder setExitActions(Supplier<?>[] exitActions){
            this.exitActions = exitActions;
            return this;
        }

        public PollDefinition build(){
            return new PollDefinition(this);
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
