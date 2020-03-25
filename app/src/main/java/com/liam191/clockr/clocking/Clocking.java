package com.liam191.clockr.clocking;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Date;

public final class Clocking {
    private final String label;
    private final String description;
    private final int durationInMinutes;
    private final Date startTime;

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

    public Date startTime(){
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
        private Date startTime = null;

        public Builder(String label, int durationInMinutes){
            if(label == null) {
                throw new IllegalArgumentException("label cannot be null");
            }
            this.label = label.trim();

            if(durationInMinutes < 0) {
                throw new IllegalArgumentException("durationInMinutes cannot be zero or negative");
            }
            this.durationInMinutes = durationInMinutes;
        }


        public Builder description(String description) throws IllegalArgumentException {
            if(description == null) {
                throw new IllegalArgumentException("description cannot be null");
            }
            this.description = description.trim();
            return this;
        }

        public Builder startTime(Date startTime) throws IllegalArgumentException {
            if(startTime == null) {
                throw new IllegalArgumentException("startTime cannot be null");
            }
            this.startTime = startTime;
            return this;
        }

        public Clocking build(){
            if (startTime == null) {
                this.startTime = new Date();
            }
            return new Clocking(this);
        }
    }
}
