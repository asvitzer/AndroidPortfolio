package com.alvinsvitzer.flixbook.movies;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.alvinsvitzer.flixbook.R;

import org.hamcrest.core.AllOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumentation test, which will execute on an Android device. Tests the functionality
 * of the RecyclerView.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RecylerViewInstrumentationTest {

    @Rule
    public ActivityTestRule<MovieActivity> mMovieActivityRule = new ActivityTestRule<>(
            MovieActivity.class);

    @Test
    public void verifyRecyclerViewHasContent() throws Exception {

        FragmentManager fm = mMovieActivityRule.getActivity().getSupportFragmentManager();

        Fragment fragment = new MovieGridFragment();
        fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        ;

        // Wait for the fragment to be committed
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.waitForIdleSync();

        ViewInteraction recyclerView = onView(AllOf.allOf(withId(R.id.movie_recycler_view), hasFocus()));

        // Check movie grid has an item with a movie date in its recycler view
        recyclerView.check(matches(hasDescendant(withId(R.id.card_view_movie_date))));

    }

    @Test
    public void verifyMovieDetailsOpensFromGridClick() throws Exception {

        FragmentManager fm = mMovieActivityRule.getActivity().getSupportFragmentManager();

        Fragment fragment = new MovieGridFragment();
        fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        ;

        // Wait for the fragment to be committed
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.waitForIdleSync();

        ViewInteraction recyclerView = onView(AllOf.allOf(withId(R.id.movie_recycler_view), hasFocus()));
        recyclerView.perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        ViewInteraction imageButton = onView(
                AllOf.allOf(withContentDescription("Navigate up"),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

    }




}
