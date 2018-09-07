package com.rustfisher.basic4;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.rustfisher.basic4.activity.Hts.SAct1;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITest1 {

    private static final String TAG = "rustAppUnitTest";

    @Rule
    public ActivityTestRule<SAct1> sActActivityTestRule = new ActivityTestRule<>(SAct1.class);

    @Before
    public void prepareData() {
        System.out.println("做一些准备工作,比如预装数据");
        Log.d(TAG, "prepareData");
    }

    @Test
    public void checkTv() {
        onView(withId(R.id.tv1)).check(matches(withText("[act1] ")));
//        onView(withId(R.id.tv1)).check(matches(withText("[act]"))); // 检查字符串
    }

    @Test
    public void clickButton() {
        onView(withId(R.id.btn1)).perform(click());
    }

}
