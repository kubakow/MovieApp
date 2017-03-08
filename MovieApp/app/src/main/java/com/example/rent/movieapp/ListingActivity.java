package com.example.rent.movieapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(ListingPresenter.class)
public class ListingActivity extends NucleusAppCompatActivity<ListingPresenter> {


    private static final String SEARCH_TITLE = "search_title";
    private MovieListAdapter adapter;
    private ViewFlipper viewFlipper;
    private ImageView noInternetImage;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        adapter = new MovieListAdapter();
        String title = getIntent().getStringExtra(SEARCH_TITLE);
        recyclerView = (RecyclerView) findViewById(R.id.activity_listing_recycler_view);
        recyclerView.setAdapter(adapter);
        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        noInternetImage = (ImageView) findViewById(R.id.no_internet_image);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        getPresenter().getDataAsync(title);
    }

    public static Intent createIntent(Context context, String title) {
        Intent intent = new Intent(context, ListingActivity.class);
        intent.putExtra(SEARCH_TITLE, title);
        return intent;
    }

    public void setDataFromModelOnUiThread(SearchResult searchData, boolean isProblemWithInternetConnection) {

        runOnUiThread(() -> {
                if(isProblemWithInternetConnection){
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(noInternetImage));
        }else{
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(recyclerView));
                adapter.setItems(searchData.getItems());
                }
            });

    }

}
