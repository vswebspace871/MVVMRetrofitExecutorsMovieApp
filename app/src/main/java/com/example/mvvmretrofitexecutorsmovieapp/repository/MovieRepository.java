package com.example.mvvmretrofitexecutorsmovieapp.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvmretrofitexecutorsmovieapp.models.MovieModel;
import com.example.mvvmretrofitexecutorsmovieapp.request.MovieAPIClient;
import com.example.mvvmretrofitexecutorsmovieapp.request.Servicey;
import com.example.mvvmretrofitexecutorsmovieapp.response.MovieSearchResponse;
import com.example.mvvmretrofitexecutorsmovieapp.utils.Credentials;
import com.example.mvvmretrofitexecutorsmovieapp.utils.MovieAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    //this class is acting as repository

    /**
     * Repository will Get Data from MovieAPI CLient
     */

    private static MovieRepository instance;

    private MovieAPIClient movieAPIClient;

    private String mQuery;
    private int mPageNumber;


    //getter
    //singleton
    public static MovieRepository getInstance() {
        if (instance == null) {

            instance = new MovieRepository();
        }
        return instance;
    }

    //constructor
    private MovieRepository() {
        movieAPIClient = MovieAPIClient.getInstance();
    }


    public MovieAPIClient getMovieAPIClient() {
        return movieAPIClient;
    }

    public LiveData<List<MovieModel>> getMovies() {
        return movieAPIClient.getMovies();
    }

    public LiveData<List<MovieModel>> getPopMovies() {
        return movieAPIClient.getPopMovie();
    }



    //Calling the method from MovieAPI Client
    public void searchMovieApi(String query, int pageNumber) {

        mQuery = query;

        mPageNumber = pageNumber;

        movieAPIClient.searchMovieAPI(query, pageNumber);
    }

    public void getPopMovie(int pageNumber) {

        mPageNumber = pageNumber;

        movieAPIClient.getPopularMoviesAPI(pageNumber);
    }




    public void searchNextPage(){
        searchMovieApi(mQuery, mPageNumber+1);
    }


}


