package com.example.recoope_mobile.activities.company;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.recoope_mobile.R;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivityCompany extends AppCompatActivity {

    private Fragment feedFragment;
    private Fragment searchFragment;
    private Fragment calendarFragment;
    private Fragment paymentsFragment;
    private Fragment userFragment;
    private NavigationBarView navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        navBar = findViewById(R.id.navbar);

        // navBar.setBackground(null);
        navBar.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home_button) {
                feedFragment = (feedFragment != null) ? feedFragment : new FeedFragment();
                replaceFragment(feedFragment);
            }
            else if (itemId == R.id.search_button) {
                searchFragment = (searchFragment != null) ? searchFragment : new SearchFragment();
                replaceFragment(searchFragment);
            }
            else if (itemId == R.id.calendar_button) {
                calendarFragment = (calendarFragment != null) ? calendarFragment : new CalendarFragment();
                replaceFragment(calendarFragment);
            }
            else if (itemId == R.id.payments_button) {
                paymentsFragment = (paymentsFragment != null) ? paymentsFragment : new PaymentsFragment();
                replaceFragment(paymentsFragment);
            }
            else if (itemId == R.id.user_button) {
                userFragment = (userFragment != null) ? userFragment : new CompanyFragment();
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
        fragmentTransaction.replace(R.id.companyContent, fragment);
        fragmentTransaction.commit();
    }

}