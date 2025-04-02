package com.example.fsmartphotoalbum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fsmartphotoalbum.R;

import java.util.List;

public class IdentifyAdapter extends RecyclerView.Adapter<IdentifyAdapter.IdentifyViewHolder> {

    private Context mContext;
    private List<String> mContentList;

    public IdentifyAdapter(Context mContext, List<String> mContentList) {
        this.mContext = mContext;
        this.mContentList = mContentList;
    }

    @NonNull
    @Override
    public IdentifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IdentifyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_identify, null));
    }

    @Override
    public void onBindViewHolder(@NonNull IdentifyViewHolder holder, int position) {
        holder.content.setText(mContentList.get(position));
    }

    @Override
    public int getItemCount() {
        return mContentList.size();
    }

    static class IdentifyViewHolder extends RecyclerView.ViewHolder {

        TextView content;

        public IdentifyViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.tv_item_identify_content);
        }
    }
}
