package com.example.mvvmretrofitexecutorsmovieapp;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {
    // Singleton Pattern

    /**
     *
     * this class will get Data in Background Thread
     *
     * */

    private static AppExecutors instance;

    public static AppExecutors getInstance() {
        if (instance == null){
            instance = new AppExecutors();
        }
        return instance;
    }

    private final ScheduledExecutorService mNetworkIO = Executors.newScheduledThreadPool(3);
    //Three Thread
    //One fo connect Retroft
    //One for Cancel Retrofit

    public ScheduledExecutorService networkIO(){
        return  mNetworkIO;
    }

}
