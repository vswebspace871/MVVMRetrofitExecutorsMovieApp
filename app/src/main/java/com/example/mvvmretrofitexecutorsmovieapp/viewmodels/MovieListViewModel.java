package com.example.mvvmretrofitexecutorsmovieapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvmretrofitexecutorsmovieapp.models.MovieModel;
import com.example.mvvmretrofitexecutorsmovieapp.repository.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    /**
     * ViewmOdel will get Data from Repository
     */

    private MovieRepository movieRepository;

    public MovieListViewModel() {
        movieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getmMoviesListWithLiveData() {

        return movieRepository.getMovies();

    }
    public LiveData<List<MovieModel>> getmPopMoviesListWithLiveData() {

        return movieRepository.getPopMovies();

    }



    public void searchMovieApi(String query, int pageNumber) {

        movieRepository.searchMovieApi(query, pageNumber);
    }

    public void getPopMovie(int pageNumber) {

        movieRepository.getPopMovie(pageNumber);
    }



    public void searchNextPage(){
        movieRepository.searchNextPage();
    }
}
