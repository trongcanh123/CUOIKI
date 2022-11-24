package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ListView listView;
    Button btprofile, btcontent;
    ViewFlipper flipper;
    ArrayList<ItemMenu> arrayList;
    MenuAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        btcontent= findViewById(R.id.btcontent);
        btprofile= findViewById(R.id.btprofile);
        flipper = findViewById(R.id.flip);
        anhxa();
        actionViewFlipper();
        actiontoolbar();
        acctionMenu();
        btcontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        getItemIntent();
    }

    private void getItemIntent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Intent intent =new Intent(HomeActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
                if(position==1){
                    Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                if (position==2){
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void acctionMenu() {
        arrayList= new ArrayList<>();
        arrayList.add(new ItemMenu("Profile", R.drawable.ic_baseline_person));
        arrayList.add(new ItemMenu("Content ", R.drawable.ic_baseline_content_paste_24));
        arrayList.add(new ItemMenu("Sign out ", R.drawable.ic_baseline_login));
        adapter= new MenuAdapter(this, R.layout.dong_item,arrayList);
        listView.setAdapter(adapter);
    }

    private void actiontoolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    private void anhxa() {
        toolbar=findViewById(R.id.toolbar);
        drawerLayout= findViewById(R.id.drawlayout);
        navigationView= findViewById(R.id.nagavationview);
        listView=findViewById(R.id.lv);
    }

    private void actionViewFlipper() {
        List<String> quangCao = new ArrayList<>();
        quangCao.add("https://anhdep123.com/wp-content/uploads/2020/11/hinh-anh-trai-sau-rieng.jpg");
        quangCao.add("https://cdn01.dienmaycholon.vn/filewebdmclnew/public/picture/experience/article_3614.jpg");
        quangCao.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSTRNwO03eLpt5ZnW2bukpZC-gFj14MSO2mxg&usqp=CAU");
        for(int i=0;i<quangCao.size();i++) {
            ImageView anh = new ImageView(HomeActivity.this);
            Picasso.get().load(quangCao.get(i)).into(anh);
            anh.setScaleType(ImageView.ScaleType.FIT_XY);
            flipper.addView(anh);
        }
        flipper.setFlipInterval(3000);
        flipper.setAutoStart(true);
        Animation animationIn = AnimationUtils.loadAnimation(HomeActivity.this,R.anim.slide_in_right);
        Animation animationout = AnimationUtils.loadAnimation(HomeActivity.this,R.anim.slide_out_right);
        flipper.setInAnimation(animationIn);
        flipper.setOutAnimation(animationout);
    }
}