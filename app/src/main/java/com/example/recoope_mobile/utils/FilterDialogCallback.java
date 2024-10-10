package com.example.recoope_mobile.utils;

import java.util.List;

public interface FilterDialogCallback {
    void onFilterSelected(List<String> filters, String closeAt, String minWeight, String maxWeight);  // Para aceitar todos os parâmetros de uma vez
}
