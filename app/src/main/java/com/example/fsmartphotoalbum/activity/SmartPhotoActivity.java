package com.example.fsmartphotoalbum.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.fsmartphotoalbum.R;
import com.example.fsmartphotoalbum.adapter.PhotoAdapter;
import com.example.fsmartphotoalbum.entity.Photo;
import com.example.fsmartphotoalbum.entity.SmartPhotoItem;
import com.example.fsmartphotoalbum.model.AlbumModel;

import java.util.List;

public class SmartPhotoActivity extends AppCompatActivity {

    public static final String KEY_SMART_PHOTO_ITEM_TITLE = "key_smart_photo_item_title";
    private static final int NUM = 3;       // 每行展示的图片数

    private RecyclerView mListRv;

    private SmartPhotoItem mSmartPhotoItem;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_photo);

        initData();
        initView();
        updateAdapter();
    }

    private void initData() {
        mTitle = getIntent().getStringExtra(KEY_SMART_PHOTO_ITEM_TITLE);
        if (!TextUtils.isEmpty(mTitle)) {
            setTitle(mTitle);
        }
        mSmartPhotoItem = AlbumModel.getInstance().getSmartPhotoItem(mTitle);
    }

    private void initView() {
        mListRv = findViewById(R.id.rv_smart_photo_list);
        mListRv.setLayoutManager(new GridLayoutManager(this, NUM));
    }

    private void updateAdapter() {
        if (mSmartPhotoItem == null) {
            return;
        }

        List<Photo> photoList = mSmartPhotoItem.getPhotoList();
        PhotoAdapter photoAdapter = new PhotoAdapter(this, photoList);
        photoAdapter.setOnClickListener(new PhotoAdapter.OnClickListener() {
            @Override
            public void onClickItem(Photo photo, int position) {
                Intent intent = new Intent(SmartPhotoActivity.this, PhotoActivity.class);
                intent.putExtra(PhotoActivity.KEY_ALBUM_ITEM_NAME, mSmartPhotoItem.getTitle());
                intent.putExtra(PhotoActivity.KEY_PHOTO_INDEX, position);
                intent.putExtra(PhotoActivity.KEY_FROM, 2);
                intent.putExtra(KEY_SMART_PHOTO_ITEM_TITLE, mTitle);
                startActivity(intent);
            }
        });
        mListRv.setAdapter(photoAdapter);
    }
}
