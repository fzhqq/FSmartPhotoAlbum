package com.example.fsmartphotoalbum.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.RecoverableSecurityException;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.fsmartphotoalbum.R;
import com.example.fsmartphotoalbum.adapter.PhotoPageAdapter;
import com.example.fsmartphotoalbum.db.DatabaseManager;
import com.example.fsmartphotoalbum.entity.Album;
import com.example.fsmartphotoalbum.entity.AlbumItem;
import com.example.fsmartphotoalbum.entity.Photo;
import com.example.fsmartphotoalbum.entity.SmartPhotoItem;
import com.example.fsmartphotoalbum.eventbus.Event;
import com.example.fsmartphotoalbum.eventbus.EventBusCode;
import com.example.fsmartphotoalbum.eventbus.EventBusUtil;
import com.example.fsmartphotoalbum.eventbus.event.AlbumFragmentEvent;
import com.example.fsmartphotoalbum.eventbus.event.AlbumItemEvent;
import com.example.fsmartphotoalbum.eventbus.event.CollectEvent;
import com.example.fsmartphotoalbum.eventbus.event.PhotoEvent;
import com.example.fsmartphotoalbum.fragment.PhotoFragment;
import com.example.fsmartphotoalbum.model.AlbumModel;
import com.example.fsmartphotoalbum.util.StatusBarUtil;
import com.example.fsmartphotoalbum.util.StringUtil;
import com.example.fsmartphotoalbum.widget.TipDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String KEY_ALBUM_ITEM_NAME = "key_album_item_name";
    public static final String KEY_PHOTO_INDEX = "key_photo_index";
    public static final String KEY_FROM = "key_from";   // 0：正常 1：收藏 2：智能分类
    public static final int REQUEST_DELETE_PERMISSION = 0;

    private ViewPager mPhotoVp;
    private LinearLayout mIdentifyLv;
    private LinearLayout mShareLv;
    private RelativeLayout mCollectRv;
    private LinearLayout mDeleteLv;
    private LinearLayout mDetailLv;
    private LinearLayout mTopBarLv;
    private LinearLayout mBottomMenuLv;
    private ImageView mBackIv;
    private TextView mTitleTv;
    private ImageView mCollectIv;

    private String mAlbumItemName;
    private int mPhotoIndex;
    private int mFrom;

    private List<Photo> mPhotoList = new ArrayList<>();
    private List<Fragment> mPhotoFragmentList = new ArrayList<>();
    private PhotoPageAdapter mPhotoPageAdapter;
    private Photo mPendingPhoto;
    private SmartPhotoItem mSmartPhotoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //隐藏标题栏
        setContentView(R.layout.activity_photo);
        StatusBarUtil.setLightColorStatusBar(this);
        getWindow().setStatusBarColor(Color.WHITE);

        EventBusUtil.register(this);

        initData();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    private void initData() {
        mAlbumItemName = getIntent().getStringExtra(KEY_ALBUM_ITEM_NAME);
        mPhotoIndex = getIntent().getIntExtra(KEY_PHOTO_INDEX, 0);
        mFrom = getIntent().getIntExtra(KEY_FROM, 0);

        List<Photo> photoList = new ArrayList<>();
        if (mFrom == 1) {
            photoList = DatabaseManager.getInstance().queryAllCollect();
        } else if (mFrom == 2) {
            String title = getIntent().getStringExtra(SmartPhotoActivity.KEY_SMART_PHOTO_ITEM_TITLE);
            mSmartPhotoItem = AlbumModel.getInstance().getSmartPhotoItem(title);
            if (mSmartPhotoItem != null) {
                photoList = mSmartPhotoItem.getPhotoList();
            }
        } else {
            Album album = AlbumModel.getInstance().getAlbum();
            AlbumItem albumItem = album.getAlbumItem(mAlbumItemName);
            photoList = albumItem.getPhotoList();
        }

        for (Photo photo : photoList) {
            mPhotoFragmentList.add(PhotoFragment.newInstance(photo));
        }
    }

    private Photo getCurPhoto() {
        List<Photo> photoList = new ArrayList<>();
        if (mFrom == 1) {
            photoList = DatabaseManager.getInstance().queryAllCollect();
        } else if (mFrom == 2) {
            if (mSmartPhotoItem != null) {
                photoList = mSmartPhotoItem.getPhotoList();
            }
        } else {
            Album album = AlbumModel.getInstance().getAlbum();
            AlbumItem albumItem = album.getAlbumItem(mAlbumItemName);
            photoList = albumItem.getPhotoList();
        }
        return photoList.get(mPhotoIndex);
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        mPhotoVp = findViewById(R.id.vp_photo_view_pager);
        mPhotoPageAdapter = new PhotoPageAdapter(getSupportFragmentManager(), mPhotoFragmentList);
        mPhotoVp.setAdapter(mPhotoPageAdapter);
        mPhotoVp.setCurrentItem(mPhotoIndex);
        mPhotoVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPhotoIndex = position;
                updateTitle();
                updateCollectIv();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mIdentifyLv = findViewById(R.id.lv_photo_menu_identify);
        mIdentifyLv.setOnClickListener(this);
        mShareLv = findViewById(R.id.lv_photo_menu_share);
        mShareLv.setOnClickListener(this);
        mCollectRv = findViewById(R.id.rv_photo_menu_collect);
        mCollectRv.setOnClickListener(this);
        mDeleteLv = findViewById(R.id.lv_photo_menu_delete);
        mDeleteLv.setOnClickListener(this);
        mDetailLv = findViewById(R.id.lv_photo_menu_detail);
        mDetailLv.setOnClickListener(this);

        mTopBarLv = findViewById(R.id.lv_photo_top_bar);
        mBottomMenuLv = findViewById(R.id.lv_photo_menu);

        mBackIv = findViewById(R.id.iv_photo_back);
        mBackIv.setOnClickListener(this);

        mTitleTv = findViewById(R.id.tv_photo_title);
        updateTitle();

        mCollectIv = findViewById(R.id.iv_photo_collect);
        mCollectIv.setOnClickListener(this);
        updateCollectIv();
    }

    @SuppressLint("SetTextI18n")
    private void updateTitle() {
        mTitleTv.setText((mPhotoIndex + 1) + "/" + mPhotoFragmentList.size());
    }

    private void updateCollectIv() {
        Photo photo = getCurPhoto();
        if (DatabaseManager.getInstance().isExistInCollect(photo.getId())) {
            mCollectIv.setSelected(true);
        } else {
            mCollectIv.setSelected(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event<PhotoEvent> event) {
        switch (event.getCode()) {
            case EventBusCode.fragment2Photo:
                boolean isVisible = event.getData().isVisible();    // 是否显示顶部/底部 栏
                if (!isVisible) {
                    mTopBarLv.setVisibility(View.GONE);
                    mBottomMenuLv.setVisibility(View.GONE);
                    StatusBarUtil.setDarkColorStatusBar(this);
                    getWindow().setStatusBarColor(Color.BLACK);
                } else {
                    mTopBarLv.setVisibility(View.VISIBLE);
                    mBottomMenuLv.setVisibility(View.VISIBLE);
                    StatusBarUtil.setLightColorStatusBar(this);
                    getWindow().setStatusBarColor(Color.WHITE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_photo_back:
                finish();
                break;
            case R.id.lv_photo_menu_identify:
                Intent identifyIntent = new Intent(PhotoActivity.this, IdentifyActivity.class);
                identifyIntent.putExtra(IdentifyActivity.KEY_IDENTIFY_PHOTO, getCurPhoto());
                startActivity(identifyIntent);
                break;
            case R.id.lv_photo_menu_share:
                share();
                break;
            case R.id.iv_photo_collect:
            case R.id.rv_photo_menu_collect:
                final Photo photo = getCurPhoto();
                if (!mCollectIv.isSelected()) {
                    // 收藏图片
                    mCollectIv.setSelected(true);
                    DatabaseManager.getInstance().insertCollect(photo);
                    Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
                    updateCollect();
                } else {
                    // 取消收藏
                    new TipDialog.Builder(this)
                            .setContent("确定要取消收藏该图片？")
                            .setCancel("不了")
                            .setEnsure("确定")
                            .setOnClickListener(new TipDialog.OnClickListener() {
                                @Override
                                public void clickEnsure() {
                                    if (mFrom == 1) {
                                        mPhotoFragmentList.remove(mPhotoIndex);
                                        mPhotoPageAdapter.notifyDataSetChanged();
                                        updateTitle();
                                        DatabaseManager.getInstance().deleteCollect(photo.getId());
                                        Toast.makeText(PhotoActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                                        updateList();
                                    } else {
                                        mCollectIv.setSelected(false);
                                        DatabaseManager.getInstance().deleteCollect(photo.getId());
                                        Toast.makeText(PhotoActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                                    }
                                    updateCollect();
                                }

                                @Override
                                public void clickCancel() {

                                }
                            })
                            .build()
                            .show();
                }
                break;
            case R.id.lv_photo_menu_delete:
                delete();
                break;
            case R.id.lv_photo_menu_detail:
                showDetail();
                break;
            default:
                break;
        }
    }

    private void share() {
        Photo photo = getCurPhoto();
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("image/*");  //设置分享内容的类型
        share_intent.putExtra(Intent.EXTRA_STREAM, photo.getUri());
        //创建分享的Dialog
        share_intent = Intent.createChooser(share_intent, "share");
        startActivity(share_intent);
    }

    private void showDetail() {
        Photo photo = getCurPhoto();
        StringBuilder sb = new StringBuilder();
        sb.append("文件名：").append(photo.getName()).append("\n").append("\n");
        sb.append("文件路径：").append(photo.getPath()).append("\n").append("\n");
        sb.append("拍摄时间：").append(StringUtil.milliseconds2String(photo.getTime() * 1000)).append("\n").append("\n");
        sb.append("文件大小：").append(StringUtil.byte2String(photo.getSize())).append("\n").append("\n");
        sb.append("尺寸：").append(photo.getWidth()).append("x").append(photo.getHeight()).append("px");

        new AlertDialog.Builder(this)
                .setMessage(sb.toString())
                .show();
    }

    private void updateCollect() {
        Event<CollectEvent> collectEvent = new Event<>(EventBusCode.updateCollect, new CollectEvent(true));
        EventBusUtil.sendEvent(collectEvent);
    }

    private void delete() {
        new TipDialog.Builder(this)
                .setContent("确定要删除该图片？")
                .setCancel("不了")
                .setEnsure("确定")
                .setOnClickListener(new TipDialog.OnClickListener() {
                    @Override
                    public void clickEnsure() {
                        // 删除图片
                        Photo photo = getCurPhoto();
                        if (photo != null) {
                            deletePhoto(photo);
                        }
                    }

                    @Override
                    public void clickCancel() {

                    }
                })
                .build()
                .show();
    }

    private void deletePhoto(Photo photo) {
        try {
            int res = getContentResolver().delete(photo.getUri(), null, null);
            if (res > 0) {
                File file = new File(photo.getPath());
                file.delete();
            }
            afterDeletePhoto(photo);
        } catch (Throwable t) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && t instanceof RecoverableSecurityException) {
                mPendingPhoto = photo;

                RecoverableSecurityException recoverableSecurityException = (RecoverableSecurityException) t;

                IntentSender intentSender = recoverableSecurityException.getUserAction().getActionIntent().getIntentSender();
                try {
                    startIntentSenderForResult(intentSender, REQUEST_DELETE_PERMISSION, null, 0, 0, 0, null);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void afterDeletePhoto(Photo photo) {
        mPhotoFragmentList.remove(mPhotoIndex);
        mPhotoPageAdapter.notifyDataSetChanged();
        updateTitle();

        AlbumModel.getInstance().delete(mAlbumItemName, photo.getPath());

        // 从收藏数据库删除
        DatabaseManager.getInstance().deleteCollect(photo.getId());
        updateCollect();
    }

    private void updateList() {
        Event<AlbumItemEvent> albumItemEvent = new Event<>(EventBusCode.updateList, new AlbumItemEvent(true));
        EventBusUtil.sendEvent(albumItemEvent);

        Event<AlbumFragmentEvent> albumFragmentEvent = new Event<>(EventBusCode.updateList, new AlbumFragmentEvent(true));
        EventBusUtil.sendEvent(albumFragmentEvent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK &&
                requestCode == REQUEST_DELETE_PERMISSION) {
            deletePhoto(mPendingPhoto);
        }
    }
}
