package com.soussidev.iptv.iptv_tunisia.Presenter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v17.leanback.widget.Presenter;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TableLayout;
import android.widget.TextView;

import com.soussidev.iptv.iptv_tunisia.R;
import com.soussidev.iptv.iptv_tunisia.model.Channel;
import com.soussidev.iptv.iptv_tunisia.model.Programme;
import com.soussidev.iptv.iptv_tunisia.model.Vod;
import com.soussidev.iptv.iptv_tunisia.server.Constants;
import com.soussidev.iptv.iptv_tunisia.server.RequestInterface;
import com.soussidev.iptv.iptv_tunisia.server.ServerRequest;
import com.soussidev.iptv.iptv_tunisia.server.ServerResponse;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Soussi on 14/02/2018.
 */

public class DetailsPresenterCustom extends Presenter {

    private ResourceCache mResourceCache = new ResourceCache();
    private Context mContext;
    private List<Programme> listprogrammes;
    private List<Programme> listprogrammes_Filtre;
    public DetailsPresenterCustom(Context context) {
        mContext = context;
    }
    private TextView text_titre;
    private TextView text_cat;
    private TextView text_lang;
    private TextView text_time;
    private TextView text_contenu;
    private TextView text_discription;
    private TableLayout tableLayout;
    private TextView table_titre;
    private TextView table_start_time;
    private TextView table_end_time;
    private TextView table_descrption;
    private Calendar calendar;
    private SimpleDateFormat getCurentTime;
    private SimpleDateFormat getCurentDate;
    private SimpleDateFormat getCurentDate_filtre;
    private SimpleDateFormat getCurentH_filtre;
    private String strDate,strTime,getcurrent,getHeure;
    private TextView text_curentdate;
    private DateTimeFormatter dateTimeFormatter;
    private LocalDateTime localDateTime;

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.detail_view_content, null);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        text_titre = mResourceCache.getViewById(viewHolder.view, R.id.textview_titre);
        text_cat = mResourceCache.getViewById(viewHolder.view, R.id.textview_cat);
        text_lang = mResourceCache.getViewById(viewHolder.view, R.id.textview_lang);
        text_time = mResourceCache.getViewById(viewHolder.view, R.id.textview_time);
        text_contenu = mResourceCache.getViewById(viewHolder.view, R.id.textview_contenu);
        text_curentdate = mResourceCache.getViewById(viewHolder.view, R.id.textview_date);
        TextView text_discription_name = mResourceCache.getViewById(viewHolder.view, R.id.textview_name_discription);
        text_discription = mResourceCache.getViewById(viewHolder.view, R.id.textview_discription);

        tableLayout = mResourceCache.getViewById(viewHolder.view,R.id.presenterdetailProgramme_Table);
        table_titre = mResourceCache.getViewById(viewHolder.view,R.id.presenterTitre_programme);
        table_start_time = mResourceCache.getViewById(viewHolder.view,R.id.presenterDate_start_programme);
        table_end_time = mResourceCache.getViewById(viewHolder.view,R.id.presenterDate_end_programme);
        table_descrption = mResourceCache.getViewById(viewHolder.view,R.id.presenterDiscription_programme);

        calendar= Calendar.getInstance();
        getCurentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        getCurentTime = new SimpleDateFormat("HH:mm:ss",Locale.ITALY);
        getCurentDate_filtre = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        getCurentH_filtre = new SimpleDateFormat("HH", Locale.getDefault());
         strDate = getCurentDate.format(calendar.getTime());
         strTime = getCurentTime.format(calendar.getTime());
         getcurrent=getCurentDate_filtre.format(calendar.getTime());
        getHeure=getCurentH_filtre.format(calendar.getTime());
         text_curentdate.setText(strTime+" / "+strDate);

//using java 8
        /* localDateTime  = LocalDateTime.now();
         dateTimeFormatter  = DateTimeFormatter.ofPattern("HH:mm:ss");
        strTime=localDateTime.format(dateTimeFormatter);*/

        if(item instanceof Channel)
        {
            Channel channel = (Channel) item;

            getProgramme_API(channel);
            tableLayout.setVisibility(View.GONE);



            if (channel != null) {



                text_titre.setText(channel.getName_channel());
                text_cat.setText(mContext.getResources().getString(R.string.presenter_categorie)+channel.getCategory());
                text_lang.setText(mContext.getResources().getString(R.string.presenter_lang)+channel.getLang());
                if(!channel.getQuality_ch().isEmpty())
                {
                    text_contenu.setText(" "+channel.getQuality_ch());
                }
                else
                {
                    text_contenu.setText(mContext.getResources().getString(R.string.presenter_quality));
                }
                text_time.setVisibility(View.GONE);
                text_discription_name.setVisibility(View.GONE);
                text_discription.setVisibility(View.GONE);

            }
        }
        else if(item instanceof Vod)
        {
            Vod vod = (Vod) item;
            tableLayout.setVisibility(View.GONE);

            if (vod != null) {
                text_titre.setText(vod.getNom_vod());
                text_cat.setText(mContext.getResources().getString(R.string.presenter_categorie)+vod.getCat_vod());
                text_lang.setText(mContext.getResources().getString(R.string.presenter_lang)+vod.getLang_vod());
                text_time.setText(mContext.getResources().getString(R.string.presenter_dure)+vod.getTime_vod());

                if(!vod.getQuality_vod().isEmpty())
                {
                    text_contenu.setText(" "+vod.getQuality_vod());
                }
                else
                {
                    text_contenu.setText(mContext.getResources().getString(R.string.presenter_quality));
                }
                text_discription.setText(mContext.getResources().getString(R.string.presenter_description)+vod.getTime_vod()+vod.getDescription_vod());
                makeTextViewResizable(text_discription, 2, mContext.getResources().getString(R.string.presenter_custom_text_open), true, mContext);


            }
        }



    }

    @Override public void onUnbindViewHolder(ViewHolder viewHolder) {
        // Nothing to do here.
    }



    //big textview

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore ,final Context context) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore, context), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore, context), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore, context), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }
    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore, final Context context) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {

                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, context.getResources().getString(R.string.presenter_custom_text_close), false, context);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, context.getResources().getString(R.string.presenter_custom_text_open), true, context);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }


    /**
     * @auteur Soussi Mohamed
     * @see ServerRequest
     * @Function getProgramme_API
     */
    //Get Programme API IPTV
    private void getProgramme_API(Channel channel){

        listprogrammes = new ArrayList<Programme>();
        listprogrammes_Filtre = new ArrayList<Programme>();

        RequestInterface requestInterface = Constants.getClient().create(RequestInterface.class);
        ServerRequest request = new ServerRequest();

        request.setOperation(Constants.GET_OPERATION.getProgramme);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();

                Log.e("DetailPresenterCustom :",resp.getMessage());
                Log.e("DetailPresenterCustom :",resp.getResult());

                if(resp.getResult().equals(Constants.SUCCESS)){

                    int responseCode = response.code();
                    Log.d("DetailPresenterCustom :", String.valueOf(responseCode));

                    for (Programme cn : resp.getProgrammes()) {
                        listprogrammes.add(cn);
                        Log.d("curent date in database", cn.getDate_prog());
                        Log.d("curent date in time", cn.getStart_time());
                    }
                    //if get connexion [success] and arraylist is not empty load rows of Programme IPTV
                    if(listprogrammes!= null)
                    {

                        Log.d("curent date", strDate);
                        Log.d("curent time", strTime);
                        Log.e("Chargee :", String.valueOf(listprogrammes.isEmpty()));
                        int heur_= Integer.parseInt(getHeure);
                        heur_=heur_+1;
                        String heur_final= String.valueOf(heur_);

                        listprogrammes_Filtre = listprogrammes.stream().filter(u -> u.getName_channel()
                                .equals(channel.getName_channel())&& u.getDate_prog().equals(getcurrent)&&
                                u.getStart_time().startsWith(heur_final))
                                .collect(Collectors.toList());

                        Log.d("filter lambada final:>", String.valueOf(listprogrammes_Filtre.size()));
                        for (Programme cn : listprogrammes_Filtre) {
                             if(cn.getTitre_prog() != null)
                             {
                                 tableLayout.setVisibility(View.VISIBLE);
                                 table_titre.setText(cn.getTitre_prog());
                                 table_start_time.setText(cn.getStart_time());
                                 table_end_time.setText(cn.getEnd_time());
                                 table_descrption.setText(cn.getDescription_prog());

                             }
                             else {
                                 tableLayout.setVisibility(View.GONE);

                             }


                        }
                    }

                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Log.d(Constants.TAG,"failed Programme");
                Log.d(Constants.TAG,t.getLocalizedMessage());


            }
        });
        //check listProgramme is empty or not
        Log.d(Constants.TAG, String.valueOf(listprogrammes.isEmpty()));

    }

}