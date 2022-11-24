package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Untils.ReferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SignoutActivity extends AppCompatActivity {
    private static final int CODE_REQUEST_PICK = 123;
    TextView textviewsignin,add;
    EditText user, email,pass,address;
    Button xacnhan;
    ImageView anhimg;
    FrameLayout anh;
    private String encodedImage;
    FirebaseAuth auth;
    DatabaseReference database;
    ReferenceManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signout);
        anhXa();
        textviewsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignoutActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangKi();
            }
        });
        anh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(SignoutActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},CODE_REQUEST_PICK);
            }
        });
    }

    private void dangKi() {
        final String email1 = email.getText().toString().trim();
        final String user1 = user.getText().toString().trim();
        final String pass1 = pass.getText().toString().trim();
        final String address1 = address.getText().toString().trim();
        if(isValidSignUpDetails()){
            auth.createUserWithEmailAndPassword(email1,pass1).addOnCompleteListener(SignoutActivity.this,
                    new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String id = task.getResult().getUser().getUid();
                        database.child("users").child(id).child("name").setValue(user1);
                        database.child("users").child(id).child("address").setValue(address1);
                        database.child("users").child(id).child("encodedImage").setValue(encodedImage);
                        Toast.makeText(SignoutActivity.this, "You are registered successfull.", Toast.LENGTH_SHORT).show();
                        //manager.putString("name",user1);
                        manager.putString("iduser",id);
                        manager.putString("email",email1);
                        //manager.putString("address",address1);
                        //manager.putString("profile",encodedImage);*/
                        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void anhXa() {
        textviewsignin = findViewById(R.id.txtsignin);
        user = findViewById(R.id.edtuser);
        email = findViewById(R.id.phone);
        pass = findViewById(R.id.edtpass);
        address = findViewById(R.id.conpass);
        xacnhan = findViewById(R.id.btsignup);
        anh = findViewById(R.id.image);
        add = findViewById(R.id.add);
        anhimg = findViewById(R.id.imageuser);
        database = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        manager = new ReferenceManager(SignoutActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_REQUEST_PICK && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,CODE_REQUEST_PICK);
        }else showToast("You are not allowed");
    }

    private void showToast(String s) {
        Toast.makeText(getApplicationContext(), s+"", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data!=null){
            Uri url = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(url);
                Bitmap anh = BitmapFactory.decodeStream(inputStream);
                anhimg.setImageBitmap(anh);
                add.setVisibility(View.INVISIBLE);
                encodedImage = encodeImage(anh);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private String encodeImage(Bitmap bitmap){
        int wigth = 150;
        int height = bitmap.getHeight()*wigth/bitmap.getWidth();
        Bitmap prebitmap = Bitmap.createScaledBitmap(bitmap,wigth,height,false);
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        prebitmap.compress(Bitmap.CompressFormat.JPEG,50,arrayOutputStream);
        byte[] bytes = arrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);

    }

    private Boolean isValidSignUpDetails() {
        if (encodedImage == null) {
            showToast("Select profile image");
            return false;
        } else if (user.getText().toString().trim().isEmpty()) {
            showToast("Enter name");
            return false;
        } else if (email.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            showToast("Enter valid email");
            return false;
        } else if (pass.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        }else if (address.getText().toString().trim().isEmpty()) {
            showToast("Password and confirm password must be same");
            return false;
        } else return true;
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }
}