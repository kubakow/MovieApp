package com.example.rent.movieapp.listing;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.rent.movieapp.MovieApplication;
import com.example.rent.movieapp.MovieDatabaseOpenHelper;
import com.example.rent.movieapp.MovieTableContract;
import com.example.rent.movieapp.R;
import com.example.rent.movieapp.detail.DetailActivity;
import com.example.rent.movieapp.search.SearchResult;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;
import retrofit2.Retrofit;

@RequiresPresenter(ListingPresenter.class)
public class ListingActivity extends NucleusAppCompatActivity<ListingPresenter> implements CurrentItemListener, OnLikeButtonClickListener, ShowOrHideCounter, OnMovieItemClickListener{


    private static final String SEARCH_TITLE = "search_title";
    private static final String SEARCH_YEAR = "search_year";
    private static final String SEARCH_TYPE = "search_type";
    public static final int NO_YEAR_SELECTED = -1;
    private MovieListAdapter adapter;

    @BindView(R.id.view_flipper)
    ViewFlipper viewFlipper;
    @BindView(R.id.no_internet_image)
    ImageView noInternetImage;
    @BindView(R.id.activity_listing_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.no_results)
    FrameLayout noResult;
    private EndlessScrollListener endlessScrollListener;
    @BindView(R.id.counter)
    TextView counterText;
    @BindView(R.id.swipe_refresher)
    SwipeRefreshLayout swipeRefreshLayout;
    private MovieDatabaseOpenHelper movieDatabaseOpenHelper;

    @Inject
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        ButterKnife.bind(this);
        MovieApplication movieApplication = (MovieApplication) getApplication();
        movieApplication.getAppComponent().inject(this);
        movieDatabaseOpenHelper = new MovieDatabaseOpenHelper(this);

        getPresenter().setRetrofit(retrofit);

        String title = getIntent().getStringExtra(SEARCH_TITLE);
        int year = getIntent().getIntExtra(SEARCH_YEAR, NO_YEAR_SELECTED);
        String type = getIntent().getStringExtra(SEARCH_TYPE);
        adapter = new MovieListAdapter();
        adapter.setOnMovieItemClickListener(this);
        adapter.setOnLikeButtonClickListener(this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        endlessScrollListener = new EndlessScrollListener(layoutManager, getPresenter());
        recyclerView.addOnScrollListener(endlessScrollListener);
        recyclerView.setLayoutManager(layoutManager);
        endlessScrollListener.setCurrentItemListener(this);
        endlessScrollListener.setShowOrHideCounter(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startLoading(title, year, type);
            }
        });

        getPresenter().subscribeOnLoadingResult()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::success, this::error);
        startLoading(title, year, type);
    }

    private void startLoading(String title, int year, String type) {
        getPresenter()
                .startLoadingItem(title, year, type);
    }

    private void error(Throwable throwable) {
        swipeRefreshLayout.setRefreshing(false);
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(noInternetImage));
    }

    private void success(ResultAggregator resultAggregator) {
        swipeRefreshLayout.setRefreshing(false);
        if ("False".equalsIgnoreCase(resultAggregator.getResponse())) {
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(noResult));
        } else {

            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(swipeRefreshLayout));
            adapter.setItems(resultAggregator.getMovieItems());
            endlessScrollListener.setTotalItemsNumber(resultAggregator.getTotalItemsResult());
        }
    }

    @OnClick(R.id.no_internet_image)
    public void onNoInternetImageView(View view) {
        Toast.makeText(this, "I've clicked image", Toast.LENGTH_LONG).show();
    }

    public static Intent createIntent(Context context, String title, int year, String type) {
        Intent intent = new Intent(context, ListingActivity.class);
        intent.putExtra(SEARCH_YEAR, year);
        intent.putExtra(SEARCH_TITLE, title);
        intent.putExtra(SEARCH_TYPE, type);
        return intent;
    }

    public void appendItems(SearchResult searchResult) {
        adapter.addItems(searchResult.getItems());
        endlessScrollListener.setTotalItemsNumber(Integer.parseInt(searchResult.getTotalResults()));
    }

    @Override
    public void onNewCurrentItem(int currentItem, int totalItemsCount) {
        counterText.setText(currentItem+ " of "+totalItemsCount);
    }

    @Override
    public void showCounter() {
            counterText.setVisibility(View.VISIBLE);
            counterText.animate().translationX(0).start();
    }

    @Override
    public void hideCounter() {
        counterText.animate().translationX(counterText.getWidth()*2).start();
    }

    @Override
    public void onMovieItemClick(String imdbID) {
        Toast.makeText(this, imdbID, Toast.LENGTH_LONG).show();
        startActivity(DetailActivity.createIntent(this, imdbID));
    }


    @Override
    public void onLikeButtonClick(MovieListingItem movieListingItem) {
        ContentValues values = new ContentValues();
        values.put(MovieTableContract.COLUMN_TITLE, movieListingItem.getTitle());
        values.put(MovieTableContract.COLUMN_YEAR, movieListingItem.getYear());
        values.put(MovieTableContract.COLUMN_TYPE, movieListingItem.getType());
        values.put(MovieTableContract.COLUMN_POSTER, movieListingItem.getPoster());
        movieDatabaseOpenHelper.getWritableDatabase().insert(MovieTableContract.TABLE_NAME, null, values);
    }
}
