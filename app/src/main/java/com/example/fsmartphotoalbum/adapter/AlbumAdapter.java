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
import com.example.fsmartphotoalbum.entity.AlbumItem;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private Context mContext;
    private List<AlbumItem> mAlbumItemList;

    private OnClickListener mListener;

    public interface OnClickListener {
        void onClickItem(AlbumItem albumItem);
    }

    public AlbumAdapter(Context mContext, List<AlbumItem> mAlbumItemList) {
        this.mContext = mContext;
        this.mAlbumItemList = mAlbumItemList;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mListener = onClickListener;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlbumViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_album, null));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        final AlbumItem albumItem = mAlbumItemList.get(position);

        Glide.with(mContext).load(albumItem.coverImageUri).transition(withCrossFade()).into(holder.headImage);
        holder.headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickItem(albumItem);
            }
        });

        holder.name.setText(albumItem.name);
        holder.num.setText(String.valueOf(albumItem.getNum()));
    }

    @Override
    public int getItemCount() {
        return mAlbumItemList.size();
    }

    static class AlbumViewHolder extends RecyclerView.ViewHolder {
        ImageView headImage;
        TextView name;
        TextView num;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            headImage = itemView.findViewById(R.id.iv_item_album_headImage);
            name = itemView.findViewById(R.id.tv_item_album_name);
            num = itemView.findViewById(R.id.tv_item_album_num);
        }
    }
}
