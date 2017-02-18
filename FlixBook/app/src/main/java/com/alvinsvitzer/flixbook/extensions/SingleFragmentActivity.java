package com.alvinsvitzer.flixbook.extensions;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.alvinsvitzer.flixbook.R;

/**
 * Created by Alvin on 1/1/16.
 * Abstract Activity Class used to load a fragment onto a layout with a single fragment
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    private Fragment mFragment;

    @LayoutRes
    protected int getLayoutResId(){
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        mFragment = fm.findFragmentById(R.id.fragment_container);

        if (mFragment == null){

            mFragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, mFragment)
                    .commit();
        }

    }

    public Fragment getCurrentFragment(){

        return mFragment;
    }

    protected abstract Fragment createFragment();

}
