package com.example.recoope_mobile.utils;

import android.view.View;
import android.widget.ImageView;

import com.example.recoope_mobile.R;

public class StatusUtils {
    public static final int STATUS_SERVER_ERROR = R.drawable.internalerror;
    public static final int STATUS_NO_DATA = R.drawable.notfounditemserror;

    public static void showStatusImage(ImageView imageView, int drawableId) {
        if (imageView != null) {
            imageView.setImageResource(drawableId);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    public static void hideStatusImage(ImageView imageView) {
        if (imageView != null) {
            imageView.setVisibility(View.GONE);
        }
    }
}
