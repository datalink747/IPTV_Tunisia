package com.soussidev.iptv.iptv_tunisia.server;

import android.util.Log;

import com.soussidev.iptv.iptv_tunisia.model.Category;
import com.soussidev.iptv.iptv_tunisia.model.Channel;
import com.soussidev.iptv.iptv_tunisia.model.Vod;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Soussi on 14/02/2018.
 */

public class List_service {
    private static List<Channel> listchannel ;
    private static List<Category> listcategory;
    private static List<Vod> listVod ;
    private static final String TAG = List_service.class.getSimpleName();

    /**
     * @auteur Soussi Mohamed
     * @API channel
     * @see ServerRequest
     */
    //Get API IPTV
    public static synchronized List<Channel> getChannel() {

            listchannel = new ArrayList<Channel>();

            RequestInterface requestInterface = Constants.getClient().create(RequestInterface.class);
            ServerRequest request = new ServerRequest();

            request.setOperation(Constants.GET_OPERATION.getChannel);
            Call<ServerResponse> response = requestInterface.operation(request);

            response.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                    ServerResponse resp = response.body();

                    Log.e(TAG, resp.getMessage());
                    Log.e(TAG, resp.getResult());

                    if (resp.getResult().equals(Constants.SUCCESS)) {

                        int responseCode = response.code();
                        Log.d(TAG, String.valueOf(responseCode));

                        for (Channel cn : resp.getChannel()) {
                            listchannel.add(cn);
                            Log.i("Name Channel", String.valueOf(cn.getName_channel()));

                        }


                    }

                }


                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

                    Log.d(Constants.TAG, "failed channel");
                    Log.d(Constants.TAG, t.getLocalizedMessage());


                }
            });



       // }

        return listchannel;
    }



}