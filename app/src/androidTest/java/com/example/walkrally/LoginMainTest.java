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
public class LoginMainTest {

        @Rule
        public IntentsTestRule<LoginMain> intentsTestRule =
                new IntentsTestRule<>(LoginMain.class);

        @Test
        public void OpenAppToast() {
                onView(withText("Please Login"))
                        .inRoot(withDecorView(not(intentsTestRule.getActivity().getWindow().getDecorView())))
                        .check(matches(isDisplayed()));
        }
        @Test
        public void NotEnterEmailAndPassword(){
                onView(withId(R.id.emailText)).perform(typeText(""));
                onView(withId(R.id.passText)).perform(typeText(""));
                onView(withId(R.id.loginButton)).perform(click());
                onView(withId(R.id.emailText)).check(matches(hasErrorText("Fields Are Empty!")));
        }
        @Test
        public void EmailWithNoPassword(){
                onView(withId(R.id.emailText)).perform(typeText("Test@t.com"));
                onView(withId(R.id.passText)).perform(typeText(""));
                onView(withId(R.id.loginButton)).perform(click());
                onView(withId(R.id.passText)).check(matches(hasErrorText("Please enter your password")));
        }
        @Test
        public void PasswordWithNoEmail(){
                onView(withId(R.id.emailText)).perform(typeText(""));
                onView(withId(R.id.passText)).perform(typeText("123456"));
                onView(withId(R.id.loginButton)).perform(click());
                onView(withId(R.id.emailText)).check(matches(hasErrorText("Please enter email id")));

        }
        @Test
        public void ToastLoginFailed(){
                onView(withId(R.id.emailText)).perform(typeText("testlogin@t.com"));
                onView(withId(R.id.passText)).perform(typeText("123456789"));
                onView(withId(R.id.loginButton)).perform(click());
                onView(withText("Login Error, Please Login Again"))
                        .inRoot(withDecorView(not(intentsTestRule.getActivity().getWindow().getDecorView())))
                        .check(matches(isDisplayed()));
        }

        @Test
        public void GoRegisterActivity(){
                onView(withId(R.id.gosignUp)).perform(click());
        }
}

