package com.alvinsvitzer.flixbook;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.alvinsvitzer.flixbook.movies.MovieActivity;
import com.alvinsvitzer.flixbook.movies.MovieGridFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

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
    public void verifyMovieDetailsOpensFromGridClick() throws Exception {

        String apiKey = mMovieActivityRule.getActivity().getMovieDBApiKey();

        FragmentManager fm = mMovieActivityRule.getActivity().getSupportFragmentManager();

        Fragment fragment = MovieGridFragment.newInstance(apiKey);
        fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commitAllowingStateLoss();


        onView(allOf(withId(R.id.movie_recycler_view), hasFocus()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));


        onView(withId(R.id.banner_text_view)).check(matches(isDisplayed()));

    }


}
