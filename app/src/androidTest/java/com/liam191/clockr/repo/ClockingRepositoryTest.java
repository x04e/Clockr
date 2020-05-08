package com.liam191.clockr.repo;

import android.content.Context;

import com.liam191.clockr.clocking.Clocking;
import com.liam191.clockr.repo.db.ClockingDao;
import com.liam191.clockr.repo.db.ClockrDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.filters.SmallTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SmallTest
@RunWith(AndroidJUnit4ClassRunner.class)
public class ClockingRepositoryTest {

    private ClockrDatabase testDb;
    private ClockingDao clockingTestDao;

    @Before
    public void createTestDb(){
        Context appContext = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(appContext, ClockrDatabase.class).build();
        clockingTestDao = testDb.clockingDao();
    }

    @After
    public void teardownTestDb(){
        testDb.close();
    }

    @Test
    public void testAddClocking(){
        ClockingRepository repository = new ClockingRepository();

        Clocking clocking = new Clocking.Builder("TestClocking1", 34).build();
        repository.add(clocking);

        assertTrue(clockingTestDao.getAll().contains(clocking));
    }

    @Test
    public void testAddClocking2(){
        //ClockingRepository repository = new ClockingRepository();

        //Clocking clocking = new Clocking.Builder("TestClocking1", 34).build();
        //repository.add(clocking);

        //assertTrue(repository.getAll().contains(clocking));
    }
}