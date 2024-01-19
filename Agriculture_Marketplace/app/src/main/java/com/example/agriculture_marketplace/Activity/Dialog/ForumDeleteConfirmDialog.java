package com.example.agriculture_marketplace.Activity.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.agriculture_marketplace.R;

public class ForumDeleteConfirmDialog extends Dialog {
    private final OnMyDialogCallback myDialogCallback;
    private String displayText;
    Button confirmButton, cancelButton;
    public ForumDeleteConfirmDialog(@NonNull Context context,String displayText, OnMyDialogCallback myDialogCallback) {
        super(context);
        this.myDialogCallback = myDialogCallback;
        this.displayText = displayText;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_delete_confirm_dialog);

        confirmButton = findViewById(R.id.forum_delete_confirm);
        cancelButton = findViewById(R.id.forum_delete_cancel);
        TextView textView = findViewById(R.id.forum_delete_confirm_text);
        textView.setText(displayText);
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
