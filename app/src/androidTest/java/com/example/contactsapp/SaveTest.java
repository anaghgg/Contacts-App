package com.example.contactsapp;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.viewpager.widget.ViewPager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.app.PendingIntent.getActivity;
import static android.content.Context.MODE_PRIVATE;
import static android.service.autofill.Validators.not;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SaveTest {
    @Rule
    public IntentsTestRule<Save> intentsTestRule =
            new IntentsTestRule<>(Save.class);

    @Test
    public void saveData() {
        Context context= ApplicationProvider.getApplicationContext();
        SQLiteDatabase db=context.openOrCreateDatabase("ContactsDB", MODE_PRIVATE, null);
        db.execSQL("DELETE FROM CONTACTS");
        db.execSQL("DELETE FROM IMAGEDB");
        db.execSQL("DELETE FROM COORDS");

        /***When name is not typed, error toast is shown and no intents are fired***/
        onView(withId(R.id.name)).perform(scrollTo(),clearText());
        onView(withId(R.id.number)).perform(scrollTo(),clearText(),typeText("123456"));
        onView(withId(R.id.button3)).perform(scrollTo(),click());



        /***When number is not typed, error toast is shown and no intents are fired***/
        onView(withId(R.id.number)).perform(scrollTo(),clearText());
        onView(withId(R.id.name)).perform(scrollTo(),clearText(),typeText("Test"));
        onView(withId(R.id.button3)).perform(scrollTo(),click());
        /***Nothing happens ---> In Save.class***/



        /***Save a contact with both numbers and both emails***/
        onView(withId(R.id.name)).perform(scrollTo(),clearText(),typeText("Anagh"));
        onView(withId(R.id.number)).perform(scrollTo(),clearText(),typeText("100"));
        onView(withId(R.id.altumber)).perform(scrollTo(),clearText(),typeText("200"));
        onView(withId(R.id.mail)).perform(scrollTo(),clearText(),typeText("anagh@mail.com"));
        onView(withId(R.id.altmail)).perform(scrollTo(),clearText(),typeText("goswami@mail.com"));
        onView(withId(R.id.button3)).perform(scrollTo(),click());
        intended(hasComponent(com.example.contactsapp.MainActivity.class.getName()));
        /***MainActivity->Saved***Now Click "New" to Save Another Contact***/
        onView(withId(R.id.button)).perform(click());



        /***Primary number clashing when only one number specified in new contact***/
        onView(withId(R.id.name)).perform(scrollTo(),clearText(),typeText("Test"));
        onView(withId(R.id.number)).perform(scrollTo(),clearText(),typeText("100"));
        onView(withId(R.id.button3)).perform(scrollTo(),click());
        intended(hasComponent(com.example.contactsapp.Merge.class.getName()));
        Espresso.pressBackUnconditionally();



        /***Primary number clashing with new Alternate when only one number specified in new contact***/
        onView(withId(R.id.name)).perform(scrollTo(),clearText(),typeText("Test"));
        onView(withId(R.id.altumber)).perform(scrollTo(),clearText(),typeText("100"));
        onView(withId(R.id.number)).perform(scrollTo(),clearText());
        onView(withId(R.id.button3)).perform(scrollTo(),click());
        intended(hasComponent(com.example.contactsapp.Merge.class.getName()));
        Espresso.pressBackUnconditionally();


        /***Primary number clashing when both numbers are specified in new contact***/
        onView(withId(R.id.name)).perform(scrollTo(),clearText(),typeText("Test"));
        onView(withId(R.id.number)).perform(scrollTo(),clearText(),typeText("100"));
        onView(withId(R.id.altumber)).perform(scrollTo(),clearText(),typeText("1000"));
        onView(withId(R.id.button3)).perform(scrollTo(),click());
        intended(hasComponent(com.example.contactsapp.Merge.class.getName()));
        Espresso.pressBackUnconditionally();



        /***Primary number clashing with new Alternate number when both numbers are specified in new contact***/
        onView(withId(R.id.name)).perform(scrollTo(),clearText(),typeText("Test"));
        onView(withId(R.id.number)).perform(scrollTo(),clearText(),typeText("1000"));
        onView(withId(R.id.altumber)).perform(scrollTo(),clearText(),typeText("100"));
        onView(withId(R.id.button3)).perform(scrollTo(),click());
        intended(hasComponent(com.example.contactsapp.Merge.class.getName()));
        Espresso.pressBackUnconditionally();



        /***Alternate number with new Primary number clashing when only one number specified in new contact***/
        onView(withId(R.id.name)).perform(scrollTo(),clearText(),typeText("Test"));
        onView(withId(R.id.number)).perform(scrollTo(),clearText(),typeText("200"));
        onView(withId(R.id.altumber)).perform(scrollTo(),clearText());
        onView(withId(R.id.button3)).perform(scrollTo(),click());
        intended(hasComponent(com.example.contactsapp.Merge.class.getName()));
        Espresso.pressBackUnconditionally();



        /***Alternate number clashing when only one number specified in new contact***/
        onView(withId(R.id.name)).perform(scrollTo(),clearText(),typeText("Test"));
        onView(withId(R.id.altumber)).perform(scrollTo(),clearText(),typeText("200"));
        onView(withId(R.id.number)).perform(scrollTo(),clearText());
        onView(withId(R.id.button3)).perform(scrollTo(),click());
        intended(hasComponent(com.example.contactsapp.Merge.class.getName()));
        Espresso.pressBackUnconditionally();



        /***Alternate number clashing when both numbers specified in new contact***/
        onView(withId(R.id.name)).perform(scrollTo(),clearText(),typeText("Test"));
        onView(withId(R.id.number)).perform(scrollTo(),clearText(),typeText("2000"));
        onView(withId(R.id.altumber)).perform(scrollTo(),clearText(),typeText("200"));
        onView(withId(R.id.button3)).perform(scrollTo(),click());
        intended(hasComponent(com.example.contactsapp.Merge.class.getName()));
        Espresso.pressBackUnconditionally();



        /***Alternate number clashing with new Primary when both numbers specified in new contact***/
        onView(withId(R.id.name)).perform(scrollTo(),clearText(),typeText("Test"));
        onView(withId(R.id.number)).perform(scrollTo(),clearText(),typeText("200"));
        onView(withId(R.id.altumber)).perform(scrollTo(),clearText(),typeText("2000"));
        onView(withId(R.id.button3)).perform(scrollTo(),click());
        intended(hasComponent(com.example.contactsapp.Merge.class.getName()));
        Espresso.pressBackUnconditionally();



        /***Primary email clashing when only one email specified***/
        onView(withId(R.id.name)).perform(scrollTo(),clearText(),typeText("Test"));
        onView(withId(R.id.number)).perform(scrollTo(),clearText(),typeText("12345"));
        onView(withId(R.id.altumber)).perform(scrollTo(),clearText(),typeText("987654"));
        onView(withId(R.id.mail)).perform(scrollTo(),clearText(),typeText("anagh@mail.com"));
        onView(withId(R.id.altmail)).perform(scrollTo(),clearText());
        onView(withId(R.id.button3)).perform(scrollTo(),click());
        intended(hasComponent(com.example.contactsapp.Merge.class.getName()));
        Espresso.pressBackUnconditionally();



        /***Primary email with new Allternate email clashing when only one email specified***/
        onView(withId(R.id.name)).perform(scrollTo(),clearText(),typeText("Test"));
        onView(withId(R.id.number)).perform(scrollTo(),clearText(),typeText("12345"));
        onView(withId(R.id.altumber)).perform(scrollTo(),clearText(),typeText("987654"));
        onView(withId(R.id.mail)).perform(scrollTo(),clearText());
        onView(withId(R.id.altmail)).perform(scrollTo(),clearText(),typeText("anagh@mail.com"));
        onView(withId(R.id.button3)).perform(scrollTo(),click());
        intended(hasComponent(com.example.contactsapp.Merge.class.getName()));
        Espresso.pressBackUnconditionally();



        /***Primary email clashing when both emails specified***/
        onView(withId(R.id.name)).perform(scrollTo(),clearText(),typeText("Test"));
        onView(withId(R.id.number)).perform(scrollTo(),clearText(),typeText("12345"));
        onView(withId(R.id.altumber)).perform(scrollTo(),clearText(),typeText("987654"));
        onView(withId(R.id.mail)).perform(scrollTo(),clearText(),typeText("anagh@mail.com"));
        onView(withId(R.id.altmail)).perform(scrollTo(),clearText(),typeText("test@mail.com"));
        onView(withId(R.id.button3)).perform(scrollTo(),click());
        intended(hasComponent(com.example.contactsapp.Merge.class.getName()));
        Espresso.pressBackUnconditionally();



        /***Primary email with Alternate email clashing when both emails specified***/
        onView(withId(R.id.name)).perform(scrollTo(),clearText(),typeText("Test"));
        onView(withId(R.id.number)).perform(scrollTo(),clearText(),typeText("12345"));
        onView(withId(R.id.altumber)).perform(scrollTo(),clearText(),typeText("987654"));
        onView(withId(R.id.mail)).perform(scrollTo(),clearText(),typeText("test@mail.com"));
        onView(withId(R.id.altmail)).perform(scrollTo(),clearText(),typeText("anagh@mail.com"));
        onView(withId(R.id.button3)).perform(scrollTo(),click());
        intended(hasComponent(com.example.contactsapp.Merge.class.getName()));
        Espresso.pressBackUnconditionally();



        /***Alternate email with new Primary email clashing when only one email specified***/
        onView(withId(R.id.name)).perform(scrollTo(),clearText(),typeText("Test"));
        onView(withId(R.id.number)).perform(scrollTo(),clearText(),typeText("12345"));
        onView(withId(R.id.altumber)).perform(scrollTo(),clearText(),typeText("987654"));
        onView(withId(R.id.mail)).perform(scrollTo(),clearText(),typeText("goswami@mail.com"));
        onView(withId(R.id.altmail)).perform(scrollTo(),clearText());
        onView(withId(R.id.button3)).perform(scrollTo(),click());
        intended(hasComponent(com.example.contactsapp.Merge.class.getName()));
        Espresso.pressBackUnconditionally();



        /***Alternate email clashing when only one email specified***/
        onView(withId(R.id.name)).perform(scrollTo(),clearText(),typeText("Test"));
        onView(withId(R.id.number)).perform(scrollTo(),clearText(),typeText("12345"));
        onView(withId(R.id.altumber)).perform(scrollTo(),clearText(),typeText("987654"));
        onView(withId(R.id.mail)).perform(scrollTo(),clearText());
        onView(withId(R.id.altmail)).perform(scrollTo(),clearText(),typeText("goswami@mail.com"));
        onView(withId(R.id.button3)).perform(scrollTo(),click());
        intended(hasComponent(com.example.contactsapp.Merge.class.getName()));
        Espresso.pressBackUnconditionally();



        /***Alternate email with new Primary email clashing both emails specified***/
        onView(withId(R.id.name)).perform(scrollTo(),clearText(),typeText("Test"));
        onView(withId(R.id.number)).perform(scrollTo(),clearText(),typeText("12345"));
        onView(withId(R.id.altumber)).perform(scrollTo(),clearText(),typeText("987654"));
        onView(withId(R.id.mail)).perform(scrollTo(),clearText(),typeText("goswami@mail.com"));
        onView(withId(R.id.altmail)).perform(scrollTo(),clearText(),typeText("test@mail.com"));
        onView(withId(R.id.button3)).perform(scrollTo(),click());
        intended(hasComponent(com.example.contactsapp.Merge.class.getName()));
        Espresso.pressBackUnconditionally();



        /***Alternate email clashing when only one email specified***/
        onView(withId(R.id.name)).perform(scrollTo(),clearText(),typeText("Test"));
        onView(withId(R.id.number)).perform(scrollTo(),clearText(),typeText("12345"));
        onView(withId(R.id.altumber)).perform(scrollTo(),clearText(),typeText("987654"));
        onView(withId(R.id.mail)).perform(scrollTo(),clearText(),typeText("test@mail.com"));
        onView(withId(R.id.altmail)).perform(scrollTo(),clearText(),typeText("goswami@mail.com"));
        onView(withId(R.id.button3)).perform(scrollTo(),click());
        intended(hasComponent(com.example.contactsapp.Merge.class.getName()));
        Espresso.pressBackUnconditionally();




    }


}



