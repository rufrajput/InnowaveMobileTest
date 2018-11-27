package com.innowave.mobiletest.application;


import android.content.Context;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import com.innowave.mobiletest.R;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Ubuntu-R.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


    }

}