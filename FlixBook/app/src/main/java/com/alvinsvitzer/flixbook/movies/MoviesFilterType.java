package com.alvinsvitzer.flixbook.movies;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alvin on 2/15/17.
 */

public enum MoviesFilterType {


    POPULAR_MOVIES(1),

    HIGHLY_RATED_MOVIES(2),

    FAVORITE_MOVIES(3);

    private int value;

    private static Map map = new HashMap<>();

    MoviesFilterType(int value){
        this.value = value;
    }

    static {

        for (MoviesFilterType moviesFilterType: MoviesFilterType.values()){
            map.put(moviesFilterType.value, moviesFilterType);
        }

    }


    public static MoviesFilterType valueOf(int movieFilterType){
        return (MoviesFilterType) map.get(movieFilterType);
    }

    public int getValue(){
        return value;
    }




}
