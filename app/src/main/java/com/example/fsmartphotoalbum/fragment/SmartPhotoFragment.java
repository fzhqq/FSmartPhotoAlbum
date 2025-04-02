package com.example.fsmartphotoalbum.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fsmartphotoalbum.R;
import com.example.fsmartphotoalbum.activity.SmartPhotoActivity;
import com.example.fsmartphotoalbum.adapter.SmartPhotoAdapter;
import com.example.fsmartphotoalbum.db.DatabaseManager;
import com.example.fsmartphotoalbum.entity.Photo;
import com.example.fsmartphotoalbum.entity.SmartPhotoItem;
import com.example.fsmartphotoalbum.model.AlbumModel;
import com.example.fsmartphotoalbum.tensorflow.Classifier;
import com.example.fsmartphotoalbum.util.IdentifyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmartPhotoFragment extends Fragment {

    private RecyclerView mAlbumRv;
    private ProgressBar mProgressBar;
    private TextView mProgressTv;

    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {

        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_smart_photo, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        request();
    }

    private void request() {
        Log.i("fzh", "request smart photo");

        new Thread(new Runnable() {
            @Override
            public void run() {
                AlbumModel.getInstance().updateAllPhotos(getActivity());

                List<Photo> allPhotos = AlbumModel.getInstance().getAllPhotos();

                Map<String, String> dbSmartPhotoMap = DatabaseManager.getInstance().queryAllSmart();

                Log.i("fzh", "fzhTest, start");
                // 拿到全部图片后，依次对所有图片进行识别
                Map<String, SmartPhotoItem> smartPhotoItemMap = new HashMap<>();
                long startTime = System.currentTimeMillis();
//                int size = Math.min(500, allPhotos.size());
                int size = allPhotos.size();
                for (int i = 0; i < size; i++) {
                    Photo photo = allPhotos.get(i);
                    if (photo == null) {
                        continue;
                    }
                    String title;
                    if (dbSmartPhotoMap.containsKey(photo.getId())) {
                        title = dbSmartPhotoMap.get(photo.getId());
                    } else {
                        List<Classifier.Recognition> results = IdentifyUtil.identify(photo);

                        if (results.isEmpty()) {
                            continue;
                        }

                        title = results.get(0).getTitle(); // 识别结果的title
                        DatabaseManager.getInstance().insertSmart(photo.getId(), title);
                    }

                    if (smartPhotoItemMap.containsKey(title)) {
                        SmartPhotoItem item = smartPhotoItemMap.get(title);
                        item.addPhoto(photo);
                    } else {
                        SmartPhotoItem item = new SmartPhotoItem(title, photo.getPath(), photo.getUri());
                        item.addPhoto(photo);
                        smartPhotoItemMap.put(title, item);
                    }

                    int finalI = i;
                    mMainHandler.post(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            mProgressTv.setText("识别图片中：" + (finalI +1) + "/" + size);
                        }
                    });
                }

                List<SmartPhotoItem> items = new ArrayList<>(smartPhotoItemMap.values());
                for (SmartPhotoItem smartPhotoItem : items) {
                    AlbumModel.getInstance().addSmartPhotoItem(smartPhotoItem);
                }

                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressTv.setVisibility(View.GONE);
                        mProgressBar.setVisibility(View.GONE);
                        updateAdapter(items);
                    }
                });

                long endTime = System.currentTimeMillis();
                long time = endTime - startTime;
                Log.i("fzh", "fzhTest, time = " + time);

            }
        }).start();
    }

    private void initView() {
        mAlbumRv = getActivity().findViewById(R.id.rv_fragment_smart_photo_album);
        mAlbumRv.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        mProgressBar = getActivity().findViewById(R.id.pb_fragment_smart_photo);
        mProgressTv = getActivity().findViewById(R.id.tv_fragment_smart_photo_progress);
    }

    private void updateAdapter(List<SmartPhotoItem> smartPhotoList) {
        SmartPhotoAdapter smartPhotoAdapter = new SmartPhotoAdapter(getActivity(), smartPhotoList);
        smartPhotoAdapter.setOnClickListener(new SmartPhotoAdapter.OnClickListener() {
            @Override
            public void onClickItem(SmartPhotoItem smartPhotoItem) {
                Intent intent = new Intent(getActivity(), SmartPhotoActivity.class);
                intent.putExtra(SmartPhotoActivity.KEY_SMART_PHOTO_ITEM_TITLE, smartPhotoItem.getTitle());
                startActivity(intent);
            }
        });
        mAlbumRv.setAdapter(smartPhotoAdapter);
    }
}
