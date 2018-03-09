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

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

import com.soussidev.iptv.iptv_tunisia.model.Channel;
import com.soussidev.iptv.iptv_tunisia.model.Vod;

public class DetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {

    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {

        if(item instanceof Channel)
        {
            Channel channel = (Channel) item;

            if (channel != null) {
                viewHolder.getTitle().setText(channel.getName_channel());
                viewHolder.getSubtitle().setText(channel.getCategory());
                viewHolder.getBody().setText(channel.getLang());

            }
        }
        else if(item instanceof Vod)
        {
            Vod vod = (Vod) item;

            if (vod != null) {
                viewHolder.getTitle().setText(vod.getNom_vod());
                viewHolder.getSubtitle().setText("Catégorie : "+vod.getCat_vod()+"\r\r"+" Durée : "+vod.getTime_vod());
                //viewHolder.getSubtitle().setText(R.string.presenter_categorie+ vod.getCat_vod()+" \r\r"+R.string.presenter_dure+vod.getTime_vod());
                viewHolder.getBody().setText(vod.getDescription_vod());


            }
        }

    }
}
