package com.example.agriculture_marketplace.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agriculture_marketplace.Activity.Dialog.ForumImageUrlDialog;
import com.example.agriculture_marketplace.Config.ForumConfig;
import com.example.agriculture_marketplace.Forum.Model.Forum;
import com.example.agriculture_marketplace.Forum.Repository.ForumRepository;
import com.example.agriculture_marketplace.Helpers.RenderImageHelper;
import com.example.agriculture_marketplace.Helpers.UserManager;
import com.example.agriculture_marketplace.MainActivity;
import com.example.agriculture_marketplace.R;

public class CreateForumActivity extends AppCompatActivity {
    private static final String TAG = "[CreateForumActivity]";
    private Button chooseImageButton, createForumButton;
    private ImageView image;
    private Spinner categorySpinner;
    private String imageUrl;
    EditText nameEditText, descriptionEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_forum);
        init();

    }

    public void init(){
        chooseImageButton = findViewById(R.id.create_forum_choose_image);
        image = findViewById(R.id.create_forum_image);
        nameEditText = findViewById(R.id.create_forum_name);
        descriptionEditText = findViewById(R.id.create_forum_description);
        categorySpinner = findViewById(R.id.create_forum_category_spinner);
        createForumButton = findViewById(R.id.create_forum_create_button);
        createForumButton.setOnClickListener(v -> {
            createForum();
        });
        chooseImageButton.setOnClickListener(v -> {
            ForumImageUrlDialog dialog = new ForumImageUrlDialog(this, imageUrl -> {
                renderImage(imageUrl);
                this.imageUrl = imageUrl;
                Log.d(TAG, String.valueOf(imageUrl.getClass()));
            });
            dialog.show();
        });
        renderSpinner();
    }
    public void renderImage(String imageUrl){
        //? Consider changing to allow user to upload image from their device
        RenderImageHelper.renderImage(imageUrl,image);
    }
    public void renderSpinner(){
        String[] categories = ForumConfig.FORUM_CATEGORY_LIST;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setSelection(0);
    }
    public void createForum(){
        String name = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        if (name.isEmpty() || description.isEmpty() || category.isEmpty() ){
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        //TODO: Change the owner id to the current user id with FirebaseAuth
        String userId = UserManager.getInstance().getCurrentUser().getId();
        Forum forum = new Forum(name, description, category,userId, imageUrl);
        ForumRepository forumRepository = new ForumRepository();
        forumRepository.saveForumToFirebase(forum).thenAccept(res -> {
            Toast.makeText(this, "Create forum successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MyForumActivity.class);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
