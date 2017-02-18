package com.alvinsvitzer.flixbook;

import com.alvinsvitzer.flixbook.moviedetail.YouTubeActivityInstrumentationTest;
import com.alvinsvitzer.flixbook.movies.RecylerViewInstrumentationTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


    // Runs all Espresso tests.
    @RunWith(Suite.class)
    @Suite.SuiteClasses({RecylerViewInstrumentationTest.class,
            YouTubeActivityInstrumentationTest.class})

    public class EspressoTestSuite {}
