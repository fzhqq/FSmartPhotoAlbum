package com.example.fsmartphotoalbum.test;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.fsmartphotoalbum.R;
import com.example.fsmartphotoalbum.entity.Album;
import com.example.fsmartphotoalbum.entity.Photo;
import com.example.fsmartphotoalbum.util.PictureUtil;
import com.example.fsmartphotoalbum.util.StringUtil;

import java.io.File;
import java.io.IOException;

import static com.example.fsmartphotoalbum.constants.Constants.REQUEST_CHOOSE_PHOTO_FROM_ALBUM;
import static com.example.fsmartphotoalbum.constants.Constants.REQUEST_WRITE_EXTERNAL_STORAGE;

public class TestActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mChoosePhoneBtn;
    private Button mGetAlbumBtn;
    private ImageView mImageIv;


    private static final int PREVIEW_IMAGE_WIDTH = 640;
    private static final int PREVIEW_IMAGE_HEIGHT = 480;
    private static final int TF_INPUT_IMAGE_WIDTH = 224;
    private static final int TF_INPUT_IMAGE_HEIGHT = 224;


    public Album album;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        checkPermission();
        initData();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void initData() {


        album = new Album();
    }

    private void initView() {
        mChoosePhoneBtn = findViewById(R.id.btn_test_choose_photo_from_album);
        mChoosePhoneBtn.setOnClickListener(this);
        mGetAlbumBtn = findViewById(R.id.btn_test_get_local_album);
        mGetAlbumBtn.setOnClickListener(this);

        mImageIv = findViewById(R.id.iv_test_choose_image);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_test_choose_photo_from_album:
                chooseFromAlbum();
                break;
            case R.id.btn_test_get_local_album:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getLocalAlbum(TestActivity.this);
                    }
                }).start();
                break;
            default:
                break;
        }
    }

    /**
     * 检查权限
     */
    private void checkPermission() {
        //如果没有WRITE_EXTERNAL_STORAGE权限，则需要动态申请权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }


    /**
     * 从相册中选择图片
     */
    private void chooseFromAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_PHOTO_FROM_ALBUM) {
            String imagePath;
            if (resultCode == RESULT_OK && data != null) {
                imagePath = PictureUtil.handleImage(data, this);
                Bitmap bitmap = PictureUtil.getBitmapFromPath(this, imagePath);
                mImageIv.setImageBitmap(bitmap);


//                final Collection<Recognition> results = mTensorFlowClassifier.doRecognize(newBitmap);
//
//                Log.d("fzh", "Got the following results from Tensorflow: " + results);
            }
        }
    }

    /**
     * 得到本地相册列表
     *
     * 耗时操作，需在子线程进行
     */
    private void getLocalAlbum(Context context) {
        final Uri contentUri = MediaStore.Files.getContentUri("external");
        final String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC";
        final String selection =
                "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?" + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)" + " AND " + MediaStore.MediaColumns.SIZE + ">0";
        String[] selectionAllArgs =
                {String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                        String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)};

        ContentResolver contentResolver = context.getContentResolver();
        String[] projections;
        projections = new String[]{MediaStore.Files.FileColumns._ID,
                MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.DATE_MODIFIED, MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.MediaColumns.WIDTH, MediaStore.MediaColumns.HEIGHT,
                MediaStore.MediaColumns.SIZE, MediaStore.Video.Media.DURATION};

        Cursor cursor = contentResolver.query(contentUri, projections, selection,
                selectionAllArgs, sortOrder);
        if (cursor == null) {
//            Log.d(TAG, "call: " + "Empty photos");
        } else if (cursor.moveToFirst()) {
//            String albumItem_all_name = getAllAlbumName(context);
//            String albumItem_video_name =
//                    context.getString(R.string.selector_folder_video_easy_photos);
            int idCol = cursor.getColumnIndex(MediaStore.MediaColumns._ID);
            int pathCol = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
            int nameCol = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
            int DateCol = cursor.getColumnIndex(MediaStore.MediaColumns.DATE_MODIFIED);
            int mimeType = cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE);
            int sizeCol = cursor.getColumnIndex(MediaStore.MediaColumns.SIZE);
            int durationCol = cursor.getColumnIndex(MediaStore.Video.Media.DURATION);
            int WidthCol = cursor.getColumnIndex(MediaStore.MediaColumns.WIDTH);
            int HeightCol = cursor.getColumnIndex(MediaStore.MediaColumns.HEIGHT);

            int num = 1;

            do {
                String id = cursor.getString(idCol);
                String path = cursor.getString(pathCol);
                String name = cursor.getString(nameCol);
                long dateTime = cursor.getLong(DateCol);
                String type = cursor.getString(mimeType);
                long size = cursor.getLong(sizeCol);
                long duration = cursor.getLong(durationCol);
                int width = 0;
                int height = 0;

                Log.i("fzh", "PhotoTest, num = " + num +
                        ", id = " + id +
                        ", path = " + path +
                        ", name = " + name +
                        ", dateTime = " + dateTime +
                        ", type = " + type +
                        ", size = " + size +
                        ", duration = " + duration);
                num++;

                if (TextUtils.isEmpty(path) || TextUtils.isEmpty(type)) {
                    continue;
                }

//                boolean isVideo = type.contains(Type.VIDEO);// 是否是视频
                Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

//                if (Setting.isOnlyVideo() && !isVideo) {
//                    continue;
//                }
//                if (!Setting.filterTypes.isEmpty() && !Setting.isFilter(type)) {
//                    continue;
//                }
//
//                if (!Setting.showGif) {
//                    if (path.endsWith(Type.GIF) || type.endsWith(Type.GIF)) {
//                        continue;
//                    }
//                }
//                if (!Setting.showVideo) {
//                    if (isVideo) {
//                        continue;
//                    }
//                }
//
//                if (size < Setting.minSize) {
//                    continue;
//                }
//                if (isVideo && (duration <= Setting.videoMinSecond || duration >= Setting.videoMaxSecond)) {
//                    continue;
//                }
//                if (!isVideo) {
//                    width = cursor.getInt(WidthCol);
//                    height = cursor.getInt(HeightCol);
//                    if (width>0 && height>0){
//                        if (width < Setting.minWidth || height < Setting.minHeight) {
//                            continue;
//                        }
//                    }
//
//                }

                File file = new File(path);
                if (!file.exists() || !file.isFile()) {
                    continue;
                }

                Photo photo = new Photo(id, name, uri, path, dateTime, width, height, size, type);
//                if (!Setting.selectedPhotos.isEmpty()) {
//                    for (Photo selectedPhoto : Setting.selectedPhotos) {
//                        if (path.equals(selectedPhoto.path)) {
//                            photo.selectedOriginal = Setting.selectedOriginal;
//                            Result.addPhoto(photo);
//                        }
//                    }
//                }

                // 初始化“全部”专辑
                if (album.isEmpty()) {
                    // 用第一个图片作为专辑的封面
                    album.addAlbumItem("全部图片", path,uri);
                }
                // 把图片全部放进“全部”专辑
                album.getAlbumItem("全部图片").addImageItem(photo);

//                if (Setting.showVideo && isVideo && !albumItem_video_name.equals(albumItem_all_name)) {
//                    album.addAlbumItem(albumItem_video_name, "", path,uri);
//                    album.getAlbumItem(albumItem_video_name).addImageItem(photo);
//                }

                // 添加当前图片的专辑到专辑模型实体中
                File parentFile = new File(path).getParentFile();
                if (null == parentFile) {
                    continue;
                }
                String folderPath = parentFile.getAbsolutePath();
                String albumName = StringUtil.getLastPathSegment(folderPath);
                album.addAlbumItem(albumName, path,uri);
                album.getAlbumItem(albumName).addImageItem(photo);
            } while (cursor.moveToNext());
            cursor.close();

            Log.i("fzh", "end");
        }
    }
}
