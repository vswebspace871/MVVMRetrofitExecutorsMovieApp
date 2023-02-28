package com.example.mvvmretrofitexecutorsmovieapp.response;


import com.example.mvvmretrofitexecutorsmovieapp.models.MovieModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//this class is reqesting a single movie request
public class MovieResponse {

    // finding the movie object
    @SerializedName("results")
    @Expose
    private MovieModel movie;

    public MovieModel getMovie() {
        return movie;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "movie=" + movie +
                '}';
    }
}
