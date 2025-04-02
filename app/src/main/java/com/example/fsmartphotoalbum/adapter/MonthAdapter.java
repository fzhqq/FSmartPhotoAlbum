package com.example.fsmartphotoalbum.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fsmartphotoalbum.R;
import com.example.fsmartphotoalbum.activity.PhotoActivity;
import com.example.fsmartphotoalbum.entity.Month;
import com.example.fsmartphotoalbum.entity.Photo;

import java.util.List;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MonthViewHolder> {

    private Context mContext;
    private List<Month> mMonthList;

    private static final int ITEM_COUNT = 4;        // 每行显示的图片数

    private OnClickListener mListener;

    public interface OnClickListener {
        void onClickItem(Photo photo, int position);
    }

    public MonthAdapter(Context mContext, List<Month> mMonthList) {
        this.mContext = mContext;
        this.mMonthList = mMonthList;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mListener = onClickListener;
    }

    @NonNull
    @Override
    public MonthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MonthViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_month, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MonthViewHolder holder, int position) {
        Month month = mMonthList.get(position);
        holder.title.setText(month.getTitle());

        final int oldPos = position;

        holder.photoList.setLayoutManager(new GridLayoutManager(mContext, ITEM_COUNT));
        PhotoAdapter photoAdapter = new PhotoAdapter(mContext, month.getPhotoList());
        photoAdapter.setOnClickListener(new PhotoAdapter.OnClickListener() {
            @Override
            public void onClickItem(Photo photo, int position) {
                int curPos = 0;
                for (int i = 0; i < oldPos; i++) {
                    curPos += mMonthList.get(i).getPhotoList().size();
                }
                mListener.onClickItem(photo, curPos + position);
            }
        });
        holder.photoList.setAdapter(photoAdapter);
    }

    @Override
    public int getItemCount() {
        return mMonthList.size();
    }

    static class MonthViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RecyclerView photoList;

        public MonthViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_item_month_title);
            photoList = itemView.findViewById(R.id.rv_item_month_image_list);
        }
    }
}
