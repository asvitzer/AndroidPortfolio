package com.alvinsvitzer.flixbook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alvinsvitzer.flixbook.model.Movie;
import com.alvinsvitzer.flixbook.model.MovieDBJSONUtils;
import com.alvinsvitzer.flixbook.utilities.MovieDBUtils;
import com.alvinsvitzer.flixbook.utilities.VolleyNetworkSingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MovieGridActivity extends AppCompatActivity {

    private static final String TAG = MovieGridActivity.class.getSimpleName();
    private TextView mTextView;
    private String movieDBApiKey;
    private Movie[] mMovieObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_grid);

        mTextView = (TextView) findViewById(R.id.text_view_query_result);

        /** Set movieDBApiKey below to your API key**/
        movieDBApiKey = getString(R.string.the_movie_db_auth_key);

        grabDefaultMovie();


     }

    private void grabDefaultMovie() {

        URL requestDefaultMovieURL = MovieDBUtils.buildDefaultUrl(movieDBApiKey);

        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, requestDefaultMovieURL.toString(), null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            mMovieObjects = MovieDBJSONUtils.getMovieDataFromJSONObject(MovieGridActivity.this, response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        Log.e(TAG, "onErrorResponse: ", error);

                        Toast.makeText(MovieGridActivity.this, "Unable to retrieve movie data.", Toast.LENGTH_SHORT)
                                .show();

                    }
                });

        VolleyNetworkSingleton.getInstance(this).addToRequestQueue(jsArrayRequest);

    }
}
