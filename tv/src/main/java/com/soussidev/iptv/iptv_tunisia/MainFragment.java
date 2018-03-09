/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.soussidev.iptv.iptv_tunisia;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.soussidev.iptv.iptv_tunisia.Presenter.CustomListRow;
import com.soussidev.iptv.iptv_tunisia.Presenter.IconHeaderItem;
import com.soussidev.iptv.iptv_tunisia.Presenter.IconHeaderItemPresenter;
import com.soussidev.iptv.iptv_tunisia.model.Category;
import com.soussidev.iptv.iptv_tunisia.model.Channel;
import com.soussidev.iptv.iptv_tunisia.model.Vod;
import com.soussidev.iptv.iptv_tunisia.server.Constants;
import com.soussidev.iptv.iptv_tunisia.server.RequestInterface;
import com.soussidev.iptv.iptv_tunisia.server.ServerRequest;
import com.soussidev.iptv.iptv_tunisia.server.ServerResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;

public class MainFragment extends BrowseFragment {
    private static final String TAG = MainFragment.class.getSimpleName();

    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int GRID_ITEM_WIDTH = 240;
    private static final int GRID_ITEM_HEIGHT = 240;

    private final Handler mHandler = new Handler();
    private ArrayObjectAdapter mRowsAdapter;
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private String mBackgroundUri;
    private BackgroundManager mBackgroundManager;
    private List<Channel> listchannel ;
    private List<Category> listcategory;
    private List<Vod> listVod;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        super.onActivityCreated(savedInstanceState);

        prepareBackgroundManager();

        setupUIElements();

        getDataFromAPI();
        getCategoryapi();
        getVodApi();
        setupEventListeners();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mBackgroundTimer) {
            Log.d(TAG, "onDestroy: " + mBackgroundTimer.toString());
            mBackgroundTimer.cancel();
        }
    }
    /**
     * @auteur Soussi Mohamed
     * @see ServerRequest
     * @API channel
     */
    //Get API IPTV
    private void getDataFromAPI(){

        listchannel = new ArrayList<Channel>();

        RequestInterface requestInterface = Constants.getClient().create(RequestInterface.class);
        ServerRequest request = new ServerRequest();

        request.setOperation(Constants.GET_OPERATION.getChannel);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();

                Log.e(TAG,resp.getMessage());
                Log.e(TAG,resp.getResult());

                if(resp.getResult().equals(Constants.SUCCESS)){

                    int responseCode = response.code();
                    Log.d(TAG, String.valueOf(responseCode));

                    for (Channel cn : resp.getChannel()) {
                        listchannel.add(cn);
                        Log.i("Name Channel", String.valueOf(cn.getName_channel()));

                    }
                    //if get connexion [success] and arraylist is not empty load rows of channel IPTV
                    if (!listchannel.isEmpty())
                    {
                        loadRows();

                    }

                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Log.d(Constants.TAG,"failed channel");
                Log.d(Constants.TAG,t.getLocalizedMessage());


            }
        });
                 //check listchannel is empty or not
                 Log.d(Constants.TAG, String.valueOf(listchannel.isEmpty()));

    }

    /**
     * @auteur Soussi Mohamed
     * @see ServerRequest
     * @API category
     */
    private void getCategoryapi(){
        listcategory = new ArrayList<Category>();
        RequestInterface requestInterface = Constants.getClient().create(RequestInterface.class);
        ServerRequest request = new ServerRequest();

        request.setOperation(Constants.GET_OPERATION.getCategory);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse resp = response.body();

                Log.e(TAG,resp.getMessage());
                Log.e(TAG,resp.getResult());

                if(resp.getResult().equals(Constants.SUCCESS)){

                    int responseCode = response.code();
                    Log.d(TAG, String.valueOf(responseCode));

                    for (Category cn : resp.getCategories()) {
                        listcategory.add(cn);
                        Log.i("Name of Category ==>", String.valueOf(cn.getNom_cat()));


                    }

                    if (!listcategory.isEmpty())
                    {
                        loadRows();
                    }

                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d(Constants.TAG,"failed category");
                Log.d(Constants.TAG,t.getLocalizedMessage());
            }
        });
    }

    private void getVodApi(){
        listVod = new ArrayList<Vod>();
        RequestInterface requestInterface = Constants.getClient().create(RequestInterface.class);
        ServerRequest request = new ServerRequest();

        request.setOperation(Constants.GET_OPERATION.get_VOD);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse resp = response.body();

                Log.e(TAG,resp.getMessage());
                Log.e(TAG,resp.getResult());

                if(resp.getResult().equals(Constants.SUCCESS)){

                    int responseCode = response.code();
                    Log.d(TAG, String.valueOf(responseCode));

                    for (Vod cn : resp.getVods()) {
                        listVod.add(cn);
                        Log.i("Name of Vod ==>", String.valueOf(cn.getNom_vod()));


                    }

                    if (!listVod.isEmpty())
                    {
                        loadRows();
                    }

                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d(Constants.TAG,"failed category");
                Log.d(Constants.TAG,t.getLocalizedMessage());
            }
        });
    }

    private void loadRows() {

                 Log.d(Constants.TAG, String.valueOf(listchannel.isEmpty()));

                 mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
                 CardPresenter cardPresenter = new CardPresenter();

    // Menu All Channel
                 IconHeaderItem Allchannel = new IconHeaderItem(0, getResources().getString(R.string.menu_channel),R.mipmap.iconiptv);
                   for (int i = 0; i < listchannel.size(); i++) {
                    if (i != 0) {
                       Collections.shuffle(listchannel);
                       }
                    }

                  ArrayObjectAdapter listChannelAdapter = new ArrayObjectAdapter(cardPresenter);
                   if(listchannel !=null) {
                       for (int j = 0; j < listchannel.size(); j++) {
                           listChannelAdapter.add(listchannel.get(j));
                       }
                   }


                  mRowsAdapter.add(new CustomListRow(Allchannel, listChannelAdapter));

     // List Category
                  IconHeaderItem Allcat = new IconHeaderItem(1, getResources().getString(R.string.menu_category),R.mipmap.iconcategory);
                   ArrayObjectAdapter listCategoryAdapter = new ArrayObjectAdapter(cardPresenter);
                   if(listcategory!=null) {
                       for (int j = 0; j < listcategory.size(); j++) {
                           listCategoryAdapter.add(listcategory.get(j));
                       }
                   }
                  mRowsAdapter.add(new CustomListRow(Allcat, listCategoryAdapter));

     // List VOD
                  IconHeaderItem Allvod = new IconHeaderItem(2, getResources().getString(R.string.menu_vod),R.mipmap.iconvod);
                  ArrayObjectAdapter listVodAdapter = new ArrayObjectAdapter(cardPresenter);
                  if(listVod!=null) {
                      for (int j = 0; j < listVod.size(); j++) {
                          listVodAdapter.add(listVod.get(j));
                      }
                  }
                  mRowsAdapter.add(new CustomListRow(Allvod, listVodAdapter));

     // Propos
        IconHeaderItem propos = new IconHeaderItem(3, getResources().getString(R.string.menu_propos),R.mipmap.icon_propos);

                    GridItemPresenter mGridPresenter = new GridItemPresenter();
                    ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
                    gridRowAdapter.add(getResources().getString(R.string.menu_propos));
                    gridRowAdapter.add(getResources().getString(R.string.menu_settings));
                    mRowsAdapter.add(new CustomListRow(propos, gridRowAdapter));

          setAdapter(mRowsAdapter);
    }


    private void prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mDefaultBackground = getResources().getDrawable(R.drawable.thempropos);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void setupUIElements() {
         setBadgeDrawable(getActivity().getResources().getDrawable(
         R.drawable.iptv));

        setTitle(getString(R.string.browse_title)); // Badge, when set, takes precedent
        // over title
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(getResources().getColor(R.color.colorPrimary2));
        // set search icon color
        setSearchAffordanceColor(getResources().getColor(R.color.search_opaque));

        //for icon header
        setHeaderPresenterSelector(new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object o) {
                return new IconHeaderItemPresenter();
            }
        });

    }

    private void setupEventListeners() {
        setOnSearchClickedListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Implement your own in-app search", Toast.LENGTH_LONG)
                        .show();
            }
        });

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    protected void updateBackground(String uri) {
        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;



        Glide.with(getActivity())
                .load(uri)
                .centerCrop()
                .error(mDefaultBackground)
                .into(new SimpleTarget<GlideDrawable>(width, height) {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable>
                                                        glideAnimation) {
                        mBackgroundManager.setDrawable(resource);
                    }
                });
        mBackgroundTimer.cancel();
    }

    private void startBackgroundTimer() {
        if (null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Channel) {
                Channel channel = (Channel) item;
                Log.d(TAG, "Item: " + item.toString());
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.CHANNEL, channel);

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        DetailsActivity.SHARED_ELEMENT_NAME).toBundle();
                getActivity().startActivity(intent, bundle);
            }

            else if(item instanceof Vod) {
                Vod vod = (Vod) item;
                Log.d(TAG, "Item: " + item.toString());
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.VOD, vod);

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        DetailsActivity.SHARED_ELEMENT_NAME_VOD).toBundle();
                getActivity().startActivity(intent, bundle);
            }

            else if(item instanceof Category) {
                Category category = (Category) item;
                Log.d(TAG, "Item: " + item.toString());
                Intent intent = new Intent(getActivity(), VerticalGridActivity.class);
                intent.putExtra(VerticalGridActivity.CATEGORY, category);

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        VerticalGridActivity.SHARED_ELEMENT_NAME).toBundle();
                getActivity().startActivity(intent, bundle);
            }

            else if (item instanceof String) {
                if (((String) item).contains(getString(R.string.menu_propos))) {
                    Intent intent = new Intent(getActivity(), DialogActivity_TV.class);
                    startActivity(intent);
                }
                else if(((String) item).contains(getString(R.string.menu_settings)))
                {

                }
                else {

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Channel) {
                String path =Constants.BASE_URL+Constants.NAME_PATH;
               String name  = ((Channel) item).getUrl_image();
               // mBackgroundUri =path+name;
                mBackgroundManager.setDrawable(getResources().getDrawable(R.drawable.thempropos));
                startBackgroundTimer();
            }
            else if (item instanceof Vod) {
                String path =Constants.BASE_URL+Constants.NAME_PATH;
                String name  = ((Vod) item).getUrl_cover_vod();
               // mBackgroundUri =path+name;
                mBackgroundManager.setDrawable(getResources().getDrawable(R.drawable.thempropos1));
                startBackgroundTimer();
            }

            else if (item instanceof Category) {
                String path =Constants.BASE_URL+Constants.NAME_PATH;
                String name  = ((Category) item).getUrl_background_image();
               // mBackgroundUri =path+name;
                mBackgroundManager.setDrawable(getResources().getDrawable(R.drawable.thempropos2));
                startBackgroundTimer();
            }

            else if(item instanceof String){
                mBackgroundManager.setDrawable(getResources().getDrawable(R.drawable.thempropos));
                startBackgroundTimer();
            }
        }
    }

    private class UpdateBackgroundTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateBackground(mBackgroundUri);
                }
            });
        }
    }

    private class GridItemPresenter extends Presenter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            TextView view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            //view.setBackgroundColor(getResources().getColor(R.color.colorPrimary2));
            view.setBackground(getResources().getDrawable(R.drawable.them3));
            view.setTextColor(Color.WHITE);
            view.setGravity(Gravity.CENTER);
            return new ViewHolder(view);
        }

        @Override        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            ((TextView) viewHolder.view).setText((String) item);
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {
        }
    }

}
