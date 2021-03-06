package com.example.rent.movieapp.search;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.example.rent.movieapp.R;
import com.example.rent.movieapp.RetroFitProvider;
import com.example.rent.movieapp.detail.DetailActivity;
import com.example.rent.movieapp.listing.ListingActivity;
import com.example.rent.movieapp.listing.OnMovieItemClickListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.reactivex.Observable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity implements OnMovieItemClickListener {

    private static final String NUMBER_PICKER_STATE = "number_picker_state";

    private Map<Integer, String> apiKeysMap = new HashMap<Integer,String>(){{
        put(R.id.radio_movie, "movie");
        put(R.id.radio_episode, "episode");
        put(R.id.radio_game, "game");
        put(R.id.radio_series, "series");}};

    @BindView(R.id.number_picker_search)
    NumberPicker numberPicker;

    @BindView(R.id.search_button)
    ImageButton searchButton;

    @BindView(R.id.text_input_edit_text)
    TextInputEditText textInputEditText;

    @BindView(R.id.year_checkbox)
    CheckBox yearBox;

    @BindView(R.id.type_checkbox)
    CheckBox typeCheckBox;

    @BindView(R.id.radio_group)
    RadioGroup radioGroup;

    @BindView(R.id.poster_header)
    RecyclerView posterHeaderRecyclerView;
    private PosterRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);
        ButterKnife.bind(this);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        numberPicker.setMinValue(1950);
        numberPicker.setValue(year);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setMaxValue(year+3);

        textInputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                   onSearchButtonClick();
                }
                return true;
            }
        });

        adapter = new PosterRecyclerViewAdapter();

        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
        posterHeaderRecyclerView.setLayoutManager(layoutManager);
        posterHeaderRecyclerView.setHasFixedSize(true);
        posterHeaderRecyclerView.setAdapter(adapter);
        posterHeaderRecyclerView.addOnScrollListener(new CenterScrollListener());
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());


        RetroFitProvider retroFitProvider = (RetroFitProvider) getApplication();

        Retrofit retrofit = retroFitProvider.provideRetrofit();
        SearchService searchService = retrofit.create(SearchService.class);
        searchService.search(1, "a*", "2016", null)
                .flatMap(searchResult -> Observable.fromIterable(searchResult.getItems()))
                    .map(movieListingItem -> new SimpleMovieItem(movieListingItem.getPoster(), movieListingItem.getImdbID()))
                    .filter(simpleMovieItem -> !"N/A".equalsIgnoreCase(simpleMovieItem.getPoster()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .toList()    
                    .subscribe(this::success, this::error);

        adapter.setOnMovieItemClickListener(this);
    }

    private void error(Throwable throwable) {
    }

    private void success(List<SimpleMovieItem> list) {
        adapter.setSimpleMovieItems(list);

    }

    @OnCheckedChanged(R.id.year_checkbox)
    void onCheckboxStateChanged(CompoundButton buttonView, boolean isChecked){
        numberPicker.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.search_button)
    void onSearchButtonClick(){
        int checkRadioId = radioGroup.getCheckedRadioButtonId();
        String typeKey = typeCheckBox.isChecked() ? apiKeysMap.get(checkRadioId) : null;
        int year = yearBox.isChecked() ? numberPicker.getValue() : ListingActivity.NO_YEAR_SELECTED;

        startActivity(ListingActivity.createIntent(SearchActivity.this,
                        textInputEditText.getText().toString(),
                                year, typeKey));
    }

    @OnCheckedChanged(R.id.type_checkbox)
    void onTypeCheckboxStateChanged(CompoundButton buttonView, boolean isChecked){
        radioGroup.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NUMBER_PICKER_STATE, numberPicker.getValue());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        numberPicker.setValue(savedInstanceState.getInt(NUMBER_PICKER_STATE));
    }

    @Override
    public void onMovieItemClick(String imdbID) {
        startActivity(DetailActivity.createIntent(this, imdbID));
    }
}
