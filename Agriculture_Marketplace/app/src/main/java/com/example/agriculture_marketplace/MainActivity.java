package com.example.agriculture_marketplace;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.agriculture_marketplace.Activity.BrowseForumFragment;
import com.example.agriculture_marketplace.Activity.BrowseProductFragment;
import com.example.agriculture_marketplace.Activity.Chat.Chat;
import com.example.agriculture_marketplace.Activity.Chat.ChatForum;
import com.example.agriculture_marketplace.Activity.Login;
import com.example.agriculture_marketplace.Activity.MyForumActivity;
import com.example.agriculture_marketplace.Activity.MyProductActivity;
import com.example.agriculture_marketplace.User.Model.User;
import com.example.agriculture_marketplace.User.Model.UserRepository;
import com.example.agriculture_marketplace.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView menu;
    TextView headerName, headerEmail;
    LinearLayout logout, aboutUs, myForum, myProduct;
    LinearLayout fragmentContainer;
    private static final String TAG = "MainActivity";
    private final UserRepository userRepository = new UserRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        logout = findViewById(R.id.logout);
        aboutUs= findViewById(R.id.aboutUs);
        fragmentContainer = findViewById(R.id.main_fragment_container);
        myForum = findViewById(R.id.my_forum);
        myProduct = findViewById(R.id.my_product);
        // Initialize the header views
        headerName = findViewById(R.id.header_name);
        headerEmail = findViewById(R.id.header_email);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_item1) {
                replaceFragment(new BrowseForumFragment());
            } else if (id == R.id.navigation_item2) {
               startActivity(new Intent(MainActivity.this, ChatForum.class));
            } else if (id == R.id.navigation_item3) {
                replaceFragment(new BrowseProductFragment());
            }
            return true;
        });

        replaceFragment(new BrowseForumFragment());
        // Set up the navigation drawer

        menu.setOnClickListener(view -> drawerLayout.openDrawer(Gravity.LEFT));
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });

        myForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MyForumActivity.class));
            }
        });
        myProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MyProductActivity.class));
            }
        });
        // Fetch and display user details in the header
        fetchAndDisplayUserDetails();
    }

    private void fetchAndDisplayUserDetails() {
        FirebaseUtil.currentUserDetails().get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User user = document.toObject(User.class);
                                if (user != null) {
                                    headerName.setText(user.getName());
                                    headerEmail.setText(user.getEmail());
                                }
                            }
                        } else {
                            // Handle errors
                            Toast.makeText(MainActivity.this, "Error fetching user details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
