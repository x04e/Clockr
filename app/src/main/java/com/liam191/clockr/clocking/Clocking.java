package com.liam191.clockr.clocking;

import java.time.Clock;
import java.time.LocalDateTime;

public final class Clocking {
    private final String label;
    private final String description;
    private final int durationInMinutes;
    private final LocalDateTime startTime;

    private Clocking(Builder clockingBuilder){
        this.label = clockingBuilder.label;
        this.description = clockingBuilder.description;
        this.durationInMinutes = clockingBuilder.durationInMinutes;
        this.startTime = clockingBuilder.startTime;
    }

    public String label(){
        return this.label;
    }

    public String description(){
        return this.description;
    }

    public int durationInMinutes(){
        return this.durationInMinutes;
    }

    public LocalDateTime startTime(){
        return this.startTime;
    }

    @Override
    public boolean equals(Object o){
        if(o == null || o.getClass() != getClass()){
            return false;
        }
        Clocking clocking = (Clocking) o;

        return (label.equals(clocking.label) &&
            description.equals(clocking.description) &&
            durationInMinutes == clocking.durationInMinutes &&
            description.equals(clocking.description) &&
            startTime.equals(clocking.startTime));
    }


    public static final class Builder {
        private String label = "";
        private String description = "";
        private int durationInMinutes = 0;
        // startTime does *not* get a default time value as the time should be set
        // when a Clocking is created, not when the Builder is created.
        private LocalDateTime startTime = null;
        private Clock systemClock = Clock.systemDefaultZone();

        public Builder(String label, int durationInMinutes){
            this.label = (label == null) ? "" : label.trim();
            setDurationInMinutes(durationInMinutes);
        }

        Builder(String label, int durationInMinutes, Clock testSystemClock){
            this(label, durationInMinutes);
            setSystemClock(testSystemClock);
        }



        private void setDurationInMinutes(int durationInMinutes) throws IllegalArgumentException{
            if(durationInMinutes < 0) {
                throw new IllegalArgumentException("durationInMinutes cannot be zero or negative");
            }
            this.durationInMinutes = durationInMinutes;
        }

        private void setStartTime(LocalDateTime startTime){
            this.startTime = startTime;
        }

        private void setSystemClock(Clock systemClock){
            this.systemClock = systemClock;
        }



        public Builder description(String description){
            this.description =  (description == null) ? "" : description.trim();
            return this;
        }

        public Builder startTime(LocalDateTime startTime){
            setStartTime(startTime);
            return this;
        }

        public Clocking build(){
            if (startTime == null) {
                setStartTime(LocalDateTime.now(systemClock));
            }
            return new Clocking(this);
        }
    }
}
