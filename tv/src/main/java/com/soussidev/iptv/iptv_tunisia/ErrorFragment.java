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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/*
 * This class demonstrates how to extend ErrorFragment
 */
public class ErrorFragment extends android.support.v17.leanback.app.ErrorFragment {
    private static final String TAG = ErrorFragment.class.getSimpleName();
    private static final boolean TRANSLUCENT = true;
    private String getMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.app_name));

        //StrictMode.enableDefaults();
        Intent receivedIntent=getActivity().getIntent();
        getMessage=receivedIntent.getStringExtra(BrowseErrorActivity.EURROR_MESSAGE);
        Log.d(TAG, getMessage);
    }

    void setErrorContent() {
        setImageDrawable(getResources().getDrawable(R.drawable.lb_ic_sad_cloud));
        setMessage(getResources().getString(R.string.error_siteweb));
        setDefaultBackground(TRANSLUCENT);

        setButtonText(getResources().getString(R.string.error_annuler));
        setButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                getFragmentManager().beginTransaction().remove(ErrorFragment.this).commit();
            }
        });
    }
}
