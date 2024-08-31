package com.example.recoope_mobile.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.recoope_mobile.R;
import com.example.recoope_mobile.activities.fragments.CalendarFragment;
import com.example.recoope_mobile.activities.fragments.FeedFragment;
import com.example.recoope_mobile.activities.fragments.PaymentsFragment;
import com.example.recoope_mobile.activities.fragments.SearchFragment;
import com.example.recoope_mobile.activities.fragments.CompanyFragment;
import com.google.android.material.navigation.NavigationBarView;

public class Main extends AppCompatActivity {

    private Fragment feedFragment;
    private Fragment searchFragment;
    private Fragment calendarFragment;
    private Fragment paymentsFragment;
    private Fragment userFragment;
    private NavigationBarView navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navBar = findViewById(R.id.navbar);

        // navBar.setBackground(null);
        navBar.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home_button) {
                if (feedFragment == null) feedFragment = new FeedFragment();
                replaceFragment(feedFragment);
            }
            else if (itemId == R.id.search_button) {
                if (searchFragment == null) searchFragment = new SearchFragment();
                replaceFragment(searchFragment);
            }
            else if (itemId == R.id.calendar_button) {
                if (calendarFragment == null) calendarFragment = new CalendarFragment();
                replaceFragment(calendarFragment);
            }
            else if (itemId == R.id.payments_button) {
                if (paymentsFragment == null) paymentsFragment = new PaymentsFragment();
                replaceFragment(paymentsFragment);
            }
            else if (itemId == R.id.user_button) {
                if (userFragment == null) userFragment = new CompanyFragment();
                replaceFragment(userFragment);
            }
            else return false;
            return true;
        });

        navBar.setSelectedItemId(R.id.home_button);
    }

    private void replaceFragment(Fragment fragment)  {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

}