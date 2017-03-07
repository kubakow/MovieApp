package com.example.rent.movieapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class SearchActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);
        ImageButton searchButton = (ImageButton) findViewById(R.id.search_button);
        TextInputEditText textInputEditText = (TextInputEditText) findViewById(R.id.text_input_edit_text);
        searchButton.setOnClickListener(v ->

                startActivity(ListingActivity.createIntent(SearchActivity.this, textInputEditText.getText().toString())));


    }

}
