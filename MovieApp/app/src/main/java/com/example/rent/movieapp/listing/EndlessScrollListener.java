package com.example.rent.movieapp.listing;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by RENT on 2017-03-11.
 */

public class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;
    private boolean isLoading;
    private OnLoadNextPageListener listener;
    private static final double PAGE_SIZE = 10;
    private CurrentItemListener currentItemListener;
    private ShowOrHideCounter showOrHideCounter;
    private boolean isCounterShown = true;

    public void setShowOrHideCounter(ShowOrHideCounter showOrHideCounter) {
        this.showOrHideCounter = showOrHideCounter;
    }

    public void setCurrentItemListener(CurrentItemListener currentItemListener) {
        this.currentItemListener = currentItemListener;
    }

    private int totalItemsNumber;

    public void setTotalItemsNumber(int totalItemsNumber) {
        this.totalItemsNumber = totalItemsNumber;
        isLoading = false;
    }

    public EndlessScrollListener(LinearLayoutManager layoutManager, OnLoadNextPageListener listener){
        this.layoutManager = layoutManager;
        this.listener = listener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int alreadyLoadedItems = layoutManager.getItemCount();
        double currentPage = alreadyLoadedItems/PAGE_SIZE;
        double numberOfAllPages = totalItemsNumber/PAGE_SIZE;
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()+1;

        if(lastVisibleItemPosition==alreadyLoadedItems && !isLoading && currentPage < numberOfAllPages) {
                loadNextPage((int) ++currentPage);
                isLoading = true;
        }
        if(currentItemListener!=null){
            currentItemListener.onNewCurrentItem(lastVisibleItemPosition, totalItemsNumber);
        }
    Log.d("result", String.valueOf(lastVisibleItemPosition));


    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if(showOrHideCounter != null){
            if(!isCounterShown && newState == RecyclerView.SCROLL_STATE_DRAGGING){
               showOrHideCounter.showCounter();
                isCounterShown = true;
            }else if(isCounterShown && newState==RecyclerView.SCROLL_STATE_IDLE){
                showOrHideCounter.hideCounter();
                isCounterShown = false;
            }
        }
    }

    private void loadNextPage(int pageNumber) {
    listener.loadNextPage(pageNumber);
    }
}
