package com.example.rent.movieapp.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.rent.movieapp.RetroFitProvider;

import io.reactivex.Observable;
import nucleus.presenter.Presenter;
import retrofit2.Retrofit;

/**
 * Created by RENT on 2017-03-13.
 */

public class DetailPresenter extends Presenter<DetailActivity>{

    private Retrofit retrofit;

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }


    public Observable<MovieItem> loadDetail(String imdbID){
        DetailService detailService = retrofit.create(DetailService.class);
        return detailService.getDetailInfo(imdbID);
    }
}
