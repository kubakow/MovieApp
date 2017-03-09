package com.example.rent.movieapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity {

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
}
