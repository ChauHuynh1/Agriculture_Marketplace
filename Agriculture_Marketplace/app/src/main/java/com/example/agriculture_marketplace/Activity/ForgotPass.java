package com.example.agriculture_marketplace.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agriculture_marketplace.databinding.ActivityForgotPassBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


// ...

public class ForgotPass extends AppCompatActivity {
    ActivityForgotPassBinding binding;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.fgEmail.getText().toString();
                if (email.isEmpty()) {
                    binding.fgEmail.setError("Enter Email");
                } else {
                    progressDialog.setTitle("Sending Password Reset Email");
                    progressDialog.setMessage("Please Wait");
                    progressDialog.show();

                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss(); // Dismiss the progressDialog when the operation is complete
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPass.this, "Please check your email", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgotPass.this, Login.class));
                            } else {
                                Toast.makeText(ForgotPass.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        binding.fgSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPass.this, Login.class));
            }
        });
    }
}
