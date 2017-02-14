package com.alvinsvitzer.flixbook;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


    // Runs all Espresso tests.
    @RunWith(Suite.class)
    @Suite.SuiteClasses({RecylerViewInstrumentationTest.class,
            YouTubeActivityInstrumentationTest.class})

    public class EspressoTestSuite {}
