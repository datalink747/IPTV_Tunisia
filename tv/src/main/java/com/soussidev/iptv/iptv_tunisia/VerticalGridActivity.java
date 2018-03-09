package com.soussidev.iptv.iptv_tunisia;

import android.app.Activity;
import android.os.Bundle;

public class VerticalGridActivity extends Activity {
    private static final String TAG = VerticalGridActivity.class.getSimpleName();
    public static final String SHARED_ELEMENT_NAME = "diver";
    public static final String CATEGORY = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_grid);
    }
}
