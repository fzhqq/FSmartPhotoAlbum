package com.example.fsmartphotoalbum.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.fsmartphotoalbum.R;
import com.example.fsmartphotoalbum.adapter.MonthAdapter;
import com.example.fsmartphotoalbum.entity.Album;
import com.example.fsmartphotoalbum.entity.AlbumItem;
import com.example.fsmartphotoalbum.entity.Month;
import com.example.fsmartphotoalbum.entity.Photo;
import com.example.fsmartphotoalbum.eventbus.Event;
import com.example.fsmartphotoalbum.eventbus.EventBusCode;
import com.example.fsmartphotoalbum.eventbus.EventBusUtil;
import com.example.fsmartphotoalbum.eventbus.event.AlbumItemEvent;
import com.example.fsmartphotoalbum.eventbus.event.PhotoEvent;
import com.example.fsmartphotoalbum.model.AlbumModel;
import com.example.fsmartphotoalbum.util.StatusBarUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class AlbumItemActivity extends AppCompatActivity {

    public static final String KEY_ALBUM_NAME = "key_album_name";

    private RecyclerView mListRv;

    private Album mAlbum;
    private MonthAdapter mMonthAdapter;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_item);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        EventBusUtil.register(this);

        initData();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        mName = getIntent().getStringExtra(KEY_ALBUM_NAME);
        setTitle(mName);
        mAlbum = AlbumModel.getInstance().getAlbum();
    }

    private void initView() {
        mListRv = findViewById(R.id.rv_album_item_list);
        mListRv.setLayoutManager(new LinearLayoutManager(this));
        updateAdapter();
    }

    private void updateAdapter() {
        AlbumItem albumItem = mAlbum.getAlbumItem(mName);
        List<Month> monthList = new ArrayList<>();
        if (albumItem != null) {
            for (String title : albumItem.photoMap.keySet()) {
                Month month = new Month(title, albumItem.photoMap.get(title));
                monthList.add(month);
            }
        }
        mMonthAdapter = new MonthAdapter(this, monthList);
        mMonthAdapter.setOnClickListener(new MonthAdapter.OnClickListener() {
            @Override
            public void onClickItem(Photo photo, int position) {
                Intent intent = new Intent(AlbumItemActivity.this, PhotoActivity.class);
                intent.putExtra(PhotoActivity.KEY_ALBUM_ITEM_NAME, mName);
                intent.putExtra(PhotoActivity.KEY_PHOTO_INDEX, position);
                startActivity(intent);
            }
        });
        mListRv.setAdapter(mMonthAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event<AlbumItemEvent> event) {
        switch (event.getCode()) {
            case EventBusCode.updateList:
                boolean update = event.getData().isUpdateList();
                if (update) {
                    updateAdapter();
                }
                break;
            default:
                break;
        }
    }
}
