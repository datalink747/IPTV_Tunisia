package com.soussidev.iptv.iptv_tunisia;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogFragmentTV extends GuidedStepFragment {

    private static final int ACTION_ID_RETURN = 1;
    private static final int ACTION_ID_SOCIAL = ACTION_ID_RETURN + 1;

    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {


        GuidanceStylist.Guidance guidance = new GuidanceStylist.Guidance(getResources().getString(R.string.app_name),
                getResources().getString(R.string.dev_description),
                getResources().getString(R.string.dev_name), getResources().getDrawable(R.drawable.iptv));
        return guidance;
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {

        GuidedAction action_retour = new GuidedAction.Builder()
                .id(ACTION_ID_RETURN)
                .icon(getResources().getDrawable(R.mipmap.iconretour))
                .title(getResources().getString(R.string.btn_dialog_return)).build();
        actions.add(action_retour);

        GuidedAction action_web = new GuidedAction.Builder()
                .id(ACTION_ID_SOCIAL)
                .icon(getResources().getDrawable(R.mipmap.iconlinkedin))
                .title(getResources().getString(R.string.btn_dialog_social)).build();
        actions.add(action_web);

    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        if (ACTION_ID_RETURN == action.getId()) {

        }
        else if(ACTION_ID_SOCIAL == action.getId())
        {
            Intent intent =new Intent(getActivity(),SiteWeb.class);
            intent.putExtra(DialogActivity_TV.GET_SOCIAL,getResources().getString(R.string.dev_social));
            startActivity(intent);
        }
        getActivity().finish();
    }
}
