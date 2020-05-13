package com.liam191.clockr.repo;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.filters.SmallTest;
import androidx.test.runner.AndroidJUnit4;

import com.liam191.clockr.clocking.Clocking;
import com.liam191.clockr.repo.db.ClockingDao;
import com.liam191.clockr.repo.db.ClockrDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SmallTest
@RunWith(AndroidJUnit4.class)
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
    public void testAdd(){
        ClockingRepository repository = new ClockingRepository(clockingTestDao);

        Clocking clocking = new Clocking.Builder("TestClocking1").build();
        repository.insert(clocking);

        assertTrue(clockingTestDao.getAll().size() == 1);
        assertTrue(clockingTestDao.getAll().contains(ClockingRepository.Mapper.map(clocking)));
    }

    @Test
    public void testRemove(){
        ClockingRepository repository = new ClockingRepository(clockingTestDao);

        Clocking clocking = new Clocking.Builder("TestClocking1").build();
        repository.insert(clocking);
        assertTrue(clockingTestDao.getAll().size() == 1);

        repository.delete(clocking);
        assertTrue(clockingTestDao.getAll().size() == 0);
    }

    @Test
    public void testRemove_WithMultipleRows(){
        ClockingRepository repository = new ClockingRepository(clockingTestDao);
        Clocking clocking = new Clocking.Builder("TestClocking1").build();

        repository.insert(clocking);
        repository.insert(clocking);
        repository.insert(clocking);
        repository.insert(clocking);
        repository.insert(clocking);

        assertEquals(clockingTestDao.getAll().size(), 5);
        repository.delete(clocking);
        assertEquals(clockingTestDao.getAll().size(), 4);
    }

    @Test
    public void testReplace(){
        ClockingRepository repository = new ClockingRepository(clockingTestDao);

        Clocking clocking1 = new Clocking.Builder("TestClockingBeforeReplace").build();
        repository.insert(clocking1);
        assertTrue(clockingTestDao.getAll().contains(ClockingRepository.Mapper.map(clocking1)));
        assertEquals(clockingTestDao.getAll().size(), 1);

        Clocking clocking2 = new Clocking.Builder("TestClockingAfterReplace").build();
        repository.replace(clocking1, clocking2);
        assertTrue(clockingTestDao.getAll().contains(ClockingRepository.Mapper.map(clocking2)));
        assertEquals(clockingTestDao.getAll().size(), 1);
    }
}
