package com.soussidev.iptv.iptv_tunisia;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;

public class DialogActivity_TV extends Activity {
    public static final String GET_SOCIAL = "social";

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));


        if (savedInstanceState == null) {
            GuidedStepFragment fragment = new DialogFragmentTV();
            GuidedStepFragment.addAsRoot(this, fragment, android.R.id.content);
        }
    }
}
