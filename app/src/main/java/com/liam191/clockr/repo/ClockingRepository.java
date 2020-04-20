package com.liam191.clockr.repo;

import com.liam191.clockr.clocking.Clocking;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public final class ClockingRepository {

    private final MutableLiveData<List<Clocking>> clockings = new MutableLiveData<>();
    {
        clockings.postValue(new ArrayList<>());
    }

    ClockingRepository(){

    }

    public LiveData<List<Clocking>> getClockingsForDate(Date date){
        return clockings;
    }

    public void addClocking(Clocking clocking){
        ArrayList<Clocking> newClockings = new ArrayList<>(clockings.getValue());
        newClockings.add(clocking);
        clockings.postValue(newClockings);
    }

}
