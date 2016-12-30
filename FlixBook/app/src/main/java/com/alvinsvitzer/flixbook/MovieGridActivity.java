package com.alvinsvitzer.flixbook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.alvinsvitzer.flixbook.utilities.MovieDBUtils;
import com.alvinsvitzer.flixbook.utilities.VolleyNetworkSingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.net.URL;

public class MovieGridActivity extends AppCompatActivity {

    private TextView mTextView;
    private String movieDBApiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_grid);

        mTextView = (TextView) findViewById(R.id.text_view_query_result);
        movieDBApiKey = getString(R.string.the_movie_db_auth_key);

        grabDefaultMovie();


     }

    private void grabDefaultMovie() {

        URL requestDefaultMovieURL = MovieDBUtils.buildDefaultUrl(movieDBApiKey);


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, requestDefaultMovieURL.toString(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mTextView.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        VolleyNetworkSingleton.getInstance(this).addToRequestQueue(jsObjRequest);

    }
}
