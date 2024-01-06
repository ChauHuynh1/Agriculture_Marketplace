package com.example.agriculture_marketplace.Activity.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.agriculture_marketplace.R;

public class ForumImageUrlDialog extends Dialog {

    private final OnMyDialogCallback myDialogCallback;
    EditText imageUrlEditText;
    Button confirmButton, cancelButton;
    public ForumImageUrlDialog(@NonNull Context context, OnMyDialogCallback myDialogCallback ) {
        super(context);
        this.myDialogCallback = myDialogCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_image_url_dialog);
        imageUrlEditText = findViewById(R.id.forum_dialog_image_url);
        confirmButton = findViewById(R.id.forum_dialog_confirm_button);
        cancelButton = findViewById(R.id.forum_dialog_cancel_button);
        confirmButton.setOnClickListener(v -> {
            myDialogCallback.result(imageUrlEditText.getText().toString());
            dismiss();
        });
        cancelButton.setOnClickListener(v -> {
            dismiss();
        });
    }
    public interface OnMyDialogCallback {
        void result(String imageUrl);
    }
}
