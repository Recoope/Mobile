package com.example.recoope_mobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.recoope_mobile.R;
import com.example.recoope_mobile.activity.fragments.CalendarFragment;
import com.example.recoope_mobile.activity.fragments.CompanyFragment;
import com.example.recoope_mobile.activity.fragments.FeedFragment;
import com.example.recoope_mobile.activity.fragments.PaymentsFragment;
import com.example.recoope_mobile.activity.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Fragment feedFragment;
    private Fragment searchFragment;
    private Fragment calendarFragment;
    private Fragment paymentsFragment;
    private Fragment userFragment;
    private BottomNavigationView navBar;
    private ProgressBar progressBar; // Adiciona a variável para o ProgressBar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        navBar = findViewById(R.id.navbar);
        progressBar = findViewById(R.id.progressBar); // Inicializa o ProgressBar

        navBar.setOnItemSelectedListener(item -> {
            showLoading(); // Mostra o ProgressBar ao selecionar um item
            int itemId = item.getItemId();

            if (itemId == R.id.home_button) {
                feedFragment = (feedFragment != null) ? feedFragment : new FeedFragment();
                replaceFragment(feedFragment);
            } else if (itemId == R.id.search_button) {
                searchFragment = (searchFragment != null) ? searchFragment : new SearchFragment();
                replaceFragment(searchFragment);
            } else if (itemId == R.id.calendar_button) {
                calendarFragment = (calendarFragment != null) ? calendarFragment : new CalendarFragment();
                replaceFragment(calendarFragment);
            } else if (itemId == R.id.payments_button) {
                paymentsFragment = (paymentsFragment != null) ? paymentsFragment : new PaymentsFragment();
                replaceFragment(paymentsFragment);
            } else if (itemId == R.id.user_button) {
                userFragment = (userFragment != null) ? userFragment : new CompanyFragment();
                replaceFragment(userFragment);
            } else return false;
            return true;
        });

        navBar.setSelectedItemId(R.id.home_button);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, fragment);
        fragmentTransaction.commit();
        hideLoading(); // Esconde o ProgressBar após a troca
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE); // Torna o ProgressBar visível
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE); // Esconde o ProgressBar
    }
}
