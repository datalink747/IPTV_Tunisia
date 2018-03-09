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

import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.soussidev.iptv.iptv_tunisia.model.Category;
import com.soussidev.iptv.iptv_tunisia.model.Channel;
import com.soussidev.iptv.iptv_tunisia.model.Vod;
import com.soussidev.iptv.iptv_tunisia.server.Constants;

/*
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an Image CardView
 */
public class CardPresenter extends Presenter {
    private static final String TAG = CardPresenter.class.getSimpleName();

    private static final int CARD_WIDTH = 313;
    private static final int CARD_HEIGHT = 270;
    private static final int CARD_WIDTH_cat = 213;
    private static final int CARD_HEIGHT_cat = 160;
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;
    private Drawable mDefaultCardImage;
    private Drawable badge_image_channel,badge_image_vod,badge_image_category;

    private static void updateCardBackgroundColor(ImageCardView view, boolean selected) {
        int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;
        // Both background colors should be set because the view's background is temporarily visible
        // during animations.
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Log.d(TAG, "onCreateViewHolder");

        sDefaultBackgroundColor = parent.getResources().getColor(R.color.default_background);
        sSelectedBackgroundColor = parent.getResources().getColor(R.color.colorPrimary2);
        /*
         * This template uses a default image in res/drawable, but the general case for Android TV
         * will require your resources in xhdpi. For more information, see
         * https://developer.android.com/training/tv/start/layouts.html#density-resources
         */
        mDefaultCardImage = parent.getResources().getDrawable(R.drawable.iptv);
        badge_image_channel = parent.getResources().getDrawable(R.mipmap.iconiptv);
        badge_image_category = parent.getResources().getDrawable(R.mipmap.iconcategory);
        badge_image_vod = parent.getResources().getDrawable(R.mipmap.iconvod);

        ImageCardView cardView = new ImageCardView(parent.getContext()) {
            @Override
            public void setSelected(boolean selected) {
                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };

        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateCardBackgroundColor(cardView, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {

        if(item instanceof Channel)
        {
            Channel channel = (Channel) item;
            ImageCardView cardView = (ImageCardView) viewHolder.view;

            Log.d(TAG, "onBindViewHolder");
            if (channel.getUrl_image() != null) {


                cardView.setTitleText(channel.getName_channel());
                cardView.setContentText(channel.getCategory());
                cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
                cardView.setElevation(10);
               cardView.setBadgeImage(badge_image_channel);
                cardView.setMainImageAdjustViewBounds(true);

                Glide.with(viewHolder.view.getContext())
                        .load(Constants.BASE_URL+Constants.NAME_PATH+channel.getUrl_image())//Constants.BASE_URL+Constants.NAME_PATH+channel.getUrl_image()
                        .centerCrop()
                        .error(mDefaultCardImage)
                        .into(cardView.getMainImageView());
            }
        }
        else if(item instanceof Category)
        {
            Category category = (Category) item;
            ImageCardView cardView = (ImageCardView) viewHolder.view;

            Log.d(TAG, "onBindViewHolder");
            if (category.getUrl_background_image() != null) {

                // cardView.getMainImageView().setBackgroundResource(R.drawable.font);
                cardView.setTitleText(category.getNom_cat());
                //cardView.setContentText(category.getNom_cat());
                cardView.setElevation(10);
                cardView.setMainImageDimensions(CARD_WIDTH_cat, CARD_HEIGHT_cat);
                cardView.setBadgeImage(badge_image_category);
                Glide.with(viewHolder.view.getContext())
                        .load(Constants.BASE_URL+Constants.NAME_PATH+category.getUrl_background_image())//Constants.BASE_URL+Constants.NAME_PATH+channel.getUrl_image()
                        .centerCrop()
                        .error(mDefaultCardImage)
                        .into(cardView.getMainImageView());
            }
        }
        else if(item instanceof Vod)
        {
            Vod vod = (Vod) item;
            ImageCardView cardView = (ImageCardView) viewHolder.view;

            Log.d(TAG, "onBindViewHolder");
            if (vod.getUrl_cover_vod() != null) {

                // cardView.getMainImageView().setBackgroundResource(R.drawable.font);
                cardView.setTitleText(vod.getNom_vod());
                cardView.setContentText(vod.getCat_vod());
                cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
                cardView.setBadgeImage(badge_image_vod);
                Glide.with(viewHolder.view.getContext())
                        .load(Constants.BASE_URL+Constants.NAME_PATH+vod.getUrl_cover_vod())//Constants.BASE_URL+Constants.NAME_PATH+channel.getUrl_image()
                        .centerCrop()
                        .error(mDefaultCardImage)
                        .into(cardView.getMainImageView());
            }
        }


    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        Log.d(TAG, "onUnbindViewHolder");
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        // Remove references to images so that the garbage collector can free up memory
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }
}
