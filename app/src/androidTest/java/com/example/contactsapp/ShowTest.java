package com.example.contactsapp;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ShowTest {
    @Rule
    public IntentsTestRule<Show> intentsTestRule =
            new IntentsTestRule<>(Show.class);
    @Test
    public void testFunc() {

        /**Seeded with contact from SaveTest**/

        onView(withId(R.id.recycler)).check(matches(isDisplayed()));

        /**Click on the row with name "Anagh" **/
        onView(allOf(withId(R.id.recycler), isDisplayed()))
                .perform(actionOnItem(withChild(withText("Anagh")), click()));


        intended(hasComponent(GotoContact.class.getName()));


        onView(withText("100")).check(matches(isDisplayed()));
        onView(withText("200")).check(matches(isDisplayed()));

        /**Regex Matching using withSubstring as withText doesn't support Regex**/
        onView(withSubstring("anagh@mail.com")).check(matches(isDisplayed()));
        onView(withSubstring("goswami@mail.com")).check(matches(isDisplayed()));

    }
}