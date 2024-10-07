package com.example.recoope_mobile.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.widget.Button;
import androidx.core.content.ContextCompat;

public class ButtonToggleManager {
    private Context context;
    private int selectedColorResId;
    private int defaultColorResId;

    public ButtonToggleManager(Context context, int selectedColorResId, int defaultColorResId) {
        this.context = context;
        this.selectedColorResId = selectedColorResId;
        this.defaultColorResId = defaultColorResId;
    }

    public void toggleButton(Button button) {
        if (button.isSelected()) {
            deactivateButton(button);
        } else {
            activateButton(button);
        }
    }

    private void activateButton(Button button) {
        button.setSelected(true);
        button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, selectedColorResId)));
        button.setTextColor(Color.WHITE);
    }

    private void deactivateButton(Button button) {
        button.setSelected(false);
        button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, defaultColorResId)));
        button.setTextColor(Color.BLACK);
    }
}
