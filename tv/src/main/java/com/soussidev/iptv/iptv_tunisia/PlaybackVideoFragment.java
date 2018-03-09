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

import android.os.Bundle;
import android.support.v17.leanback.app.VideoSupportFragment;
import android.support.v17.leanback.app.VideoSupportFragmentGlueHost;
import android.support.v17.leanback.media.MediaPlayerGlue;
import android.support.v17.leanback.media.PlaybackGlue;
import android.util.Log;

import com.soussidev.iptv.iptv_tunisia.model.Channel;
import com.soussidev.iptv.iptv_tunisia.model.Vod;
import com.soussidev.iptv.iptv_tunisia.server.Constants;

/**
 * Handles video playback with media controls.
 */
public class PlaybackVideoFragment extends VideoSupportFragment {
    private static final String TAG = PlaybackVideoFragment.class.getSimpleName();

    private MediaPlayerGlue mTransportControlGlue;
    private Channel movie;
    private Vod vod;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movie = (Channel) getActivity()
                .getIntent().getSerializableExtra(DetailsActivity.CHANNEL);
        vod = (Vod) getActivity()
                .getIntent().getSerializableExtra(DetailsActivity.VOD);
        if(movie instanceof Channel)
        {
            VideoSupportFragmentGlueHost glueHost =
                    new VideoSupportFragmentGlueHost(PlaybackVideoFragment.this);

            mTransportControlGlue = new MediaPlayerGlue(getActivity());
            mTransportControlGlue.setMode(MediaPlayerGlue.NO_REPEAT);
            mTransportControlGlue.setHost(glueHost);
            mTransportControlGlue.setTitle(movie.getName_channel());
            mTransportControlGlue.setArtist(movie.getCategory());
            mTransportControlGlue.addPlayerCallback(
                    new PlaybackGlue.PlayerCallback() {
                        @Override
                        public void onPreparedStateChanged(PlaybackGlue glue) {
                            if (glue.isPrepared()) {
                                glue.play();
                            }
                        }
                    });
            if(movie.getUrl_channel().startsWith("http://")) {
                mTransportControlGlue.setVideoUrl(movie.getUrl_channel());
                Log.d("url ip:==>", movie.getUrl_channel());
            }else
            {
                Log.d(TAG,"Eurreur lien");
            }
        }
        else if(vod instanceof Vod){

            VideoSupportFragmentGlueHost glueHost =
                    new VideoSupportFragmentGlueHost(PlaybackVideoFragment.this);

            mTransportControlGlue = new MediaPlayerGlue(getActivity());
            mTransportControlGlue.setMode(MediaPlayerGlue.NO_REPEAT);
            mTransportControlGlue.setHost(glueHost);

           // mTransportControlGlue.setCover(Drawable.createFromPath(vod.getUrl_cover_vod()));
            mTransportControlGlue.setTitle(vod.getNom_vod());
            mTransportControlGlue.setArtist(vod.getCat_vod());
            mTransportControlGlue.addPlayerCallback(
                    new PlaybackGlue.PlayerCallback() {
                        @Override
                        public void onPreparedStateChanged(PlaybackGlue glue) {
                            if (glue.isPrepared()) {
                                glue.play();
                            }
                        }
                    });
            if(vod.getSource().equals(Constants.SOURCE_VOD_EN_LIGNE))

            {
                if(vod.getSource().startsWith("http://")) {
                    mTransportControlGlue.setVideoUrl(vod.getUrl_vod());
                }
                else {
                    Log.d(TAG,"Eurreur lien");
                }
            }
            else if(vod.getSource().equals(Constants.SOURCE_VOD_EN_LOCAL))

            {
                mTransportControlGlue.setVideoUrl(Constants.BASE_URL+Constants.NAME_PATH+vod.getUrl_vod());
            }
            Log.d("url ip:==>",Constants.BASE_URL+Constants.NAME_PATH+vod.getUrl_vod());
        }







    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTransportControlGlue != null) {
            mTransportControlGlue.pause();
        }
    }
}