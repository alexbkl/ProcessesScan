package com.android.vidrebany;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.vidrebany.fragments.AdminOrdersFragment;
import com.android.vidrebany.fragments.AdminUsersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminActivity extends AppCompatActivity {

    Button usersBtn, ordersBtn;
    private BottomNavigationView navigationView;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin);



        //Actionbar and its title
        toolbar = findViewById(R.id.toolbar_main);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Panel administrador");

        //home fragment transaction (default, on start)
        toolbar.setTitle("Usuaris"); //change actionbar title



        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);
        if (savedInstanceState == null) {
            AdminUsersFragment fragment1 = new AdminUsersFragment();
            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
            ft1.replace(R.id.content, fragment1, "");
            ft1.commit();
        } else {
        }


    }





    @Override
    protected void onResume() {
        super.onResume();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    //handle item clicks
                    switch (item.getItemId()){
                        case R.id.nav_users:
                            //        toolbar.setVisibility(View.GONE);

                            //home fragment transaction
                            toolbar.setTitle("Dades usuaris"); //change actionbar title
                            AdminUsersFragment fragment1 = new AdminUsersFragment();
                            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.content, fragment1, "");
                            ft1.commit();

                            return true;
                        case R.id.nav_orders:
                            //profile fragment transaction
                            //       toolbar.setVisibility(View.GONE);

                            toolbar.setTitle("Ordres"); //change actionbar title
                            AdminOrdersFragment fragment2 = new AdminOrdersFragment();
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content, fragment2, "");
                            ft2.commit();

                            return true;
                    }
                    return false;
                }
            };
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}