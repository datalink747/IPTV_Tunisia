package com.soussidev.iptv.iptv_tunisia.server;

import com.google.gson.annotations.SerializedName;
import com.soussidev.iptv.iptv_tunisia.model.Category;
import com.soussidev.iptv.iptv_tunisia.model.Channel;
import com.soussidev.iptv.iptv_tunisia.model.Programme;
import com.soussidev.iptv.iptv_tunisia.model.Vod;

import java.util.List;

/**
 * Created by Soussi on 10/02/2018.
 */

public class ServerResponse {

    private String result;
    private String message;

    @SerializedName(value="channel")
    private List<Channel> channel;

    @SerializedName(value="category")
    private List<Category> categories;

    @SerializedName(value="vod")
    private List<Vod> vods;

    @SerializedName(value="programme")
    private List<Programme> programmes;

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public List<Channel> getChannel() {
        return channel;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Vod> getVods() {
        return vods;
    }

    public List<Programme> getProgrammes() {
        return programmes;
    }
}
