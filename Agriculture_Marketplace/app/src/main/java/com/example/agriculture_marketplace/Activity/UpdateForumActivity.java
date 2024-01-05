package com.example.agriculture_marketplace.Activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agriculture_marketplace.Activity.Dialog.ForumImageUrlDialog;
import com.example.agriculture_marketplace.Config.ForumConfig;
import com.example.agriculture_marketplace.Forum.Model.Forum;
import com.example.agriculture_marketplace.Forum.Repository.ForumRepository;
import com.example.agriculture_marketplace.Helpers.RenderImageHelper;
import com.example.agriculture_marketplace.R;

public class UpdateForumActivity extends AppCompatActivity {
    private Forum currentForum;
    private static final String TAG = "[UpdateForumActivity]";
    private ImageView image;
    private String imageUrl;
    private EditText nameEditText, descriptionEditText;
    private Spinner categorySpinner;
    private Button updateForumButton, deleteForumButton, chooseImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_forum);

        currentForum = (Forum) getIntent().getSerializableExtra("forum");
    }

    public void init(){
        image = findViewById(R.id.create_forum_image);
        nameEditText = findViewById(R.id.create_forum_name);
        descriptionEditText = findViewById(R.id.create_forum_description);
        categorySpinner = findViewById(R.id.create_forum_category_spinner);
        renderSpinner();
        renderImage(currentForum.getImageUrl());
        nameEditText.setText(currentForum.getName());
        descriptionEditText.setText(currentForum.getDescription());
        updateForumButton = findViewById(R.id.create_forum_create_button);
        updateForumButton.setOnClickListener(v -> {
            updateForum();
        });
        deleteForumButton = findViewById(R.id.create_forum_delete_button);
        deleteForumButton.setOnClickListener(v -> {
            deleteForum();
        });
        chooseImageButton = findViewById(R.id.create_forum_choose_image);
        chooseImageButton.setOnClickListener(v -> {
            ForumImageUrlDialog dialog = new ForumImageUrlDialog(this, imageUrl -> {
                renderImage(imageUrl);
                this.imageUrl = imageUrl;
            });
            dialog.show();
        });

    }

    public void renderSpinner(){
        String[] categories = ForumConfig.FORUM_CATEGORY_LIST;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setSelection(adapter.getPosition(currentForum.getCategory()));
    }

    public void renderImage(String imageUrl){
        //? Consider changing to allow user to upload image from their device
         RenderImageHelper.renderImage(imageUrl,image);
    }

    public void updateForum(){
        //*Update forum to firebase
        currentForum.setName(nameEditText.getText().toString());
        currentForum.setDescription(descriptionEditText.getText().toString());
        currentForum.setCategory(categorySpinner.getSelectedItem().toString());
        currentForum.setImageUrl(imageUrl);

        ForumRepository forumRepository = new ForumRepository();
        forumRepository.updateForum(currentForum);
    }

    public void deleteForum(){
        //*Delete forum from firebase
        ForumRepository forumRepository = new ForumRepository();
        forumRepository.deleteForum(currentForum);
    }

}
