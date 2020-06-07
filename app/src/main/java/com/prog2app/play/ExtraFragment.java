package com.prog2app.play;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExtraFragment extends Fragment {
    LinearLayout whatsAppLayout,downloadLayout,settingLayout,moreAppsLayout,rateUsLayout,aboutUsLayout;
    private Switch sw1;
    boolean darkMode;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_extra, container, false);
        AdView mAdView = view.findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());
        sw1 = (Switch) view.findViewById(R.id.switch1);
        whatsAppLayout=(LinearLayout) view.findViewById(R.id.whatsAppLayout);
        downloadLayout=(LinearLayout) view.findViewById(R.id.downloadLayout);
        settingLayout=(LinearLayout) view.findViewById(R.id.settingLayout);
        moreAppsLayout=(LinearLayout) view.findViewById(R.id.moreAppsLayout);
        rateUsLayout=(LinearLayout) view.findViewById(R.id.rateUsLayout);
        aboutUsLayout=(LinearLayout) view.findViewById(R.id.aboutUsLayout);
        SharedPreferences darkModeSP = this.getActivity().getSharedPreferences("Log", MODE_PRIVATE);
        darkMode = darkModeSP.getBoolean("AppDarkMode", false);
        if(darkMode){
            sw1.setChecked(true);
        }else {
            sw1.setChecked(false);
        }
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor darkModeE = getActivity().getSharedPreferences("Log", MODE_PRIVATE).edit();
                if (sw1.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    darkModeE.putBoolean("AppDarkMode", true);
                    darkModeE.commit();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new ExtraFragment()).commit();
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    darkModeE.putBoolean("AppDarkMode", false);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new ExtraFragment()).commit();
                    darkModeE.commit();
                }
            }
        });

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
