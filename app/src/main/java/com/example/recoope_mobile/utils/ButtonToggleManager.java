package com.example.recoope_mobile.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.widget.Button;
import androidx.core.content.ContextCompat;

public class ButtonToggleManager {
    private final Context context;
    private final int selectedColor;
    private final int defaultColor;
    private final int selectedTextColor;
    private final int defaultTextColor;

    public ButtonToggleManager(Context context, int selectedColorResId, int defaultColorResId) {
        this.context = context;
        this.selectedColor = ContextCompat.getColor(context, selectedColorResId);
        this.defaultColor = ContextCompat.getColor(context, defaultColorResId);
        this.selectedTextColor = ContextCompat.getColor(context, android.R.color.white); // Cor do texto quando selecionado
        this.defaultTextColor = ContextCompat.getColor(context, android.R.color.black);  // Cor do texto quando não selecionado
    }

    // Método para alternar o estado do botão
    public void toggleButton(Button button) {
        boolean isSelected = !button.isSelected();  // Alternar o estado do botão
        setButtonState(button, isSelected);
    }

    // Método para definir o estado visual do botão diretamente
    public void setButtonState(Button button, boolean isSelected) {
        button.setSelected(isSelected);  // Definir o estado do botão
        updateButtonColors(button, isSelected);  // Atualizar as cores de fundo e texto com base no estado
    }

    // Método para aplicar a cor de fundo e cor do texto com base no estado
    private void updateButtonColors(Button button, boolean isSelected) {
        // Usar ColorStateList para manter transições suaves de cores e facilitar a gestão de estados
        ColorStateList backgroundColor = ColorStateList.valueOf(isSelected ? selectedColor : defaultColor);
        ColorStateList textColor = ColorStateList.valueOf(isSelected ? selectedTextColor : defaultTextColor);

        button.setBackgroundTintList(backgroundColor); // Usar tint para aplicar a cor de fundo
        button.setTextColor(textColor); // Definir a cor do texto
    }
}
