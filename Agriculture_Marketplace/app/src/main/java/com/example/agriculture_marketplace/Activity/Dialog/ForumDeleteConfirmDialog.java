package com.example.agriculture_marketplace.Activity.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

public class ForumDeleteConfirmDialog extends Dialog {
    private final OnMyDialogCallback myDialogCallback;
    public ForumDeleteConfirmDialog(@NonNull Context context, OnMyDialogCallback myDialogCallback) {
        super(context);
        this.myDialogCallback = myDialogCallback;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDialogCallback.result();
        dismiss();
    }
    public interface OnMyDialogCallback {
        void result();
    }
}
