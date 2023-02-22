package com.android.vidrebany;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText passwordEt;
    Button loginBtn;
    FirebaseDatabase fd;
    DatabaseReference dr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        passwordEt = findViewById(R.id.passwordEt);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(view -> {
            // Write a message to the database
            fd = FirebaseDatabase.getInstance();
            dr = fd.getReference("pass");

            dr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String input = passwordEt.getText().toString();
                    String adminpassword = Objects.requireNonNull(snapshot.child("adminpassword").getValue()).toString();
                    String userpassword = Objects.requireNonNull(snapshot.child("userpassword").getValue()).toString();
                    String transporterpassword = Objects.requireNonNull(snapshot.child("transporterpassword").getValue()).toString();
                    String serveipassword = Objects.requireNonNull(snapshot.child("serveipassword").getValue()).toString();
                    if (input.equals(userpassword)) {
                        Intent intent = new Intent(LoginActivity.this, UsersActivity.class);
                        intent.putExtra("role","user");
                        startActivity(intent);
                    }
                    else if (input.equals(adminpassword)) {
                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                        intent.putExtra("role","admin");
                        startActivity(intent);

                    } else if (input.equals(transporterpassword)) {
                        Intent intent = new Intent(LoginActivity.this, TransporterActivity.class);
                        intent.putExtra("role","transporter");
                        startActivity(intent);

                    } else if (input.equals(serveipassword)) {
                        Intent intent = new Intent(LoginActivity.this, ServeiTecnicActivity.class);
                        intent.putExtra("role","serveitecnic");
                        startActivity(intent);

                    } else {
                        Toast.makeText(LoginActivity.this, "La contrasenya no coincideix amb cap camp!", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }
}