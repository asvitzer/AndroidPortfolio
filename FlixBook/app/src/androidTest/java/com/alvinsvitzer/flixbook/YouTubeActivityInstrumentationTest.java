package com.alvinsvitzer.flixbook;

import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

/**
 * Instrumentation test, which will execute on an Android device. Tests the functionality
 * of the implicit intent to launch YouTube for movie trailers.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class YouTubeActivityInstrumentationTest {


    @Rule
    public IntentsTestRule<MovieActivity> mIntentsRule = new IntentsTestRule<>(MovieActivity.class);


    @Test
    public void validateCameraScenario() {

        //Setup Activity flow to be at Movie Detail section before starting intent testing.
        String apiKey = mIntentsRule.getActivity().getMovieDBApiKey();

        FragmentManager fm = mIntentsRule.getActivity().getSupportFragmentManager();

        Fragment fragment = MovieGridFragment.newInstance(apiKey);
        fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commitAllowingStateLoss();

        onView(allOf(withId(R.id.movie_recycler_view), hasFocus()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        /**
         * Build a fake intent to launch the youtube app. This is needed so I know that the YouTube app launched
         * based on the URI.
         */
        Intent videoIntent = new Intent();
        videoIntent.setData(Uri.parse("https://www.youtube.com/watch?v=uImk2RgCq_U"));
        videoIntent.setAction(Intent.ACTION_VIEW);

/*        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, videoIntent);

        // Stub out the YouTube app. When an intent is sent to the app, this tells Espresso to respond
        // with the ActivityResult we just created
        intending(toPackage("com.google.android.youtube")).respondWith(result);*/

        // Now that we have the stub in place, click on the button in our app that launches YouTube
        onView(withId(R.id.playTrailerFab)).perform(click());

        // Validate that an intent resolving to the YouTube activity has been sent out by our app
        intended(toPackage("com.google.android.youtube"));


    }



}
