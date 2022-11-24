package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Untils.ReferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    TextView textviewsignup;
    EditText pass,email;
    Button btSignin;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    ReferenceManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhXa();
        btSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass.length()<=0||email.length()<=0){
                    Toast.makeText(LoginActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.signInWithEmailAndPassword(email.getText().toString().trim(),pass.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        manager.putString("iduser",task.getResult().getUser().getUid());
                                        manager.putString("email",task.getResult().getUser().getEmail());
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Password or Email is wrong!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        textviewsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignoutActivity.class);
                startActivity(intent);
            }
        });
    }

    private void anhXa() {
        textviewsignup=findViewById(R.id.txtsignup);
        btSignin=findViewById(R.id.btsignin);
        pass = findViewById(R.id.pass);
        email = findViewById(R.id.email);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        manager = new ReferenceManager(getApplicationContext());
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser!=null){
            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
            manager.putString("iduser",currentUser.getUid());
            manager.putString("email", currentUser.getEmail());
            startActivity(intent);
        }
    }
}