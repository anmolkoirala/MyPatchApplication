package com.example.mypatchapplication;

import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.mypatchapplication.Common.LoginSignup.ProLogin;
import com.example.mypatchapplication.Common.SplashScreen;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest

    public  class LoginActivityTest {

    @Rule
    public ActivityTestRule<ProLogin> rule = new ActivityTestRule<>(ProLogin.class);

    //to login into the patch application
        @Test
        public void LoginActivityUI() {

            Intent intent = new Intent();
            intent.putExtra("usertype","customer");
            rule.launchActivity(intent);
            onView(withId(R.id.professional_phonenumber)).perform(typeText("9876543210"));
            closeSoftKeyboard();
            onView(withId(R.id.professional_password)).perform(typeText("anmolk"));
            closeSoftKeyboard();
            onView(withId(R.id.login_button)).perform(click());
        }

    //Login UI Register link test
    @Test
    public void checkingRegisterlinkbutton() throws Exception{
        onView(withId(R.id.login_createUserAcc)).perform(click());
        onView((withId(R.id.signup_titleone))).check(matches(isDisplayed()));

    }


    // checking validation
    @Test
    public void testCheckFieldsPhoneEmpty(){

        onView(withId(R.id.professional_password)).perform(typeText("anmolk"));
        closeSoftKeyboard();
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(com.google.android.material.R.id.textinput_error))
                .check(matches(withText("Enter valid phone number"))).check(matches(isDisplayed()));
    }


    //checking validation of password
    @Test
    public void testCheckFieldsPasswordEmpty(){

        onView(withId(R.id.professional_phonenumber)).perform(typeText("9876543210"));
        closeSoftKeyboard();
        onView(withId(R.id.login_button)).perform(click());

        onView(withId(com.google.android.material.R.id.textinput_error))
                .check(matches(withText("Field can not be empty"))).check(matches(isDisplayed()));
    }
}
