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

import com.example.agriculture_marketplace.Activity.Login;
import com.example.agriculture_marketplace.User.Model.User;
import com.example.agriculture_marketplace.User.Model.UserRepository;
import com.example.agriculture_marketplace.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView menu;
    TextView headerName, headerEmail;
    LinearLayout logout;
    private static final String TAG = "MainActivity";
    private final UserRepository userRepository = new UserRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        logout = findViewById(R.id.logout);

        // Initialize the header views
        headerName = findViewById(R.id.header_name);
        headerEmail = findViewById(R.id.header_email);

        // Set up the navigation drawer
        menu.setOnClickListener(view -> drawerLayout.openDrawer(Gravity.LEFT));
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, Login.class));
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
}

//        User user = new User("An", "an123@gmail.com", "123123123");
//        userRepository.saveUserToFirebase(user);
//        Forum forum = new Forum("name1", "category1", "description1", "6guQj41QNErU1U5i1YNx");
//        Forum forum1 = new Forum("name2", "category2", "description1","6guQj41QNErU1U5i1YNx");
//        forumRepository.saveForumToFirebase(forum);
//        CompletableFuture<ArrayList<Forum>> future = forumRepository.getAllForums();
//        future.thenAccept(forums -> {
//            Log.d(TAG, "onCreate: " + forums.toString());
//        });
//        forumRepository.saveForumToFirebase(forum1);
//        MemberForum memberForum = new MemberForum("6guQj41QNErU1U5i1YNx", "2QsepQyv9ds6j0DzD7ip");
//        memberForumRepository.saveMemberForumToFirebase(memberForum);
//        forumRepository.getForumById("ixsVrpNhldKBGzR5JBBg").thenAccept(forum -> {
//            Log.d(TAG, "onCreate: " + forum.toString());
//            Intent intent = new Intent(this, Login.class);
//            startActivity(intent);
//        });