package com.sekhon.jason.photogallery;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.sekhon.jason.photogallery.myapplication.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by jason on 2018-09-20.
 */

@RunWith(AndroidJUnit4.class)
public class UITests {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void TestFilter() {
        onView(withId(R.id.btnFilter)).perform(click());
        onView(withId(R.id.search_toDate)).perform(typeText("2018/09/30"), closeSoftKeyboard());
        onView(withId(R.id.search_fromDate)).perform(typeText("2018/09/20"), closeSoftKeyboard());
        onView(withId(R.id.search_search)).perform(click());
        for (int i = 0; i <= 5; i++) {
            onView(withId(R.id.btnRight)).perform(click());
        }
    }

    @Test
    public void TestFilter2() {
        onView(withId(R.id.btnFilter)).perform(click());
        onView(withId(R.id.search_toDate)).perform(typeText("1111/0911/3120"), closeSoftKeyboard());
        onView(withId(R.id.search_fromDate)).perform(typeText("12333/211"), closeSoftKeyboard());
        onView(withId(R.id.search_search)).perform(click());
        for (int i = 0; i <= 5; i++) {
            onView(withId(R.id.btnRight)).perform(click());
        }
    }

    @Test
    public void TestFilter3() {
        onView(withId(R.id.btnFilter)).perform(click());
        onView(withId(R.id.search_fromDate)).perform(typeText("20170101"), closeSoftKeyboard());
        onView(withId(R.id.search_toDate)).perform(typeText("20190101"), closeSoftKeyboard());
        onView(withId(R.id.search_search)).perform(click());
        for (int i = 0; i <= 5; i++) {
            onView(withId(R.id.btnRight)).perform(click());
        }
    }

    @Test
    public void TestFilter4() {
        onView(withId(R.id.btnFilter)).perform(click());
        onView(withId(R.id.search_fromDate)).perform(typeText("20190101"), closeSoftKeyboard());
        onView(withId(R.id.search_toDate)).perform(typeText("20180101"), closeSoftKeyboard());
        onView(withId(R.id.search_search)).perform(click());
        for (int i = 0; i <= 5; i++) {
            onView(withId(R.id.btnRight)).perform(click());
        }
    }
}