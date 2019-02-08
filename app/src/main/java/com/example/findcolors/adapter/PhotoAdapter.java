package com.example.findcolors.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.androidnetworking.widget.ANImageView;
import com.example.findcolors.R;
import com.example.findcolors.util.Config;
import com.example.findcolors.util.PhotosModel;

import java.util.ArrayList;
import java.util.List;

/*
    - استخدام مكتبة Fast Android Networking Library
   لتحميل الصور من الويب سيرفس إلي التطبيق
     */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.Photo> {
    List<PhotosModel> data;
    private PhotoClickListener listener;
    public interface PhotoClickListener{
        void onClick(int pos);
    }

    public PhotoAdapter(PhotoClickListener photoClickListener) {
        data = new ArrayList<>();
        listener = photoClickListener;
    }

    @NonNull
    @Override
    public Photo onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_item,viewGroup,false);
        return new Photo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Photo photo, int i) {
        photo.photo.setDefaultImageResId(R.drawable.logo);
        photo.photo.setDefaultImageResId(R.drawable.logo);
        photo.photo.setImageUrl(Config.IMAGE_PATH + data.get(i).getPhoto_name());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class Photo extends RecyclerView.ViewHolder implements View.OnClickListener {
        ANImageView photo;
        public Photo(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.imageView);
            Button show = itemView.findViewById(R.id.show);

            show.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(getAdapterPosition());
        }
    }

    public void loadItems(List<PhotosModel> list){
        this.data = list;
        notifyDataSetChanged();
    }
}
