/*
 * Copyright (c) 2019. Mahmoud Galal.
 */

package com.mahmoudgalal.almajaltask.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mahmoudgalal.almajaltask.R;
import com.mahmoudgalal.almajaltask.Utils;
import com.mahmoudgalal.almajaltask.model.NewsItem;
import com.mahmoudgalal.almajaltask.widgets.SquareImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsDetailsFragment extends Fragment {


    public static final String ITEM_KEY = "com.mahmoudgalal.almajaltask.fragments.NewsDetailsFragment_ITEM_KEY";
    private TextView titleTxt,detailsTxt,detailsDateTxt,sectionNameTxt;
    private SquareImageView imageView ;
    private WebView detailsWebView;

    public NewsDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        imageView = root.findViewById(R.id.details_main_image);
        titleTxt = root.findViewById(R.id.details_title_txt);
        detailsTxt = root.findViewById(R.id.details_txt);
        detailsDateTxt = root.findViewById(R.id.details_date_txt);
        detailsWebView = root.findViewById(R.id.details_web_view);
        sectionNameTxt = root.findViewById(R.id.section_txt);

        Bundle args = getArguments();
        NewsItem item = args.<NewsItem>getParcelable(ITEM_KEY);
        titleTxt.setText(item.title);
        detailsDateTxt.setText(Utils.getLocalizedDateTime(item.creationDate));
        sectionNameTxt.setText(item.sectionName);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            detailsTxt.setText(Html.fromHtml(item.details,Html.FROM_HTML_MODE_LEGACY));
        } else {
            detailsTxt.setText(Html.fromHtml(item.details));
        }
        detailsWebView.loadData(item.details,"text/html","UTF-8");
        detailsWebView.getSettings().setBuiltInZoomControls(false);

        Glide.with(getContext()).load(item.mainImg).crossFade().
                placeholder(R.drawable.ic_launcher_background).
                error(R.drawable.image_error_background).
                centerCrop().into(imageView);
    }
}
