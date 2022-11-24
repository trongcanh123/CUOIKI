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

public class DetailActivity extends AppCompatActivity {
    TextView name, mota;
    ImageView anh;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        name = findViewById(R.id.textView);
        mota = findViewById(R.id.textView2);
        toolbar = findViewById(R.id.toolbar);
        anh = findViewById(R.id.imageView);
        getToolBar();
        Intent intent =  getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        TraiCay traicay = (TraiCay) bundle.getSerializable("goi");
        String name1 = traicay.getTen();
        String mota1 = traicay.getMoTa();
        String hinhanh1 = traicay.getHinh();
        anh.setImageBitmap(getBitmap(hinhanh1));
        mota.setText(mota1);
        name.setText(name1);
    }

    private void getToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Product detail");
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private Bitmap getBitmap(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}