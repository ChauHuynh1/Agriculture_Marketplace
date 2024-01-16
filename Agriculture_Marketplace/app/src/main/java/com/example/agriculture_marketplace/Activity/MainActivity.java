package com.example.agriculture_marketplace.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.agriculture_marketplace.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private LinearLayout fragmentContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentContainer = findViewById(R.id.main_fragment_container);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_item2);
        bottomNavigationView.setOnItemSelectedListener(item -> {
                    int id = item.getItemId();
                    if (id == R.id.navigation_item1) {
                        replaceFragment(new BrowseForumFragment());
                        return true;
                    } else if (id == R.id.navigation_item2) {
                        return true;
                    } else if (id == R.id.navigation_item3) {
                        Intent intent = new Intent(this, CreateProductActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    }
                    return false;
                }
        );
        Intent intent = new Intent();
        intent.setClass(this, CreateProductActivity.class);
        startActivity(intent);
    }
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, fragment)
                .commit();
    }
}