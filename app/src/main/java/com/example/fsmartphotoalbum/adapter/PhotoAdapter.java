package com.example.fsmartphotoalbum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fsmartphotoalbum.R;
import com.example.fsmartphotoalbum.entity.Photo;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private Context mContext;
    private List<Photo> mPhotoList;

    private OnClickListener mListener;

    public interface OnClickListener {
        void onClickItem(Photo photo, int position);
    }

    public PhotoAdapter(Context mContext, List<Photo> mPhotoList) {
        this.mContext = mContext;
        this.mPhotoList = mPhotoList;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mListener = onClickListener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_photo, null));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, final int position) {
        final Photo photo = mPhotoList.get(position);
        Glide.with(mContext).load(photo.getUri()).transition(withCrossFade()).into(holder.photo);
        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickItem(photo, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.iv_item_photo_image);
        }
    }
}
