package com.soussidev.iptv.iptv_tunisia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.soussidev.iptv.iptv_tunisia.model.Channel;

public class SiteWeb extends Activity {
    private Channel mSelectedChannel;
    private String getSosial;
    private static final String TAG =SiteWeb.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_web);
/**
 * @auteur Soussi Mohamed
 * {@link WebView}
 */
        mSelectedChannel =
                (Channel)this.getIntent().getSerializableExtra(DetailsActivity.CHANNEL);
        //

        Intent receivedIntent=this.getIntent();
        getSosial=receivedIntent.getStringExtra(DialogActivity_TV.GET_SOCIAL);
       //

        WebView browser = (WebView) findViewById(R.id.web_url);
        browser.setWebViewClient(new WebViewClient());
        browser.getSettings().setJavaScriptEnabled(true);



        if(mSelectedChannel instanceof Channel)
        {
            Log.d(TAG,mSelectedChannel.getSite_web());
            browser.loadUrl(mSelectedChannel.getSite_web());
        }

        else if(getSosial instanceof String)
        {
            Log.d(TAG, getSosial);
            browser.loadUrl(getSosial);
        }
        else {
            this.finish();
        }



    }
}
