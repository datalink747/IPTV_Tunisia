package com.soussidev.iptv.iptv_tunisia.server;

import com.google.gson.annotations.SerializedName;
import com.soussidev.iptv.iptv_tunisia.model.Category;
import com.soussidev.iptv.iptv_tunisia.model.Channel;
import com.soussidev.iptv.iptv_tunisia.model.Programme;
import com.soussidev.iptv.iptv_tunisia.model.Vod;

/**
 * Created by Soussi on 10/02/2018.
 */

public class ServerRequest {

    private Constants.GET_OPERATION operation;

    @SerializedName(value="channel")
    private Channel channel;

    @SerializedName(value="category")
    private Category category;

    @SerializedName(value="vod")
    private Vod vod;

    @SerializedName(value="programme")
    private Programme programme;

    public void setOperation(Constants.GET_OPERATION operation) {
        this.operation = operation;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setVod(Vod vod) {
        this.vod = vod;
    }

    public void setProgramme(Programme programme) {
        this.programme = programme;
    }
}
