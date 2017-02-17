package com.alvinsvitzer.flixbook;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.alvinsvitzer.flixbook.movies.MovieActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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

/*        String apiKey = mMovieActivityRule.getActivity().getMovieDBApiKey();

        FragmentManager fm = mMovieActivityRule.getActivity().getSupportFragmentManager();

        Fragment fragment = MovieGridFragment.newInstance(apiKey);
        fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commitAllowingStateLoss();


        onView(allOf(withId(R.id.movie_recycler_view), hasFocus()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));


        onView(withId(R.id.banner_text_view)).check(matches(isDisplayed()));*/

    }


}
