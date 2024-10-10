package com.example.recoope_mobile.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.recoope_mobile.R;
import com.example.recoope_mobile.activity.fragments.FeedFragment;
import com.example.recoope_mobile.enums.InvalidFormatLogin;
import com.example.recoope_mobile.enums.InvalidFormatRegister;

import java.util.ArrayList;
import java.util.List;

public class DialogUtils {

    private static final String LOG_TAG = "Register";

    public static void showCustomDialog(InvalidFormatRegister invalidFormatRegister, Context context) {
        if (context == null) {
            Log.e(LOG_TAG, "Context is null");
            return;
        }

        Dialog customDialog = new Dialog(context);
        customDialog.setContentView(R.layout.dialog_register);

        customDialog.setCancelable(false);
        customDialog.setCanceledOnTouchOutside(true);

        Window window = customDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.TOP | Gravity.END;
            layoutParams.x = 20;
            layoutParams.y = 20;
            window.setAttributes(layoutParams);
        }

        TextView txtTitulo = customDialog.findViewById(R.id.message);
        if (txtTitulo != null) {
            txtTitulo.setText(invalidFormatRegister.getType());
        }

        try {
            customDialog.show();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error showing dialog: " + e.getMessage(), e);
        }
    }

    public static void showCustomDialog(InvalidFormatLogin invalidFormatLogin, Context context) {
        if (context == null) {
            Log.e(LOG_TAG, "Context is null");
            return;
        }

        Dialog customDialog = new Dialog(context);
        customDialog.setContentView(R.layout.dialog_register);

        customDialog.setCancelable(false);
        customDialog.setCanceledOnTouchOutside(true);

        Window window = customDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.TOP | Gravity.END;
            layoutParams.x = 20;
            layoutParams.y = 20;
            window.setAttributes(layoutParams);
        }

        TextView txtTitulo = customDialog.findViewById(R.id.message);
        if (txtTitulo != null) {
            txtTitulo.setText(invalidFormatLogin.getType());
        }

        try {
            customDialog.show();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error showing dialog: " + e.getMessage(), e);
        }
    }

    public static void showFilterDialog(FeedFragment context, FilterDialogCallback filterDialogCallback) {
        if (context.getActivity() == null) {
            Log.e("CardFeed", "Context is null");
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(context.getActivity());
        View dialogView = inflater.inflate(R.layout.filter_dialog, null);

        Dialog customDialog = new Dialog(context.getActivity());
        customDialog.setContentView(dialogView);
        customDialog.setCancelable(true);
        customDialog.setCanceledOnTouchOutside(true);

        Window window = customDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;
            window.setAttributes(layoutParams);
        }

        // Inicializar o ButtonToggleManager para gerenciar os botões de filtro
        ButtonToggleManager buttonToggleManager = new ButtonToggleManager(context.getContext(), R.color.recoope_primary_color, R.color.background_color);

        // Elementos de filtro do diálogo
        Button applyFilterButton = dialogView.findViewById(R.id.btFilterMaterialD);
        Button btGlassFilter = dialogView.findViewById(R.id.btGlassFilterD);
        Button btMetalFilter = dialogView.findViewById(R.id.btMetalFilterD);
        Button btPlasticFilter = dialogView.findViewById(R.id.btPlasticFilterD);
        Button btPaperFilter = dialogView.findViewById(R.id.btPaperFilterD);
        EditText etCloseAtFilter = dialogView.findViewById(R.id.etCloseAtD);
        EditText etMinWeight = dialogView.findViewById(R.id.etMinWeightInputD);
        EditText etMaxWeight = dialogView.findViewById(R.id.etMaxWeightInputD);

        // Configurar a alternância de seleção e cores nos botões de filtro
        btGlassFilter.setOnClickListener(v -> buttonToggleManager.toggleButton(btGlassFilter));
        btMetalFilter.setOnClickListener(v -> buttonToggleManager.toggleButton(btMetalFilter));
        btPlasticFilter.setOnClickListener(v -> buttonToggleManager.toggleButton(btPlasticFilter));
        btPaperFilter.setOnClickListener(v -> buttonToggleManager.toggleButton(btPaperFilter));

        // Definir evento de clique para o campo de data e abrir o DatePickerDialog
        etCloseAtFilter.setOnClickListener(v -> {
            CalendarUtils.openDatePickerDialog(context.getActivity(), selectedDate -> {
                etCloseAtFilter.setText(selectedDate);
            });
        });

        // Aplicar os filtros ao clicar no botão 'Aplicar Filtros'
        applyFilterButton.setOnClickListener(v -> {
            List<String> filters = new ArrayList<>();
            if (btGlassFilter.isSelected()) filters.add("VIDRO");
            if (btMetalFilter.isSelected()) filters.add("METAL");
            if (btPlasticFilter.isSelected()) filters.add("PLASTICO");
            if (btPaperFilter.isSelected()) filters.add("PAPEL");

            String closeAt = etCloseAtFilter.getText().toString();
            String minWeight = etMinWeight.getText().toString();
            String maxWeight = etMaxWeight.getText().toString();

            // Validação e chamada de retorno
            if (context.validateFilters(closeAt, minWeight, maxWeight)) {
                context.applyAdditionalFilters(closeAt, minWeight, maxWeight);
                filterDialogCallback.onFilterSelected(filters, closeAt, minWeight, maxWeight);
                customDialog.dismiss();
            } else {
                DialogUtils.showCustomFeedDialog(context);  // Mostrar diálogo de erro de filtro
            }
        });

        customDialog.show();
    }

    public static void showCustomFeedDialog(FeedFragment context) {
        if (context == null) {
            Log.e(LOG_TAG, "Context is null");
            return;
        }

        Dialog customDialog = new Dialog(context.getActivity());
        customDialog.setContentView(R.layout.dialog_register);

        customDialog.setCancelable(false);
        customDialog.setCanceledOnTouchOutside(true);

        Window window = customDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.TOP | Gravity.END;
            layoutParams.x = 20;
            layoutParams.y = 20;
            window.setAttributes(layoutParams);
        }

        TextView txtTitulo = customDialog.findViewById(R.id.message);
        txtTitulo.setText("Filtros inválidos.");


        try {
            customDialog.show();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error showing dialog: " + e.getMessage(), e);
        }
        }
}
