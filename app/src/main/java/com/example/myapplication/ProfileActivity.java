package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Untils.ReferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    ReferenceManager manager;
    TextView email,name,address;
    DatabaseReference database;
    Toolbar toolbar;
    ImageView anh;
    String name1,encodeImage1,address1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        anhXa();
        getToolBar();
        email.setText("Email: "+manager.getString("email"));
        database.child("users").child(manager.getString("iduser"))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()) {
                                    name1 = snapshot.child("name").getValue(String.class);
                                    encodeImage1 = snapshot.child("encodedImage").getValue(String.class);
                                    address1 = snapshot.child("address").getValue(String.class);
                                    anh.setImageBitmap(getBitmap(encodeImage1));
                                    name.setText(name1);
                                    address.setText("Address: "+address1);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

    }

    private void getToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My profile");
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void anhXa() {
        manager = new ReferenceManager(getApplicationContext());
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        toolbar = findViewById(R.id.toolbar);
        anh = findViewById(R.id.profile);
        address = findViewById(R.id.address);
        database = FirebaseDatabase.getInstance().getReference();
    }

    private Bitmap getBitmap(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}