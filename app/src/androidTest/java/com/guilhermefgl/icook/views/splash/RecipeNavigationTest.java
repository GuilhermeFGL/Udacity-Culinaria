package com.guilhermefgl.icook.views.splash;

import android.content.pm.ActivityInfo;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.AccessibilityChecks;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.views.main.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeNavigationTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @BeforeClass
    public static void enableAccessibilityChecks() {
        AccessibilityChecks.enable().setRunChecksFromRootView(true);
    }

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);

        // navigation is not visible in landscape
        mActivityTestRule.getActivity()
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Test
    public void recipeNavigationTest() {

        // go to RecipeActivity with click at first recipe in gridview
        onView(ViewMatchers.withId(R.id.main_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // go to ingredients tab
        ViewInteraction tabView = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.details_tab),
                                0),
                        1),
                        isDisplayed()));
        tabView.perform(click());

        // verify if ingredients list is created
        ViewInteraction linearLayout1 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.ingredient_list),
                                withParent(withId(R.id.details_pager))),
                        0),
                        isDisplayed()));
        linearLayout1.check(matches(isDisplayed()));

        // go to steps tab
        ViewInteraction tabView2 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.details_tab),
                                0),
                        0),
                        isDisplayed()));
        tabView2.perform(click());

        // verify if steps list is created
        ViewInteraction linearLayout2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.include),
                                isDescendantOfA(withId(R.id.frameLayout))),
                        0),
                        isDisplayed()));
        linearLayout2.check(matches(isDisplayed()));

        // go to StepDetailsActivity with click at first step at listview
        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.include),
                        childAtPosition(
                                withId(R.id.frameLayout),
                                0)));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        // verify if title is same of first step of current recipe
        ViewInteraction textView = onView(
                allOf(withText("Recipe Introduction"),
                        childAtPosition(
                                allOf(withId(R.id.step_toolbar),
                                        isDescendantOfA(withId(R.id.app_bar))),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Recipe Introduction")));

        // verify if 'Prev Step' button is disabled
        ViewInteraction button = onView(
                allOf(withId(R.id.step_navigation_prev_action),
                        isDisplayed()));
        button.check(matches(not(isEnabled())));

        // verify if 'Next Step' button is enabled
        ViewInteraction button2 = onView(
                allOf(withId(R.id.step_navigation_next_action),
                        isDisplayed()));
        button2.check(matches(isEnabled()));

        // click 'Next Step'
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.step_navigation_next_action),
                        isDisplayed()));
        appCompatButton.perform(click());

        // verify if title is same of second step of current recipe
        ViewInteraction textView2 = onView(
                allOf(withText("Starting prep"),
                        childAtPosition(
                                allOf(withId(R.id.step_toolbar),
                                        isDescendantOfA(withId(R.id.app_bar))),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Starting prep")));

        // verify if 'Prev Step' button is enabled
        ViewInteraction button3 = onView(
                allOf(withId(R.id.step_navigation_prev_action),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));

        // verify if 'Next Step' button is enabled
        ViewInteraction button4 = onView(
                allOf(withId(R.id.step_navigation_next_action),
                        isDisplayed()));
        button4.check(matches(isDisplayed()));

        // click 'Next Step'
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.step_navigation_next_action),
                        isDisplayed()));
        appCompatButton2.perform(click());

        // click 'Next Step'
        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.step_navigation_next_action),
                        isDisplayed()));
        appCompatButton3.perform(click());

        // click 'Next Step'
        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.step_navigation_next_action),
                        isDisplayed()));
        appCompatButton4.perform(click());

        // click 'Next Step'
        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.step_navigation_next_action),
                        isDisplayed()));
        appCompatButton5.perform(click());

        // click 'Next Step' and reach last step of current recipe
        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.step_navigation_next_action),
                        isDisplayed()));
        appCompatButton6.perform(click());

        // verify if 'Prev Step' button is enabled
        ViewInteraction button5 = onView(
                allOf(withId(R.id.step_navigation_prev_action),
                        isDisplayed()));
        button5.check(matches(isDisplayed()));

        // verify if 'Next Step' button is disabled
        ViewInteraction button6 = onView(
                allOf(withId(R.id.step_navigation_next_action),
                        isDisplayed()));
        button6.check(matches(isDisplayed()));

        // done
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

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
