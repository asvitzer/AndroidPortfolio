package com.alvinsvitzer.flixbook.extensions;

import android.support.design.widget.AppBarLayout;

/**
 * Created by Alvin on 2/17/17.
 */

public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener  {

    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    public State mCurrentState = State.IDLE;

    public abstract void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset);

    public abstract void onStateChanged(State state);
}
