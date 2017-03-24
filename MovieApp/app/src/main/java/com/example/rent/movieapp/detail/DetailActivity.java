package com.example.rent.movieapp.detail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rent.movieapp.R;
import com.example.rent.movieapp.RetroFitProvider;
import com.example.rent.movieapp.detail.gallery.GalleryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;
@RequiresPresenter(DetailPresenter.class)
public class DetailActivity extends NucleusAppCompatActivity<DetailPresenter> {

    private static final String ID_KEY = "id_key";
    private Disposable subscribe;

    @BindView(R.id.list_item_poster)
    ImageView poster;
    @BindView(R.id.list_item_title_and_year)
    TextView titleAndYear;
    @BindView(R.id.actors_text_view)
    TextView actors;
    @BindView(R.id.director_text_view)
    TextView director;

    public static Intent createIntent(Context context, String imdbID){
        Intent intent = new Intent (context, DetailActivity.class);
        intent.putExtra(ID_KEY, imdbID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        String imdbID = getIntent().getStringExtra(ID_KEY);
        ButterKnife.bind(this);
        RetroFitProvider retroFitProvider = (RetroFitProvider) getApplication();
        getPresenter().setRetrofit(retroFitProvider.provideRetrofit());


        subscribe = getPresenter().loadDetail(imdbID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::success, this::error);



    }

    private void error(Throwable throwable) {
    }

    private void success(MovieItem movieItem) {
        Glide.with(this).load(movieItem.getPoster()).into(poster);
        director.setText(movieItem.getDirector());
        actors.setText(movieItem.getActors());
        titleAndYear.setText(movieItem.getTitle()+" ("+movieItem.getYear()+")");
        poster.setOnClickListener(v -> GalleryActivity.startActivity(DetailActivity.this, movieItem.getPoster(),poster));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(subscribe!=null){
            subscribe.dispose();
        }
    }
}
