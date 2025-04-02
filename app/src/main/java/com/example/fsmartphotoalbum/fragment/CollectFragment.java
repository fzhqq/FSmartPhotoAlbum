package com.example.fsmartphotoalbum.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fsmartphotoalbum.R;
import com.example.fsmartphotoalbum.activity.AlbumItemActivity;
import com.example.fsmartphotoalbum.activity.PhotoActivity;
import com.example.fsmartphotoalbum.adapter.PhotoAdapter;
import com.example.fsmartphotoalbum.db.DatabaseManager;
import com.example.fsmartphotoalbum.entity.Photo;
import com.example.fsmartphotoalbum.eventbus.Event;
import com.example.fsmartphotoalbum.eventbus.EventBusCode;
import com.example.fsmartphotoalbum.eventbus.EventBusUtil;
import com.example.fsmartphotoalbum.eventbus.event.AlbumFragmentEvent;
import com.example.fsmartphotoalbum.eventbus.event.CollectEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class CollectFragment extends Fragment {

    private static final String NAME = "我的收藏";
    private static final int NUM = 3;       // 每行展示的图片数

    private RecyclerView mCollectRv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBusUtil.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBusUtil.unregister(this);
    }

    @Nullable
    @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {

        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_collect, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    private void initView() {
        mCollectRv = getActivity().findViewById(R.id.rv_collect_list);
        mCollectRv.setLayoutManager(new GridLayoutManager(getActivity(), NUM));
        updateAdapter();
    }

    private void updateAdapter() {
        List<Photo> photoList = DatabaseManager.getInstance().queryAllCollect();
        PhotoAdapter photoAdapter = new PhotoAdapter(getActivity(), photoList);
        photoAdapter.setOnClickListener(new PhotoAdapter.OnClickListener() {
            @Override
            public void onClickItem(Photo photo, int position) {
                Intent intent = new Intent(getActivity(), PhotoActivity.class);
                intent.putExtra(PhotoActivity.KEY_ALBUM_ITEM_NAME, NAME);
                intent.putExtra(PhotoActivity.KEY_PHOTO_INDEX, position);
                intent.putExtra(PhotoActivity.KEY_FROM, 1);
                startActivity(intent);
            }
        });
        mCollectRv.setAdapter(photoAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event<CollectEvent> event) {
        switch (event.getCode()) {
            case EventBusCode.updateCollect:
                updateAdapter();
                break;
            default:
                break;
        }
    }
}
