package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Untils.ReferenceManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    Toolbar toolbar;
    ArrayList<TraiCay> traiCayArrayList;
    TraiCayAdapter adapter;
    ImageView image;
    TextView add;
    EditText nameedt,mota;
    String encodedImage;
    final int CODE_REQUEST_PICK_2 = 231;
    DatabaseReference database;
    ReferenceManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.listview1);
        manager = new ReferenceManager(getApplicationContext());
        database = FirebaseDatabase.getInstance().getReference();
        toolbar = findViewById(R.id.bar);
        getTool();
        traiCayArrayList =new ArrayList<>();
        adapter = new TraiCayAdapter(this,R.layout.dong_trai_cay,traiCayArrayList);
        lv.setAdapter(adapter);
        getProduct();
    }
    public void update(int position){
        TraiCay traiCay = traiCayArrayList.get(position);
        String name = traiCay.getTen();
        encodedImage = traiCay.getHinh();

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add);

        TextView title = dialog.findViewById(R.id.title);
        title.setText("Update Product");
        add = dialog.findViewById(R.id.add);
        add.setVisibility(View.INVISIBLE);
        image = dialog.findViewById(R.id.anh);
        image.setImageBitmap(getBitmap(traiCay.getHinh()));
        FrameLayout khung = dialog.findViewById(R.id.image);
        nameedt = dialog.findViewById(R.id.name);
        nameedt.setText(traiCay.getTen());
        mota = dialog.findViewById(R.id.mota);
        mota.setText(traiCay.getMoTa());
        Button xacnhan = dialog.findViewById(R.id.xacnhan);
        Button huy = dialog.findViewById(R.id.huy);

        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidSignUpDetails2()){
                    traiCayArrayList.get(position).setTen(nameedt.getText().toString().trim());
                    traiCayArrayList.get(position).setMoTa(mota.getText().toString().trim());
                    traiCayArrayList.get(position).setHinh(encodedImage);
                    database.child("users").child(manager.getString("iduser")).child("sanpham")
                            .addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    String key = snapshot.getKey().toString();
                                    if(snapshot.getValue(TraiCay.class).getTen().compareTo(name)==0){
                                        database.child("users").child(manager.getString("iduser")).child("sanpham").child(key)
                                                .child("ten").setValue(nameedt.getText().toString().trim());
                                        database.child("users").child(manager.getString("iduser")).child("sanpham").child(key)
                                                .child("hinh").setValue(encodedImage);
                                        database.child("users").child(manager.getString("iduser")).child("sanpham").child(key)
                                                .child("moTa").setValue(mota.getText().toString().trim());
                                        Toast.makeText(MainActivity.this, "Update success", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                    adapter.notifyDataSetChanged();
                    dialog.cancel();
                }
            }
        });
        khung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},CODE_REQUEST_PICK_2);
            }
        });
        dialog.show();
    }

    public void getContent(int position){
        Intent intent = new Intent(MainActivity.this,DetailActivity.class);
        Bundle bundle = new Bundle();
        TraiCay traicay = traiCayArrayList.get(position);
        bundle.putSerializable("goi",traicay);
        intent.putExtra("data",bundle);
        startActivity(intent);
    }
    public void remove(int position){
        TraiCay traiCay = traiCayArrayList.get(position);
        String name = traiCay.getTen();
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Notification!");
        dialog.setMessage("Do you want to remove this product?");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                traiCayArrayList.remove(position);
                database.child("users").child(manager.getString("iduser")).child("sanpham")
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                String key = snapshot.getKey().toString();
                                if(snapshot.getValue(TraiCay.class).getTen().compareTo(name)==0){
                                    database.child("users").child(manager.getString("iduser")).child("sanpham").child(key)
                                            .removeValue();
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                adapter.notifyDataSetChanged();
            }
        });
        dialog.show();
    }

    private void getProduct() {
        traiCayArrayList.clear();
        database.child("users").child(manager.getString("iduser")).child("sanpham")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        TraiCay traiCay = snapshot.getValue(TraiCay.class);
                        traiCayArrayList.add(new TraiCay(traiCay.getTen(),traiCay.getMoTa(),traiCay.getHinh()));
                        adapter.notifyDataSetChanged();
                        //Toast.makeText(MainActivity.this, traiCay.getTen()+"", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getTool() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Product");
        if(getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.add){
            encodedImage = null;
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_add);

            add = dialog.findViewById(R.id.add);
            image = dialog.findViewById(R.id.anh);
            FrameLayout khung = dialog.findViewById(R.id.image);
            nameedt = dialog.findViewById(R.id.name);
            mota = dialog.findViewById(R.id.mota);
            Button xacnhan = dialog.findViewById(R.id.xacnhan);
            Button huy = dialog.findViewById(R.id.huy);

            huy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            xacnhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isValidSignUpDetails()){
                        TraiCay traiCay = new TraiCay(nameedt.getText().toString().trim(),mota.getText().toString().trim(),encodedImage);
                        database.child("users").child(manager.getString("iduser")).child("sanpham").push().setValue(traiCay);
                        Toast.makeText(MainActivity.this, "Add Success!", Toast.LENGTH_SHORT).show();
                        //getProduct();
                        dialog.cancel();
                    }
                }
            });
            khung.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},CODE_REQUEST_PICK_2);
                }
            });
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data!=null){
            Uri url = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(url);
                Bitmap anh = BitmapFactory.decodeStream(inputStream);
                image.setImageBitmap(anh);
                add.setVisibility(View.INVISIBLE);
                encodedImage = encodeImage(anh);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_REQUEST_PICK_2 && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,CODE_REQUEST_PICK_2);
        }else showToast("You are not allowed");
    }

    private void showToast(String s) {
        Toast.makeText(getApplicationContext(), s+"", Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignUpDetails() {
        if (encodedImage == null) {
            showToast("Select profile image");
            return false;
        } else if (nameedt.getText().toString().trim().isEmpty()) {
            showToast("Enter name");
            return false;
        } else if (mota.getText().toString().trim().isEmpty()) {
            showToast("Enter a description");
            return false;
        } else return true;
    }

    private Boolean isValidSignUpDetails2() {
        if (nameedt.getText().toString().trim().isEmpty()) {
            showToast("Enter name");
            return false;
        } else if (mota.getText().toString().trim().isEmpty()) {
            showToast("Enter a description");
            return false;
        } else return true;
    }

    private Bitmap getBitmap(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}