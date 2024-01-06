package com.example.agriculture_marketplace.Activity.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.agriculture_marketplace.R;

public class ForumDeleteConfirmDialog extends Dialog {
    private final OnMyDialogCallback myDialogCallback;
    Button confirmButton, cancelButton;
    public ForumDeleteConfirmDialog(@NonNull Context context, OnMyDialogCallback myDialogCallback) {
        super(context);
        this.myDialogCallback = myDialogCallback;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_delete_confirm_dialog);

        confirmButton = findViewById(R.id.forum_delete_confirm);
        cancelButton = findViewById(R.id.forum_delete_cancel);
        confirmButton.setOnClickListener(v -> {
            myDialogCallback.result();
            dismiss();
        });
        cancelButton.setOnClickListener(v -> {
            dismiss();
        });
    }
    public interface OnMyDialogCallback {
        void result();
    }
}
