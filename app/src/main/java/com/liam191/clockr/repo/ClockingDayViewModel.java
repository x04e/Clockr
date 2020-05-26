package com.liam191.clockr.repo;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.liam191.clockr.clocking.Clocking;
import com.liam191.clockr.repo.db.ClockingDayDao;
import com.liam191.clockr.repo.db.ClockingEntity;

import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

public final class ClockingDayViewModel extends ViewModel {

    // TODO: Use date to return new instances
    // TODO: Add DatabaseView annotation to DAO for ClockingDayView

    private final ClockingRepository clockingRepository;
    private final ZonedDateTime day;
    private final MutableLiveData<List<Clocking>> clockingList = new MutableLiveData<>();

    private final Observer<List<ClockingEntity>> liveDataObserver = clockingEntityList -> {
        List<Clocking> newClockingList = new ArrayList<>();
        for(ClockingEntity entity : clockingEntityList){
            newClockingList.add(ClockingRepository.Mapper.map(entity));
        }
        clockingList.postValue(newClockingList);
    };

    private ClockingDayViewModel(ClockingRepository clockingRepository, ClockingDayDao clockingDayDao, ZonedDateTime day){
        this.clockingRepository = clockingRepository;
        this.day = day;

        // TODO: Replace with Transformations
        //      -See: https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54
        clockingDayDao.getAllForDate(day)
                .observeForever(liveDataObserver);
    }

    public LiveData<List<Clocking>> get(){
        return clockingList;
    }

    public void add(Clocking clocking){
        clockingRepository.insert(clocking);
    }

    public void remove(Clocking clocking){
        clockingRepository.delete(clocking);
    }

    public void replace(Clocking target, Clocking replacement){
        clockingRepository.replace(target, replacement);
    }

    public static class Builder implements ViewModelProvider.Factory {
        private final ClockingRepository clockingRepository;
        private final ClockingDayDao clockingDayDao;
        private ZonedDateTime ofDate;

        public Builder(ClockingRepository clockingRepository, ClockingDayDao clockingDayDao){
            if(clockingRepository == null){
                throw new IllegalArgumentException("clockingRepository cannot be null");
            }
            if(clockingDayDao == null){
                throw new IllegalArgumentException("clockingDayDao cannot be null");
            }

            this.clockingRepository = clockingRepository;
            this.clockingDayDao = clockingDayDao;
        }

        public Builder ofDate(ZonedDateTime ofDate){
            // TODO: Add null check

            this.ofDate = ofDate;
            return this;
        }

        ClockingDayViewModel build(){
            return new ClockingDayViewModel(clockingRepository, clockingDayDao, ofDate);
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) build();
        }
    }
}