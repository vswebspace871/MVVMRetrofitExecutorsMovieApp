package com.example.mvvmretrofitexecutorsmovieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mvvmretrofitexecutorsmovieapp.adapters.MovieAdapter;
import com.example.mvvmretrofitexecutorsmovieapp.adapters.OnMovieListener;
import com.example.mvvmretrofitexecutorsmovieapp.models.MovieModel;
import com.example.mvvmretrofitexecutorsmovieapp.request.Servicey;
import com.example.mvvmretrofitexecutorsmovieapp.response.MovieSearchResponse;
import com.example.mvvmretrofitexecutorsmovieapp.utils.Credentials;
import com.example.mvvmretrofitexecutorsmovieapp.utils.MovieAPI;
import com.example.mvvmretrofitexecutorsmovieapp.viewmodels.MovieListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity implements OnMovieListener {

    private static final String TAG = "TAG";

    /**
     * MainActivity will get Data from ViewModel
     */

    /**
     * ***************************
     * ****  VERY IMP TIP *******
     *****************************
     *
     * inside rs folder add directory xml and then add network-security-config.xml
     *
     * otherwise API will not give response in EXECUTORS class
     * */

    /**
     * Before run our App, we need
     * to add the Network Security Config
     */
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    private MovieListViewModel movieListViewModel;

    boolean isPopular = true;

    private List<MovieModel> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Title of Toolbar
        setTitle("MovieApp");

        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        /** calling method to add Movie list into recyclerView */
        configureRecyclerVIew();

        /** Calling method the OBSERVER METHOD */
        observeAnyChange();

        /** Calling method the OBSERVER for Popular Movies METHOD */
        observePopularMovies();

        /**Calling Method , SearchVIew inside Toolbar */
        setUpSearchVIew();

        movieListViewModel.getPopMovie(1);

    }

    private void observePopularMovies() {
        movieListViewModel.getmPopMoviesListWithLiveData().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if (movieModels != null) {
                    // creating temp list of Movies to check click listner on recyclerview item
                    movieList.addAll(movieModels);
                    movieAdapter.setmMovies(movieModels);
                }
            }
        });

    }

    private void setUpSearchVIew() {

        //searchView Widget
        final SearchView searchView = findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                movieListViewModel.searchMovieApi(query, 1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPopular = false;
            }
        });
    }


    //Observing any data change
    private void observeAnyChange() {
        /** Observe Data from VIewModel , AUtomatic Update the RecyclerVIew */
        movieListViewModel.getmMoviesListWithLiveData().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                //TODO
                // now put list in recyclerview Adapter here..

                if (movieModels != null) {
                    // creating temp list of Movies to check click listner on recyclerview item
                    movieList.addAll(movieModels);

                    movieAdapter.setmMovies(movieModels);
                    /*for (MovieModel movieModel : movieModels) {

                        movieAdapter.setmMovies(movieModels);
                    }*/
                }

            }
        });

    }


    //Call the method in Main Activity
    /*private void searchMovieApi(String query, int pageNumber) {

        movieListViewModel.searchMovieApi(query, pageNumber);

    }*/

    // initializing recyclerVIew and Adding Data to it
    private void configureRecyclerVIew() {
        //Live Data cannot be passed in adapter Constructor
        movieAdapter = new MovieAdapter(this);

        recyclerView.setAdapter(movieAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        /**
         *
         * RecyclerVIew Pagination
         *
         * Loading next page of Api response
         **/

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if (!recyclerView.canScrollVertically(1)) {
                    //if vertically scroling stopped then show next page
                    movieListViewModel.searchNextPage();
                }

            }
        });


    }

    @Override
    public void onMovieClick(int position) {

        //OnCLick function on Movie-Item - Send Movie Details to Next Activity

        //We dont need position of the movie in recyclerView,
        //we need the ID of the movie in Order to get All its details

        Intent intent = new Intent(this, MovieDetails.class);
        //Sending whole Movie Object to DetailActivity
        intent.putExtra("movie", movieAdapter.getSelectedMovie(position));
        startActivity(intent);


    }

    @Override
    public void onCategoryClick(String category) {

    }
}