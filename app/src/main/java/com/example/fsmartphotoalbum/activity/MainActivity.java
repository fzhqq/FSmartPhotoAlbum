package com.example.fsmartphotoalbum.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.fsmartphotoalbum.R;
import com.example.fsmartphotoalbum.adapter.MainPageAdapter;
import com.example.fsmartphotoalbum.fragment.AlbumFragment;
import com.example.fsmartphotoalbum.fragment.CollectFragment;
import com.example.fsmartphotoalbum.fragment.SmartPhotoFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SD = 1;

    private TabLayout mMainTv;
    private ViewPager mMainVp;

    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mTitleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("智能相册");

        checkPermission();
    }

    private void initData() {
        mTitleList.add("我的相册");
        mTitleList.add("我的收藏");
        mTitleList.add("智能分类");

        mFragmentList.add(new AlbumFragment());
        mFragmentList.add(new CollectFragment());
        mFragmentList.add(new SmartPhotoFragment());
    }

    private void initView() {
        mMainVp = findViewById(R.id.vp_main_view_pager);
        MainPageAdapter mainPageAdapter = new MainPageAdapter(getSupportFragmentManager(),
                mFragmentList, mTitleList);
        mMainVp.setAdapter(mainPageAdapter);
        mMainVp.setOffscreenPageLimit(2);

        mMainTv = findViewById(R.id.tv_main_tab);
        mMainTv.setupWithViewPager(mMainVp);
    }

    /**
     * 检查权限
     */
    private void checkPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_SD);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_SD:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initData();
                    initView();
                    break;
                }
                // 用户不同意
                finish();
                break;
            default:
                break;
        }
    }

}
