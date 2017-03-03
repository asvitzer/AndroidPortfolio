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

    private State mCurrentState = State.IDLE;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (verticalOffset == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(State.IDLE);
            }
            mCurrentState = State.IDLE;
        }
    }

    public abstract void onStateChanged(State state);
}
