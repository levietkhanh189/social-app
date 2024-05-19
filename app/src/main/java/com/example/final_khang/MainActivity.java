package com.example.final_khang;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.final_khang.fragment.Fragment_Home;
import com.example.final_khang.fragment.Fragment_Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    //Variables
    private FrameLayout fragmentContainerr;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //MappingUI
        MappingUI();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    replaceFragmentContent(new Fragment_Home());
                } else if (item.getItemId() == R.id.nav_profile) {
                    replaceFragmentContent(new Fragment_Profile());
                } else if (item.getItemId() == R.id.nav_search) {
                    Toast.makeText(MainActivity.this, "Click vào Fragment Search", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.nav_notifications) {
                    Toast.makeText(MainActivity.this, "Click vào Fragment Notifications", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.nav_add_post) {
                    Intent intent = new Intent(MainActivity.this, NewPostActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });


        //initFragment()
        initFragmennt();
    }

    private void initFragmennt() {
        Fragment firstFragment = new Fragment_Home();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, firstFragment);
        fragmentTransaction.commit();
    }

    protected void replaceFragmentContent(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
    }

    private void MappingUI() {
        fragmentContainerr = (FrameLayout) findViewById(R.id.fragment_container);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
    }


}