package com.example.fsmartphotoalbum.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fsmartphotoalbum.R;
import com.example.fsmartphotoalbum.entity.SmartPhotoItem;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class SmartPhotoAdapter extends RecyclerView.Adapter<SmartPhotoAdapter.SmartAlbumViewHolder> {

    private Context mContext;
    private List<SmartPhotoItem> mSmartPhotoList;

    private OnClickListener mListener;

    public interface OnClickListener {
        void onClickItem(SmartPhotoItem smartPhotoItem);
    }

    public SmartPhotoAdapter(Context mContext, List<SmartPhotoItem> mSmartPhotoList) {
        this.mContext = mContext;
        this.mSmartPhotoList = mSmartPhotoList;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mListener = onClickListener;
    }

    @NonNull
    @Override
    public SmartAlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SmartAlbumViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_album, null));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SmartAlbumViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        final SmartPhotoItem smartPhotoItem = mSmartPhotoList.get(position);

        Glide.with(mContext).load(smartPhotoItem.getCoverImageUri()).transition(withCrossFade()).into(holder.headImage);
        holder.headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickItem(smartPhotoItem);
            }
        });

        holder.name.setText(smartPhotoItem.getTitle());
        holder.num.setText(String.valueOf(smartPhotoItem.getPhotoList().size()));
    }

    @Override
    public int getItemCount() {
        return mSmartPhotoList.size();
    }

    static class SmartAlbumViewHolder extends RecyclerView.ViewHolder {
        ImageView headImage;
        TextView name;
        TextView num;

        public SmartAlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            headImage = itemView.findViewById(R.id.iv_item_album_headImage);
            name = itemView.findViewById(R.id.tv_item_album_name);
            num = itemView.findViewById(R.id.tv_item_album_num);
        }
    }
}
