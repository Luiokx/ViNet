package com.salvatierra.vinet;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainNav extends AppCompatActivity {
    private MainActivity menu = new MainActivity();
    private SearchActivity search = new SearchActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainnav);

        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.navMenu);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.mainNavigation:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutNavigation, menu).commit();
                        return true;
                    case R.id.searchNavigation:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutNavigation, search).commit();
                        return true;
                    case R.id.profileNavigation:

                        break;
                }

                return false;
            }
        });
        navigationView.setSelectedItemId(R.id.mainNavigation);
    }
}
