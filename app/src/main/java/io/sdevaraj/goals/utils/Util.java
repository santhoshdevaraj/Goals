package io.sdevaraj.goals.utils;

import android.view.View;

import java.util.List;

/**
 * Created by sdevaraj on 4/13/16.
 */
public class Util {
    public static void showViews(List<View> views) {
        for (View view: views) {
            view.setVisibility(View.VISIBLE);
        }
    }
    public static void hideViews(List<View> views) {
        for (View view: views) {
            view.setVisibility(View.GONE);
        }
    }
}
