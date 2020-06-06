package com.prog2app.play;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExtraFragment extends Fragment {
    LinearLayout whatsAppLayout,downloadLayout,themeLayout,settingLayout,moreAppsLayout,rateUsLayout,aboutUsLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_extra, container, false);
        AdView mAdView = view.findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());
        whatsAppLayout=(LinearLayout) view.findViewById(R.id.whatsAppLayout);
        downloadLayout=(LinearLayout) view.findViewById(R.id.downloadLayout);
        themeLayout=(LinearLayout) view.findViewById(R.id.themeLayout);
        settingLayout=(LinearLayout) view.findViewById(R.id.settingLayout);
        moreAppsLayout=(LinearLayout) view.findViewById(R.id.moreAppsLayout);
        rateUsLayout=(LinearLayout) view.findViewById(R.id.rateUsLayout);
        aboutUsLayout=(LinearLayout) view.findViewById(R.id.aboutUsLayout);

        whatsAppLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),WhatsappVideoList.class));
            }
        });
        downloadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),DownloadVideos.class));
            }
        });
        themeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Coming Soon",Toast.LENGTH_LONG).show();
            }
        });
        settingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Coming Soon",Toast.LENGTH_LONG).show();
            }
        });
        rateUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent    viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://play.google.com/store/apps/details?id=com.prog2app.play&hl=en"));
                startActivity(viewIntent);
            }
        });
        moreAppsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://play.google.com/store/apps/developer?id=prog2app"));
                startActivity(viewIntent);
            }
        });
        aboutUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),WebViewActivity.class));
            }
        });

        return view;
    }
}
