package com.soussidev.iptv.iptv_tunisia.server;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Soussi on 10/02/2018.
 */

public interface RequestInterface {

    @POST("IPTVTunisia/")
    Call<ServerResponse> operation(@Body ServerRequest request);
}
