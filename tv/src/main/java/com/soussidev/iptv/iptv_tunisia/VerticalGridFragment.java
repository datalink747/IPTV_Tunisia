package com.soussidev.iptv.iptv_tunisia;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.VerticalGridPresenter;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.soussidev.iptv.iptv_tunisia.model.Category;
import com.soussidev.iptv.iptv_tunisia.model.Channel;
import com.soussidev.iptv.iptv_tunisia.server.Constants;
import com.soussidev.iptv.iptv_tunisia.server.RequestInterface;
import com.soussidev.iptv.iptv_tunisia.server.ServerRequest;
import com.soussidev.iptv.iptv_tunisia.server.ServerResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerticalGridFragment extends android.support.v17.leanback.app.VerticalGridFragment {

    private static final String TAG = VerticalGridFragment.class.getSimpleName();
    private static final int NUM_COLUMNS = 5;
    private ArrayObjectAdapter mAdapter;
    private List<Channel> listchannel_filter ;
    private Category mSelectedChannel_filter;
    private BackgroundManager mBackgroundManager;
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private String mBackgroundUri;
    private final Handler mHandler = new Handler();
    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_LARGE;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        VerticalGridPresenter gridPresenter = new VerticalGridPresenter(ZOOM_FACTOR);
        gridPresenter.setNumberOfColumns(NUM_COLUMNS);
        setGridPresenter(gridPresenter);
        prepareBackgroundManager();


        mSelectedChannel_filter =
                (Category) getActivity().getIntent().getSerializableExtra(VerticalGridActivity.CATEGORY);
        if(mSelectedChannel_filter!=null)
        {
            Log.i("get intent for cat:==>",mSelectedChannel_filter.getNom_cat());
            setTitle(mSelectedChannel_filter.getNom_cat());


        }

        setupEventListeners();
        getChannel_filter_API();
        // it will move current focus to specified position. Comment out it to see the behavior.
        // setSelectedPosition(5);
    }


    private void setupFragment() {


        mAdapter = new ArrayObjectAdapter(new CardPresenter());

        List<Channel> channelsByCategory = listchannel_filter.stream().filter(u -> u.getCategory()
                .equals(mSelectedChannel_filter.getNom_cat()))
                .collect(Collectors.toList());

        Log.d("filter lambada:>", String.valueOf(channelsByCategory.size()));


        for (int j = 0; j < channelsByCategory.size(); j++) {
            mAdapter.add(channelsByCategory.get(j));
        }

        setAdapter(mAdapter);
    }


    /**
     * @auteur Soussi Mohamed
     * @see ServerRequest
     * @API channel
     */
    //Get API IPTV
    private void getChannel_filter_API(){

        listchannel_filter = new ArrayList<Channel>();

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
                        listchannel_filter.add(cn);
                        Log.i("Name Channel", String.valueOf(cn.getName_channel()));

                    }
                    //if get connexion [success] and arraylist is not empty load rows of channel IPTV
                    if (!listchannel_filter.isEmpty())
                    {
                        setupFragment();

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
        Log.d(Constants.TAG, String.valueOf(listchannel_filter.isEmpty()));

    }


    private void setupEventListeners() {
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Channel) {
                Channel channel = (Channel) item;

                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.CHANNEL, channel);
                getActivity().startActivity(intent);
            }
        }
    }

    private void prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mDefaultBackground = getResources().getDrawable(R.drawable.thempropos);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mBackgroundTimer) {
            Log.d(TAG, "onDestroy: " + mBackgroundTimer.toString());
            mBackgroundTimer.cancel();
        }
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


    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {

            if(item instanceof Channel)
            {

                mBackgroundManager.setDrawable(getResources().getDrawable(R.drawable.thempropos2));
                startBackgroundTimer();
            }



        }
    }


}
