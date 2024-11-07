package com.example.recoope_mobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

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
    private FrameLayout progressOverlay;
    private Fragment currentFragment; // Fragmento atualmente exibido

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        navBar = findViewById(R.id.navbar);
        progressOverlay = findViewById(R.id.progressOverlay);

        navBar.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment selectedFragment = null;

            if (itemId == R.id.home_button) {
                feedFragment = (feedFragment != null) ? feedFragment : new FeedFragment();
                selectedFragment = feedFragment;
            } else if (itemId == R.id.search_button) {
                searchFragment = (searchFragment != null) ? searchFragment : new SearchFragment();
                selectedFragment = searchFragment;
            } else if (itemId == R.id.calendar_button) {
                calendarFragment = (calendarFragment != null) ? calendarFragment : new CalendarFragment();
                selectedFragment = calendarFragment;
            } else if (itemId == R.id.payments_button) {
                paymentsFragment = (paymentsFragment != null) ? paymentsFragment : new PaymentsFragment();
                selectedFragment = paymentsFragment;
            } else if (itemId == R.id.user_button) {
                userFragment = (userFragment != null) ? userFragment : new CompanyFragment();
                selectedFragment = userFragment;
            }

            if (selectedFragment != null && selectedFragment != currentFragment) {
                showLoading();
                replaceFragment(selectedFragment);
                currentFragment = selectedFragment;
            }
            return true;
        });

        // Define o fragmento inicial ao iniciar a Activity
        if (savedInstanceState == null) {
            navBar.setSelectedItemId(R.id.home_button);
            feedFragment = new FeedFragment();
            currentFragment = feedFragment;
            replaceFragment(feedFragment);
        }
    }

    private void replaceFragment(Fragment fragment) {
        if (currentFragment == fragment) return;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, fragment);
        fragmentTransaction.addToBackStack(null); // Permite voltar ao fragmento anterior com o botão "Voltar"
        fragmentTransaction.commitAllowingStateLoss();
    }

    // Método para mostrar o ProgressBar
    public void showLoading() {
        progressOverlay.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        progressOverlay.setVisibility(View.GONE);
    }
}
