package com.android.vidrebany;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.android.vidrebany.fragments.AdminOrdersFragment;
import com.android.vidrebany.fragments.AdminProcessesFragment;
import com.android.vidrebany.fragments.AdminUsersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

@SuppressWarnings("deprecation")
public class AdminActivity extends AppCompatActivity {

    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin);



        //Actionbar and its title
        toolbar = findViewById(R.id.toolbar_main);


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Panel administrador");

        //home fragment transaction (default, on start)
        toolbar.setTitle("Processos"); //change actionbar title


        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnItemSelectedListener(selectedListener);
        if (savedInstanceState == null) {
            AdminUsersFragment fragment1 = new AdminUsersFragment();
            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
            ft1.replace(R.id.content, fragment1, "");
            ft1.commit();
        }


    }





    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressWarnings("deprecation")
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
                        case R.id.nav_processes:
                            toolbar.setTitle("Processos"); //change actionbar title
                            AdminProcessesFragment fragment2 = new AdminProcessesFragment();
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content, fragment2, "");
                            ft2.commit();

                            return true;
                        case R.id.nav_orders:
                            //profile fragment transaction
                            //       toolbar.setVisibility(View.GONE);

                            toolbar.setTitle("Ordres"); //change actionbar title
                            AdminOrdersFragment fragment3 = new AdminOrdersFragment();
                            FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.content, fragment3, "");
                            ft3.commit();

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