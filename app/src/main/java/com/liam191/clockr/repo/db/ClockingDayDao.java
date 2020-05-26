package com.liam191.clockr.repo.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.TypeConverters;

import org.threeten.bp.ZonedDateTime;

import java.util.List;

@Dao
public interface ClockingDayDao {
    @Query("SELECT * FROM clockingentity" +
            " WHERE date(substr(:date, 0, instr(:date, '['))) = date(substr(start_time, 0, instr(start_time, '[')))" +
            " OR date(substr(:date, 0, instr(:date, '['))) = date(substr(end_time, 0, instr(end_time, '[')))")
    @TypeConverters(ZonedDateTimeConverter.class)
    LiveData<List<ClockingEntity>> getAllForDate(ZonedDateTime date);
}
