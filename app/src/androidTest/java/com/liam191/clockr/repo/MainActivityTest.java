package com.liam191.clockr.repo;

import com.liam191.clockr.MainActivity;
import com.liam191.clockr.R;
import com.liam191.clockr.clocking.Clocking;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.threeten.bp.Clock;
import org.threeten.bp.ZonedDateTime;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

@LargeTest
public class MainActivityTest {

    // TODO: Find a way to clear database between tests
    // TODO: Isolate dependency on time in Activities, maybe with set system time
    //       in tests or a getClock() method in AppContainer?

    public ClockrApplicationTestRunner.FakeClockrApplication.FakeAppContainerImpl appContainer;
    public ClockingRepository repository;

    @Rule
    public ActivityTestRule activityTestRule;

    @Before
    public void setup(){
        activityTestRule = new ActivityTestRule(MainActivity.class);
        appContainer = ((ClockrApplicationTestRunner.FakeClockrApplication) ApplicationProvider.getApplicationContext()).getAppContainer();
        repository = appContainer.getClockingRepository();

        //TODO: NPE when calling getAppContainer() because once the activity is created it's too late to use another appContainer
        //      - See here for possible solutions: https://stackoverflow.com/a/23246170
        //TODO: Refactor this mess into separate classes
    }

    @Test
    public void testMainActivity(){
        ZonedDateTime fakeNow = ZonedDateTime.parse("2020-03-04T10:00:00Z[Europe/London]");
        Clock fakeClock = Clock.fixed(fakeNow.toInstant(), fakeNow.getZone());
        ClockrApplicationTestRunner.FakeClockrApplication.FakeAppContainerImpl appContainer = ((ClockrApplicationTestRunner.FakeClockrApplication) ApplicationProvider.getApplicationContext()).getAppContainer();
        appContainer.setAppClock(fakeClock);

        ZonedDateTime testDate = ZonedDateTime.parse("2020-03-04T08:11Z[Europe/London]");

        Clocking clocking1 = new Clocking.Builder("hello world")
                .startTime(testDate.plusMinutes(10))
                .description("This is a description for the first clocking.").build();
        Clocking clocking2 = new Clocking.Builder("goodbye world")
                .startTime(testDate.plusMinutes(50))
                .description("This a description for the second clocking.").build();
        Clocking clocking3 = new Clocking.Builder("new world")
                .startTime(testDate.plusMinutes(120)).build();
        Clocking clocking4 = new Clocking.Builder("old world")
                .startTime(testDate.plusDays(5))
                .description("This clocking has a different start date and shouldn't show up").build();

        repository.insert(clocking1);
        repository.insert(clocking2);
        repository.insert(clocking3);
        repository.insert(clocking4);

        activityTestRule.launchActivity(null);

        onView(withId(R.id.clocking_recyclerview))
                .check(matches(hasChildCount(3)))
                // clocking1
                .check(matches(hasDescendant(withText(clocking1.label()))))
                .check(matches(hasDescendant(withText(clocking1.description()))))
                .check(matches(hasDescendant(withText(clocking1.startTime().toString()))))
                // clocking2
                .check(matches(hasDescendant(withText(clocking2.label()))))
                .check(matches(hasDescendant(withText(clocking2.description()))))
                .check(matches(hasDescendant(withText(clocking2.startTime().toString()))))
                // clocking3
                .check(matches(hasDescendant(withText(clocking3.label()))))
                .check(matches(hasDescendant(withText(clocking3.description()))))
                .check(matches(hasDescendant(withText(clocking3.startTime().toString()))))
                // clocking4 - should not be displayed because it's from another date
                .check(matches(not(hasDescendant(withText(clocking4.label())))))
                .check(matches(not(hasDescendant(withText(clocking4.description())))))
                .check(matches(not(hasDescendant(withText(clocking4.startTime().toString())))));

    }
}
