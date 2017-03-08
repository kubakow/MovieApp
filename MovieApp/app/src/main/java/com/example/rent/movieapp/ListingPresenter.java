package com.example.rent.movieapp;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import nucleus.presenter.Presenter;

/**
 * Created by RENT on 2017-03-07.
 */

public class ListingPresenter extends Presenter<ListingActivity> {

    public void getDataAsync(String title){
        new Thread(){
            @Override
            public void run() {
                try {
                    String result = getDataFromModel(title);
                    SearchResult searchResult = new Gson().fromJson(result, SearchResult.class);
                    getView().setDataFromModelOnUiThread(searchResult, false);
                } catch (IOException e) {
                    getView().setDataFromModelOnUiThread(null, true);
                }
            }
        }.start();
    }


    private String getDataFromModel(String title) throws IOException {
        String stringUrl = "https://www.omdbapi.com/?s="+title;
        URL url = new URL(stringUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(5000);
        InputStream inputStream = httpURLConnection.getInputStream();
        return convertStreamToString(inputStream);
    }

    public String convertStreamToString(java.io.InputStream is){
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


}
