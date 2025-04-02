package com.example.fsmartphotoalbum.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.fsmartphotoalbum.adapter.AlbumAdapter;
import com.example.fsmartphotoalbum.entity.Album;
import com.example.fsmartphotoalbum.entity.AlbumItem;
import com.example.fsmartphotoalbum.eventbus.Event;
import com.example.fsmartphotoalbum.eventbus.EventBusCode;
import com.example.fsmartphotoalbum.eventbus.EventBusUtil;
import com.example.fsmartphotoalbum.eventbus.event.AlbumFragmentEvent;
import com.example.fsmartphotoalbum.eventbus.event.SmartPhotoEvent;
import com.example.fsmartphotoalbum.model.AlbumModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AlbumFragment extends Fragment {

    private RecyclerView mAlbumRv;

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

        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_album, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
        initView();
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AlbumModel.getInstance().updateAlbum(getActivity());

                Event<SmartPhotoEvent> smartPhotoEvent = new Event<>(EventBusCode.updateAllPhotos, new SmartPhotoEvent(true));
                EventBusUtil.sendEvent(smartPhotoEvent);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        updateAdapter();
                    }
                });
            }
        }).start();
    }

    private void initView() {
        mAlbumRv = getActivity().findViewById(R.id.rv_album_album);
        mAlbumRv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
    }

    private void updateAdapter() {
        Album album = AlbumModel.getInstance().getAlbum();
        AlbumAdapter albumAdapter = new AlbumAdapter(getActivity(), album.albumItems);
        albumAdapter.setOnClickListener(new AlbumAdapter.OnClickListener() {
            @Override
            public void onClickItem(AlbumItem albumItem) {
                Intent intent = new Intent(getActivity(), AlbumItemActivity.class);
                intent.putExtra(AlbumItemActivity.KEY_ALBUM_NAME, albumItem.name);
                startActivity(intent);
            }
        });
        mAlbumRv.setAdapter(albumAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event<AlbumFragmentEvent> event) {
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
