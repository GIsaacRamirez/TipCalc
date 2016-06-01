package edu.galileo.android.tipcalc;

import android.app.Application;

/**
 * Created by isaac on 31/05/16.
 */
public class TipCalcApp extends Application{
    private final static String ABOUT_URL="https://about.me/adriancatalan";

    public String getAboutUrl(){
        return ABOUT_URL;
    }

}
