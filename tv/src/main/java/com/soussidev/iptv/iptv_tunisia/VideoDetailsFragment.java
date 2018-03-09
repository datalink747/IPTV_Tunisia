/*
 * Copyright (C) 2014 The Android Open Source Project
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

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.app.DetailsFragmentBackgroundController;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.soussidev.iptv.iptv_tunisia.Presenter.DetailsPresenterCustom;
import com.soussidev.iptv.iptv_tunisia.model.Channel;
import com.soussidev.iptv.iptv_tunisia.model.Vod;
import com.soussidev.iptv.iptv_tunisia.server.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * LeanbackDetailsFragment extends DetailsFragment, a Wrapper fragment for leanback details screens.
 * It shows a detailed view of video and its meta plus related videos.
 */
public class VideoDetailsFragment extends DetailsFragment {
    private static final String TAG = VideoDetailsFragment.class.getSimpleName();

    private static final int ACTION_LIVE = 1;
    private static final int ACTION_PROGRAMME = 2;
    private static final int ACTION_WEB_SITE = 3;
    private static final int ACTION_RETOUR = 4;
    private static final int DETAIL_THUMB_WIDTH = 300;
    private static final int DETAIL_THUMB_HEIGHT = 300;
    private Channel mSelectedChannel;
    private Vod mSelectedVod;
    private ArrayObjectAdapter mAdapter;
    private ClassPresenterSelector mPresenterSelector;
    private DetailsFragmentBackgroundController mDetailsBackground;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate DetailsFragment");
        super.onCreate(savedInstanceState);

        mDetailsBackground = new DetailsFragmentBackgroundController(this);

        mSelectedChannel =
                (Channel) getActivity().getIntent().getSerializableExtra(DetailsActivity.CHANNEL);

        mSelectedVod =
                (Vod) getActivity().getIntent().getSerializableExtra(DetailsActivity.VOD);

        if(mSelectedChannel instanceof Channel)
        {


            if (mSelectedChannel != null) {
                mPresenterSelector = new ClassPresenterSelector();
                mAdapter = new ArrayObjectAdapter(mPresenterSelector);
                setupDetailsOverviewRow();
                setupDetailsOverviewRowPresenter();
                //  setupRelatedMovieListRow();
                setAdapter(mAdapter);
                initializeBackground(mSelectedChannel);
                //  setOnItemViewClickedListener(new ItemViewClickedListener());
            } else {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        }
        else if(mSelectedVod instanceof Vod)
        {
            if (mSelectedVod != null) {
                mPresenterSelector = new ClassPresenterSelector();
                mAdapter = new ArrayObjectAdapter(mPresenterSelector);
                setupDetailsOverviewRow_for_vod();
                setupDetailsOverviewRowPresenter_for_vod();
                //  setupRelatedMovieListRow();
                setAdapter(mAdapter);
                initializeBackground_for_vod(mSelectedVod);
                //  setOnItemViewClickedListener(new ItemViewClickedListener());
            } else {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        }

    }

    private void initializeBackground(Channel data) {


        mDetailsBackground.enableParallax();

        if(Constants.BASE_URL+Constants.NAME_PATH+data.getUrl_them()!= null)
        {
            Glide.with(getActivity())
                    .load(Constants.BASE_URL+Constants.NAME_PATH+data.getUrl_them())
                    .asBitmap()
                    .centerCrop()
                    .error(R.drawable.default_background)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap,
                                                    GlideAnimation<? super Bitmap> glideAnimation) {
                            mDetailsBackground.setCoverBitmap(bitmap);
                            mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
                        }
                    });
        }
        else {




        }

    }



    private void setupDetailsOverviewRow() {
        Log.d(TAG, "doInBackground: " + mSelectedChannel.toString());
        final DetailsOverviewRow row = new DetailsOverviewRow(mSelectedChannel);
        row.setImageDrawable(
                ContextCompat.getDrawable(getActivity(), R.drawable.default_background));
        int width = convertDpToPixel(getActivity().getApplicationContext(), DETAIL_THUMB_WIDTH);
        int height = convertDpToPixel(getActivity().getApplicationContext(), DETAIL_THUMB_HEIGHT);
        Glide.with(getActivity())
                .load(Constants.BASE_URL+Constants.NAME_PATH+mSelectedChannel.getUrl_image())
                .centerCrop()
                .error(R.drawable.default_background)
                .into(new SimpleTarget<GlideDrawable>(width, height) {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable>
                                                        glideAnimation) {
                        Log.d(TAG, "details overview card image url ready: " + resource);
                        row.setImageDrawable(resource);
                        mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
                    }
                });

        ArrayObjectAdapter actionAdapter = new ArrayObjectAdapter();




        actionAdapter.add(
                    new Action(
                            ACTION_LIVE,
                            getResources().getString(R.string.btn_menu_regarder),
                            "IPTV",getResources().getDrawable(R.drawable.lb_ic_play) ));



        actionAdapter.add(
                new Action(
                        ACTION_PROGRAMME,
                        getResources().getString(R.string.btn_menu_epg)));
        actionAdapter.add(
                new Action(
                        ACTION_WEB_SITE,
                        getResources().getString(R.string.btn_menu_siteweb)));
        actionAdapter.add(
                new Action(
                        ACTION_RETOUR,
                        getResources().getString(R.string.btn_menu_retour)));

        row.setActionsAdapter(actionAdapter);

        mAdapter.add(row);
    }

    private void setupDetailsOverviewRowPresenter() {
        // Set detail background.
        FullWidthDetailsOverviewRowPresenter detailsPresenter =
                new FullWidthDetailsOverviewRowPresenter(new DetailsPresenterCustom(getActivity()));
        detailsPresenter.setBackgroundColor(
                ContextCompat.getColor(getActivity(), R.color.grey));

        // Hook up transition element.
        FullWidthDetailsOverviewSharedElementHelper sharedElementHelper =
                new FullWidthDetailsOverviewSharedElementHelper();
        sharedElementHelper.setSharedElementEnterTransition(
                getActivity(), DetailsActivity.SHARED_ELEMENT_NAME);
        detailsPresenter.setListener(sharedElementHelper);
        detailsPresenter.setParticipatingEntranceTransition(true);

        detailsPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {
                if (action.getId() == ACTION_LIVE) {
                    if(!mSelectedChannel.getUrl_channel().isEmpty())
                    {
                        Intent intent = new Intent(getActivity(), PlaybackActivity.class);
                        intent.putExtra(DetailsActivity.CHANNEL, mSelectedChannel);
                        startActivity(intent);
                    }
                    else {

                        Intent intent = new Intent(getActivity(), BrowseErrorActivity.class);
                        intent.putExtra(BrowseErrorActivity.EURROR_MESSAGE,getResources().getString(R.string.error_channel));
                        startActivity(intent);
                    }

                }

                else if(action.getId() == ACTION_PROGRAMME)
                {

                }

                else if(action.getId() == ACTION_WEB_SITE)
                {
                       if(!mSelectedChannel.getSite_web().isEmpty())
                       {
                           Intent intent = new Intent(getActivity(), SiteWeb.class);
                           intent.putExtra(DetailsActivity.CHANNEL, mSelectedChannel);
                           startActivity(intent);
                       }
                       else {
                           Intent intent = new Intent(getActivity(), BrowseErrorActivity.class);
                           intent.putExtra(BrowseErrorActivity.EURROR_MESSAGE,getResources().getString(R.string.error_siteweb));
                           startActivity(intent);
                       }

                }
                else if(action.getId() == ACTION_RETOUR)
                {
                    getActivity().finish();
                }
                else {
                    Toast.makeText(getActivity(), action.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mPresenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsPresenter);
    }
 /**
  * @DEVELOPER :SOUSSI MOHAMED
  * @fragment : Detail fragment VOD
  * @start
  */
 private void initializeBackground_for_vod(Vod data) {


     mDetailsBackground.enableParallax();

     if(Constants.BASE_URL+Constants.NAME_PATH+data.getUrl_cover_vod()!= null)
     {
         Glide.with(getActivity())
                 .load(Constants.BASE_URL+Constants.NAME_PATH+data.getUrl_cover_vod())
                 .asBitmap()
                 .centerCrop()
                 .error(R.drawable.default_background)
                 .into(new SimpleTarget<Bitmap>() {
                     @Override
                     public void onResourceReady(Bitmap bitmap,
                                                 GlideAnimation<? super Bitmap> glideAnimation) {
                         mDetailsBackground.setCoverBitmap(bitmap);
                         mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
                     }
                 });
     }
     else {




     }

 }

    private void setupDetailsOverviewRow_for_vod() {
        Log.d(TAG, "doInBackground: " + mSelectedVod.toString());
        final DetailsOverviewRow row = new DetailsOverviewRow(mSelectedVod);
        row.setImageDrawable(
                ContextCompat.getDrawable(getActivity(), R.drawable.default_background));
        int width = convertDpToPixel(getActivity().getApplicationContext(), DETAIL_THUMB_WIDTH);
        int height = convertDpToPixel(getActivity().getApplicationContext(), DETAIL_THUMB_HEIGHT);
        Glide.with(getActivity())
                .load(Constants.BASE_URL+Constants.NAME_PATH+mSelectedVod.getUrl_cover_vod())
                .centerCrop()
                .error(R.drawable.default_background)
                .into(new SimpleTarget<GlideDrawable>(width, height) {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable>
                                                        glideAnimation) {
                        Log.d(TAG, "details overview card image url ready: " + resource);
                        row.setImageDrawable(resource);
                        mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
                    }
                });

        ArrayObjectAdapter actionAdapter = new ArrayObjectAdapter();

        actionAdapter.add(
                new Action(
                        ACTION_LIVE,
                        getResources().getString(R.string.btn_menu_regarder),"VOD",getResources().getDrawable(R.drawable.lb_ic_play)));

        actionAdapter.add(
                new Action(
                        ACTION_RETOUR,
                        getResources().getString(R.string.btn_menu_retour)));
        row.setActionsAdapter(actionAdapter);

        mAdapter.add(row);
    }

    private void setupDetailsOverviewRowPresenter_for_vod() {
        // Set detail background.
        FullWidthDetailsOverviewRowPresenter detailsPresenter =
                new FullWidthDetailsOverviewRowPresenter(new DetailsPresenterCustom(getActivity()));
        detailsPresenter.setBackgroundColor(
                ContextCompat.getColor(getActivity(), R.color.grey));


        // Hook up transition element.
        FullWidthDetailsOverviewSharedElementHelper sharedElementHelper =
                new FullWidthDetailsOverviewSharedElementHelper();
        sharedElementHelper.setSharedElementEnterTransition(
                getActivity(), DetailsActivity.SHARED_ELEMENT_NAME_VOD);
        detailsPresenter.setListener(sharedElementHelper);
        detailsPresenter.setParticipatingEntranceTransition(true);

        detailsPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {
                if (action.getId() == ACTION_LIVE) {


                    if(!mSelectedVod.getUrl_vod().isEmpty())
                    {
                        Intent intent = new Intent(getActivity(), PlaybackActivity.class);
                        intent.putExtra(DetailsActivity.VOD, mSelectedVod);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(getActivity(), BrowseErrorActivity.class);
                        intent.putExtra(BrowseErrorActivity.EURROR_MESSAGE,getResources().getString(R.string.error_siteweb));
                        startActivity(intent);
                    }

                }

                else if(action.getId() == ACTION_RETOUR)
                {
                    getActivity().finish();
                }
                else {
                    Toast.makeText(getActivity(), action.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mPresenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsPresenter);
    }


    /**
     * @DEVELOPER :SOUSSI MOHAMED
     * @fragment : Detail fragment VOD
     * @end
     */



  /*  private void setupRelatedMovieListRow() {
        String subcategories[] = {getString(R.string.related_movies)};
        List<Movie> list = MovieList.getList();

        Collections.shuffle(list);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
        for (int j = 0; j < NUM_COLS; j++) {
            listRowAdapter.add(list.get(j % 5));
        }

        HeaderItem header = new HeaderItem(0, subcategories[0]);
        mAdapter.add(new ListRow(header, listRowAdapter));
        mPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());
    }*/

    public int convertDpToPixel(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    /*private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Movie) {
                Log.d(TAG, "Item: " + item.toString());
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(getResources().getString(R.string.movie), mSelectedChannel);

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        DetailsActivity.SHARED_ELEMENT_NAME).toBundle();
                getActivity().startActivity(intent, bundle);
            }
        }
    }*/

    //check URL connection for channel or vod
    private void executeReq(URL urlObject) throws IOException
    {
        HttpURLConnection conn = null;
        conn = (HttpURLConnection) urlObject.openConnection();
        conn.setReadTimeout(30000);//milliseconds
        conn.setConnectTimeout(3500);//milliseconds
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        // Start connect
        conn.connect();
        InputStream response =conn.getInputStream();
        Log.d("Response:", response.toString());
    }




}


