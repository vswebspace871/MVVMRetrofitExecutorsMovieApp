package com.example.mvvmretrofitexecutorsmovieapp.utils;

import com.example.mvvmretrofitexecutorsmovieapp.models.MovieModel;
import com.example.mvvmretrofitexecutorsmovieapp.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieAPI {

    //Popular Movies

    @GET("3/movie/popular")
    Call<MovieSearchResponse> getPopularMovies(
            @Query("api_key") String key,
            @Query("page") int page
    );




    //Search for movies, search Query
    @GET("3/search/movie")
    Call<MovieSearchResponse> searchMovieApi(
            @Query("api_key") String key,
            @Query("query") String movieName,
            @Query("page") int page

    );


    //Search by movie id
    @GET("3/movie/{movie_id}?")
    Call<MovieModel> getMovieById(
            @Path("movie_id") int movie_id,
            @Query("api_key") String key
    );


}
