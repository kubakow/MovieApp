package com.example.rent.movieapp.listing;

import android.util.Log;

import com.example.rent.movieapp.search.SearchResult;
import com.example.rent.movieapp.search.SearchService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import nucleus.presenter.Presenter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RENT on 2017-03-07.
 */

public class ListingPresenter extends Presenter<ListingActivity> implements OnLoadNextPageListener {

    private Retrofit retrofit;
    private String title, type;
    private String stringYear;
    private ResultAggregator resultAggregator = new ResultAggregator();
    private BehaviorSubject<ResultAggregator> subject = BehaviorSubject.create();

    public void startLoadingItem(String title, int year, String type) {
        this.title = title;
        this.type = type;
        stringYear = year == ListingActivity.NO_YEAR_SELECTED ? null : String.valueOf(year);

        if (!subject.hasValue()) {
            loadNextPage(1);
        }
    }

    public Observable<ResultAggregator> subscribeOnLoadingResult() {
        return subject;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    @Override
    public void loadNextPage(int page) {
        retrofit.create(SearchService.class).search(page, title, stringYear, type)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(searchResult -> {
            resultAggregator.addNewItems(searchResult.getItems());
            resultAggregator.setTotalItemsResult(Integer.parseInt(searchResult.getTotalResults()));
            if (page > 1) {
                resultAggregator.setResponse("true");
            } else {
                resultAggregator.setResponse(searchResult.getResponse());
            }
            subject.onNext(resultAggregator);

        }, throwable -> {
            Log.d("result", throwable.toString());
        });

    }
}
