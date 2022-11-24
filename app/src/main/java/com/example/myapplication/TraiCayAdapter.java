package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TraiCayAdapter extends BaseAdapter {
    private MainActivity context;
    private int layout;
    private List<TraiCay> traiCayList;

    public TraiCayAdapter(MainActivity context, int layout,List<TraiCay> traiCayList) {
        this.context = context;
        this.layout = layout;
        this.traiCayList = traiCayList;
    }

    @Override
    public int getCount() {
        return traiCayList.size();
    }

    @Override
    public Object getItem(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class hoderView{
        TextView ten,mota;
        ImageView anh;
        ImageButton xoa,update;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        hoderView hoder;
    if(convertView==null) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(layout, null);
        hoder= new hoderView();
        hoder.xoa = convertView.findViewById(R.id.xoa);
        hoder.ten = (TextView) convertView.findViewById(R.id.textviewten);
        hoder.mota = (TextView) convertView.findViewById(R.id.textviewmota);
        hoder.anh = (ImageView) convertView.findViewById(R.id.imagview);
        hoder.update = convertView.findViewById(R.id.update);
        convertView.setTag(hoder);
    }
    else{
        hoder = (hoderView) convertView.getTag();
    }
        TraiCay traiCay =traiCayList.get(i);
        hoder.ten.setText(traiCay.getTen());
        hoder.mota.setText(traiCay.getMoTa());
        hoder.anh.setImageBitmap(getBitmap(traiCay.getHinh()));
        hoder.anh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.getContent(i);
            }
        });

        hoder.xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.remove(i);
            }
        });

        hoder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.update(i);
            }
        });

        Animation animation = AnimationUtils.loadAnimation(context,R.anim.anim_scale);
        convertView.startAnimation(animation);
        return convertView;
    }
    private Bitmap getBitmap(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}
