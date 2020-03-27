package com.example.walkrally;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterTest {
    @Rule
    public IntentsTestRule<Register> intentsTestRule =
            new IntentsTestRule<>(Register.class);

    @Test
    public void ToastNoEmailWithNoPassword(){
        onView(withId(R.id.signupButton)).perform(click());
        onView(withText("Fields Are Empty!"))
                .inRoot(withDecorView(not(intentsTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    }
