package com.example.studentscoremanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentscoremanagement.fragment.HomeFragment;
import com.example.studentscoremanagement.fragment.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements EventListener{

    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent getDaTa=getIntent();
        userName=getDaTa.getStringExtra(DBHelper.COL_TAIKHOAN_TEN);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this,  R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.homeFragment:
                        selectedFragment = HomeFragment.newInstance();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment,selectedFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        return true;

                    case R.id.userFragment:
                        selectedFragment = UserFragment.newInstance(userName);
                        transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment,selectedFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        return true;

                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment, selectedFragment);
                transaction.commit();
                return true;
            }
          });

        addControl();
        setEvent();
    }

    private void setEvent() {

    }

    private void addControl() {
    }

    @Override
    public void doSomething() {

    }
}