package com.example.mvvmretrofitexecutorsmovieapp.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvmretrofitexecutorsmovieapp.AppExecutors;
import com.example.mvvmretrofitexecutorsmovieapp.models.MovieModel;
import com.example.mvvmretrofitexecutorsmovieapp.repository.MovieRepository;
import com.example.mvvmretrofitexecutorsmovieapp.response.MovieSearchResponse;
import com.example.mvvmretrofitexecutorsmovieapp.utils.Credentials;
import com.example.mvvmretrofitexecutorsmovieapp.utils.MovieAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieAPIClient {

    /**
     * MovieAPi CLient WIll get Data from RETROFIT, API (interface)
     */

    //search movies LiveData
    private MutableLiveData<List<MovieModel>> mMoviesListWithLiveData;

    //populr movie Livedata
    private MutableLiveData<List<MovieModel>> mPopularMoviesLiveData;


    private static MovieAPIClient instance;

    //making GLOBAL RUNNABLE
    private RetrievesMovieRunnable retrievesMovieRunnable;

    private RetrievesMovieRunnablePop retrievesMovieRunnablePopular;

    public static MovieAPIClient getInstance() {
        if (instance == null) {

            instance = new MovieAPIClient();
        }
        return instance;
    }

    public MovieAPIClient() {

        mMoviesListWithLiveData = new MutableLiveData<>(); /** VERY IMP STEP */

        mPopularMoviesLiveData = new MutableLiveData<>();
    }


    public LiveData<List<MovieModel>> getMovies() {
        return mMoviesListWithLiveData;
    }

    public LiveData<List<MovieModel>> getPopMovie() {
        return mPopularMoviesLiveData;
    }

    /**
     * MAIN TWO METHOD WHICH ARE GETTING RESPONSE FROM RETROFIT API
     * Get DATA FROM API WITH THE HELP OF EXECUTORS (ON BACKGROUND THREAD)
     */

    /**
     * get DATA FROM
     * AppExecutors CLass
     */


    public void searchMovieAPI(String query, int pageNumber) {

        if (retrievesMovieRunnable != null) {

            retrievesMovieRunnable = null;

        }

        retrievesMovieRunnable = new RetrievesMovieRunnable(query, pageNumber);


        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrievesMovieRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //Cancel retrofit Call

                /**
                 * Agar 4 Second me Retrofit SE DATA NAHI AAYA To Retrofit Call
                 *
                 * Cancel Ho Jayegi
                 *
                 * */
                myHandler.cancel(true);

                /**
                 * APP WILL NEVER CRASH
                 * */
            }
        }, 5000, TimeUnit.MILLISECONDS);


        /** FETCH THE DATA FROM API */


    }


    public void getPopularMoviesAPI(int pageNumber) {

        if (retrievesMovieRunnablePopular != null) {

            retrievesMovieRunnablePopular = null;

        }

        retrievesMovieRunnablePopular = new RetrievesMovieRunnablePop(pageNumber);


        final Future myHandler2 = AppExecutors.getInstance().networkIO().submit(retrievesMovieRunnablePopular);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //Cancel retrofit Call

                /**
                 * Agar 4 Second me Retrofit SE DATA NAHI AAYA To Retrofit Call
                 *
                 * Cancel Ho Jayegi
                 *
                 * */
                myHandler2.cancel(true);

                /**
                 * APP WILL NEVER CRASH
                 * */
            }
        }, 1000, TimeUnit.MILLISECONDS);


        /** FETCH THE DATA FROM API */


    }

    private class RetrievesMovieRunnable implements Runnable {
        /**
         * *
         * 2 Search Query
         */

        public String query;
        public int pageNumber;
        boolean cancelRequest;


        public RetrievesMovieRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            /**
             * GET THE RESPONSE OBJECT
             * */

            try {
                Response response = getMovies(query, pageNumber).execute();

                if (cancelRequest) {
                    return;

                }

                if (response.code() == 200) {
                    List<MovieModel> moviesList = new ArrayList<>(
                            ((MovieSearchResponse) response.body()).getMovies()
                    );

                    if (pageNumber == 1) {
                        /** Sending data to LiveDATA */
                        //PostValue is used for background Thread
                        //SetValue is NOT used for background Thread
                        mMoviesListWithLiveData.postValue(moviesList);

                        /**Sending data to LiveDATA*/

                    } else {

                        List<MovieModel> currentMovies = mMoviesListWithLiveData.getValue();

                        currentMovies.addAll(moviesList);
                        mMoviesListWithLiveData.postValue(currentMovies);

                    }

                } else {

                    String error = response.errorBody().toString();
                    Log.d("TAG", "run: Error " + error);
                    mMoviesListWithLiveData.postValue(null);
                }


            } catch (IOException e) {
                e.printStackTrace();
                mMoviesListWithLiveData.postValue(null);
            }


        }

        /**
         * SEARCH METHOD QUERY
         */

        private Call<MovieSearchResponse> getMovies(String query, int pageNumber) {
            return Servicey.getMovieAPI().searchMovieApi(Credentials.API_KEY,
                    query, pageNumber);
        }

        private void cancelRequest() {

            Log.d("TAG", "cancelRequest: Cancelling Request ");
            cancelRequest = true;

        }
    }

    private class RetrievesMovieRunnablePop implements Runnable {
        /**
         * *
         * 2 Search Query
         */

        public int pageNumber;
        boolean cancelRequest;


        public RetrievesMovieRunnablePop(int pageNumber) {
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            /**
             * GET THE RESPONSE OBJECT
             * */

            try {
                Response response2 = getPop(pageNumber).execute();

                if (cancelRequest) {
                    return;

                }

                if (response2.code() == 200) {
                    List<MovieModel> moviesList = new ArrayList<>(
                            ((MovieSearchResponse) response2.body()).getMovies()
                    );

                    if (pageNumber == 1) {
                        /** Sending data to LiveDATA */
                        //PostValue is used for background Thread
                        //SetValue is NOT used for background Thread
                        mPopularMoviesLiveData.postValue(moviesList);

                        /**Sending data to LiveDATA*/

                    } else {

                        List<MovieModel> currentMovies = mMoviesListWithLiveData.getValue();

                        currentMovies.addAll(moviesList);
                        mPopularMoviesLiveData.postValue(currentMovies);

                    }

                } else {

                    String error = response2.errorBody().toString();
                    Log.d("TAG", "run: Error " + error);
                    mPopularMoviesLiveData.postValue(null);
                }


            } catch (IOException e) {
                e.printStackTrace();
                mPopularMoviesLiveData.postValue(null);
            }


        }

        /**
         * SEARCH METHOD QUERY
         */

        private Call<MovieSearchResponse> getPop(int pageNumber) {
            return Servicey.getMovieAPI().getPopularMovies(Credentials.API_KEY, pageNumber);
        }

        private void cancelRequest() {

            Log.d("TAG", "cancelRequest: Cancelling Request ");
            cancelRequest = true;

        }
    }

    /*//Search with movie name, return list of movies with matching search query
    private void getRetrofitResponse() {
        MovieAPI movieAPI = Servicey.getMovieAPI();

        Call<MovieSearchResponse> responseCall = movieAPI.searchMovieApi(
                Credentials.API_KEY, "Jungle", "1"
        );

        responseCall.enqueue(new Callback<MovieSearchResponse>() {
            @Override
            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {

                if (response.code() == 200) {// response is successful

                    Log.d("TAG", "onResponse: " + response.body().toString());

                    List<MovieModel> movies = new ArrayList<>(response.body().getMovies());

                    *//************************************************************************
     * ListOfMovies will give Data to MutableLiveData
     * MutableLiveData will be Accesedd through LIVEDATA because of
     * DATA SECURITY
     * *//*
                    mMoviesListWithLiveData.postValue(movies);

                    *//*************************************************************************//*

                    for (MovieModel movie : movies) {

                        Log.d("TAG", "onResponse:The List of Title=  " + movie.getTitle());
                    }

                } else {
                    Log.d("TAG", "onResponse: ERROR = " + response.errorBody().toString());
                }

            }

            @Override
            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {

            }
        });
    }

    //Making search with id
    private void getMovieById() {

        MovieAPI movieAPI = Servicey.getMovieAPI();
        Call<MovieModel> call = movieAPI.getMovieById(550, Credentials.API_KEY);

        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.code() == 200) {

                    MovieModel movie = response.body();
                    Log.d("TAG", "onResponse: Title = " + movie.getTitle() +
                            " Release Date = " + movie.getRelease_date());

                } else {

                    //show error code
                    try {
                        Log.d("TAG", "onResponse: FAILED" + response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });


    }*/


}
