package com.alvinsvitzer.flixbook.extensions;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.alvinsvitzer.flixbook.R;

/**
 * Abstract Activity Class used to load a fragment onto a layout with a single fragment
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    @LayoutRes
    protected int getLayoutResId(){
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragmentByTag = fm.findFragmentByTag(getTag());

        if (fragmentByTag == null){

            fragmentByTag = createFragment();
            fm.beginTransaction()
                    .replace(R.id.fragment_container, fragmentByTag, getTag())
                    .commit();
        }

    }

    protected abstract Fragment createFragment();
    protected abstract String getTag();

}
