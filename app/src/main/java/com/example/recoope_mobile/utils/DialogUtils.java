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
import com.example.recoope_mobile.activity.fragments.BidFragment;
import com.example.recoope_mobile.activity.fragments.CooperativeFragment;
import com.example.recoope_mobile.activity.fragments.FeedFragment;
import com.example.recoope_mobile.enums.InvalidFormatLogin;
import com.example.recoope_mobile.enums.InvalidFormatRegister;
import com.example.recoope_mobile.enums.InvalidFormatUpdate;
import com.example.recoope_mobile.model.Payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static void showFilterDialog(FeedFragment context, FilterDialogCallback filterDialogCallback, ArrayList<String> activeFilters) {
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

        ButtonToggleManager buttonToggleManager = new ButtonToggleManager(context.getContext(), R.color.recoope_primary_color, R.color.white);

        // Elementos de filtro do diálogo
        Button applyFilterButton = dialogView.findViewById(R.id.btFilterMaterialD);
        Button btGlassFilter = dialogView.findViewById(R.id.btGlassFilterD);
        Button btMetalFilter = dialogView.findViewById(R.id.btMetalFilterCoop);
        Button btPlasticFilter = dialogView.findViewById(R.id.btPlasticFilterD);
        Button btPaperFilter = dialogView.findViewById(R.id.btPaperFilterD);
        EditText etCloseAtFilter = dialogView.findViewById(R.id.etCloseAtD);
        EditText etMinWeight = dialogView.findViewById(R.id.etMinWeightInputD);
        EditText etMaxWeight = dialogView.findViewById(R.id.etMaxWeightInputD);

        // Definir evento de clique para o campo de data e abrir o DatePickerDialog
        etCloseAtFilter.setOnClickListener(v -> {
            CalendarUtils.openDatePickerDialog(context.getActivity(), selectedDate -> {
                etCloseAtFilter.setText(selectedDate);
            });
        });

        // Mapear os botões com seus respectivos filtros
        Map<Button, String> filterMap = new HashMap<>();
        filterMap.put(btGlassFilter, "VIDRO");
        filterMap.put(btMetalFilter, "METAL");
        filterMap.put(btPlasticFilter, "PLASTICO");
        filterMap.put(btPaperFilter, "PAPEL");

        // Sincronizar o estado inicial dos filtros com base nos filtros ativos
        for (Map.Entry<Button, String> entry : filterMap.entrySet()) {
            if (activeFilters.contains(entry.getValue())) {
                entry.getKey().setSelected(true);
            }
        }

        // Configurar alternância de seleção e cores nos botões de filtro
        for (Button button : filterMap.keySet()) {
            button.setOnClickListener(v -> buttonToggleManager.toggleButton(button));
        }

        // Evento do botão de aplicar filtro
        applyFilterButton.setOnClickListener(v -> {
            List<String> filters = new ArrayList<>();
            for (Map.Entry<Button, String> entry : filterMap.entrySet()) {
                if (entry.getKey().isSelected()) {
                    filters.add(entry.getValue());
                }
            }

            String closeAt = etCloseAtFilter.getText().toString();
            String minWeight = etMinWeight.getText().toString();
            String maxWeight = etMaxWeight.getText().toString();

            // Chamando o callback para retornar os filtros selecionados
            if (context.validateFilters(closeAt, minWeight, maxWeight)) {
                context.applyAdditionalFilters(closeAt, minWeight, maxWeight);
                filterDialogCallback.onFilterSelected(filters, closeAt, minWeight, maxWeight);
                customDialog.dismiss();
            } else {
                DialogUtils.showCustomFeedDialog(context);
            }
        });

        customDialog.show();
    }

    public static void showFilterDialog(CooperativeFragment context, FilterDialogCallback filterDialogCallback, ArrayList<String> activeFilters) {
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

        ButtonToggleManager buttonToggleManager = new ButtonToggleManager(context.getContext(), R.color.recoope_primary_color, R.color.white);

        // Elementos de filtro do diálogo
        Button applyFilterButton = dialogView.findViewById(R.id.btFilterMaterialD);
        Button btGlassFilter = dialogView.findViewById(R.id.btGlassFilterD);
        Button btMetalFilter = dialogView.findViewById(R.id.btMetalFilterCoop);
        Button btPlasticFilter = dialogView.findViewById(R.id.btPlasticFilterD);
        Button btPaperFilter = dialogView.findViewById(R.id.btPaperFilterD);
        EditText etCloseAtFilter = dialogView.findViewById(R.id.etCloseAtD);
        EditText etMinWeight = dialogView.findViewById(R.id.etMinWeightInputD);
        EditText etMaxWeight = dialogView.findViewById(R.id.etMaxWeightInputD);

        // Definir evento de clique para o campo de data e abrir o DatePickerDialog
        etCloseAtFilter.setOnClickListener(v -> {
            CalendarUtils.openDatePickerDialog(context.getActivity(), selectedDate -> {
                etCloseAtFilter.setText(selectedDate);
            });
        });

        // Mapear os botões com seus respectivos filtros
        Map<Button, String> filterMap = new HashMap<>();
        filterMap.put(btGlassFilter, "VIDRO");
        filterMap.put(btMetalFilter, "METAL");
        filterMap.put(btPlasticFilter, "PLASTICO");
        filterMap.put(btPaperFilter, "PAPEL");

        // Sincronizar o estado inicial dos filtros com base nos filtros ativos
        for (Map.Entry<Button, String> entry : filterMap.entrySet()) {
            if (activeFilters.contains(entry.getValue())) {
                entry.getKey().setSelected(true);
            }
        }

        // Configurar alternância de seleção e cores nos botões de filtro
        for (Button button : filterMap.keySet()) {
            button.setOnClickListener(v -> buttonToggleManager.toggleButton(button));
        }

        // Evento do botão de aplicar filtro
        applyFilterButton.setOnClickListener(v -> {
            List<String> filters = new ArrayList<>();
            for (Map.Entry<Button, String> entry : filterMap.entrySet()) {
                if (entry.getKey().isSelected()) {
                    filters.add(entry.getValue());
                }
            }

            String closeAt = etCloseAtFilter.getText().toString();
            String minWeight = etMinWeight.getText().toString();
            String maxWeight = etMaxWeight.getText().toString();

            // Chamando o callback para retornar os filtros selecionados
            if (context.validateFilters(closeAt, minWeight, maxWeight)) {
                context.applyAdditionalFilters(closeAt, minWeight, maxWeight);
                filterDialogCallback.onFilterSelected(filters, closeAt, minWeight, maxWeight);
                customDialog.dismiss();
            } else {
                DialogUtils.showCustomFeedDialog(context);
            }
        });

        customDialog.show();
    }


    public static void showCustomFeedDialog(FeedFragment context) {
        if (context == null) {
            Log.e("CardFeed", "Context is null");
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
            Log.e("CardFeed", "Error showing dialog: " + e.getMessage(), e);
        }
    }

    public static void showCustomFeedDialog(CooperativeFragment context) {
        if (context == null) {
            Log.e("CardFeed", "Context is null");
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
            Log.e("CardFeed", "Error showing dialog: " + e.getMessage(), e);
        }
    }

    public static void showPaymentDialog(Payment payment, Context context) {
        Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.full_payments_dialog);
        dialog.getWindow().setLayout(1000, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView txtNumPaymentD = dialog.findViewById(R.id.txtNumPaymentD);
        if (txtNumPaymentD == null) {
            Log.e("DialogUtils", "txtNumPaymentD não encontrado");
        } else {
            txtNumPaymentD.setText(String.valueOf(payment.getId()));
        }

        TextView txtDatePaymentD = dialog.findViewById(R.id.txtDatePaymentD);
        if (txtDatePaymentD == null) {
            Log.e("DialogUtils", "txtDatePaymentD não encontrado");
        } else {
            txtDatePaymentD.setText(PtBrUtils.formatDate(PtBrUtils.parseDate(payment.getEmissionDate())));
        }

        TextView txtCooperativeName = dialog.findViewById(R.id.txtCooperativeName);
        if (txtCooperativeName == null) {
            Log.e("DialogUtils", "txtCooperativeName não encontrado");
        } else {
            txtCooperativeName.setText(payment.getCooperativeName());
        }

        TextView txtCnpjPayment = dialog.findViewById(R.id.txtCnpjPayment);
        if (txtCnpjPayment == null) {
            Log.e("DialogUtils", "txtCnpjPayment não encontrado");
        } else {
            txtCnpjPayment.setText(payment.getCompanyCnpj());
        }

        TextView txtPayerNamePaymentD = dialog.findViewById(R.id.txtPayerNamePaymentD);
        if (txtPayerNamePaymentD == null) {
            Log.e("DialogUtils", "txtPayerNamePaymentD não encontrado");
        } else {
            txtPayerNamePaymentD.setText(payment.getCompanyName());
        }

        TextView txtPayerCnpjD = dialog.findViewById(R.id.txtPayerCnpjD);
        if (txtPayerCnpjD == null) {
            Log.e("DialogUtils", "txtPayerCnpjD não encontrado");
        } else {
            txtPayerCnpjD.setText(payment.getCompanyCnpj());
        }

        TextView txtTimePaymentD = dialog.findViewById(R.id.txtTimePaymentD);
        if (txtTimePaymentD == null) {
            Log.e("DialogUtils", "txtTimePaymentD não encontrado");
        } else {
            txtTimePaymentD.setText(payment.getEmissionTime());
        }

        dialog.show();
    }

    public static void showCustomBidDialog(BidFragment context, String message) {
        if (context == null) {
            Log.e("CardFeed", "Context is null");
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
        txtTitulo.setText(message);

        try {
            customDialog.show();
        } catch (Exception e) {
            Log.e("CardFeed", "Error showing dialog: " + e.getMessage(), e);
        }
    }

    public static void showCustomDialog(InvalidFormatUpdate invalidFormatUpdate, Context context) {
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
            txtTitulo.setText(invalidFormatUpdate.getMessage());
        }

        try {
            customDialog.show();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error showing dialog: " + e.getMessage(), e);
        }
    }

    public static void showCustomDialog(String message, Context context) {
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
            txtTitulo.setText(message);
        }

        try {
            customDialog.show();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error showing dialog: " + e.getMessage(), e);
        }
    }

}
