package com.alvinsvitzer.flixbook.moviedetail;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.alvinsvitzer.flixbook.movies.MovieActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MovieDetailElementsExistTest {

    @Rule
    public ActivityTestRule<MovieActivity> mActivityTestRule = new ActivityTestRule<>(MovieActivity.class);

/*    @Test
    public void checkElementsExist() {

        FragmentManager fm = mActivityTestRule.getActivity().getSupportFragmentManager();

        Fragment fragment = new MovieGridFragment();
        fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commitAllowingStateLoss();

        ViewInteraction recyclerView = onView(allOf(withId(R.id.movie_recycler_view), hasFocus()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        ViewInteraction imageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.my_toolbar),
                                        childAtPosition(
                                                withId(R.id.collapsing_toolbar),
                                                1)),
                                0),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction actionBar$Tab = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.sliding_tabs),
                                0),
                        0),
                        isDisplayed()));
        actionBar$Tab.check(matches(isDisplayed()));

        ViewInteraction actionBar$Tab2 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.sliding_tabs),
                                0),
                        1),
                        isDisplayed()));
        actionBar$Tab2.check(matches(isDisplayed()));

        ViewInteraction actionBar$Tab3 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.sliding_tabs),
                                0),
                        2),
                        isDisplayed()));
        actionBar$Tab3.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.movie_release_date_label), withText("Release Date:"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.viewpager),
                                        0),
                                2),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.movie_vote_average_label), withText("Vote Average:"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.viewpager),
                                        0),
                                4),
                        isDisplayed()));
        textView3.check(matches(isDisplayed()));

        ViewInteraction imageView = onView(
                allOf(withId(R.id.movie_poster_image),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.collapsing_toolbar),
                                        0),
                                2),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.banner_text_view), withText("Rings"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.collapsing_toolbar),
                                        0),
                                3),
                        isDisplayed()));
        textView4.check(matches(isDisplayed()));

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.playTrailerFab),
                        childAtPosition(
                                allOf(withId(R.id.MovieDetailCoordLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

    }*/

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
