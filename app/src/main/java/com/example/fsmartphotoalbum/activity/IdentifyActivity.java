package com.example.fsmartphotoalbum.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fsmartphotoalbum.R;
import com.example.fsmartphotoalbum.adapter.IdentifyAdapter;
import com.example.fsmartphotoalbum.entity.Photo;
import com.example.fsmartphotoalbum.tensorflow.Classifier;
import com.example.fsmartphotoalbum.util.IdentifyUtil;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class IdentifyActivity extends AppCompatActivity {

    public static final String KEY_IDENTIFY_PHOTO = "key_identify_photo";

    private ImageView mPhotoIv;
    private RecyclerView mResultRv;

    private Photo mPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify);
        setTitle("识别结果");

        initData();
        initView();
        identify();
    }

    private void initData() {
        mPhoto = getIntent().getParcelableExtra(KEY_IDENTIFY_PHOTO);
    }

    private void initView() {
        mPhotoIv = findViewById(R.id.iv_identify_image);
        Glide.with(this).load(mPhoto.getUri()).transition(withCrossFade()).into(mPhotoIv);

        mResultRv = findViewById(R.id.rv_identify_list);
        mResultRv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void identify() {
        List<Classifier.Recognition> results = IdentifyUtil.identify(mPhoto);

        List<String> mContentList = new ArrayList<>();
        for (Classifier.Recognition recognition : results) {
            String res = "图片类别：" + recognition.getTitle() + "\n"
                    + "自信程度：" + recognition.getConfidence();
            mContentList.add(res);
        }
        IdentifyAdapter adapter = new IdentifyAdapter(this, mContentList);
        mResultRv.setAdapter(adapter);
    }
}
