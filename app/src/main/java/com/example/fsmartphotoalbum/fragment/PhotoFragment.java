package com.example.fsmartphotoalbum.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.example.fsmartphotoalbum.R;
import com.example.fsmartphotoalbum.entity.Photo;
import com.example.fsmartphotoalbum.eventbus.Event;
import com.example.fsmartphotoalbum.eventbus.EventBusCode;
import com.example.fsmartphotoalbum.eventbus.EventBusUtil;
import com.example.fsmartphotoalbum.eventbus.event.PhotoEvent;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PhotoFragment extends Fragment {

    private static final String KEY_PHOTO = "key_photo";

    private PhotoView mPhotoIv;

    private Photo mPhoto;
    private boolean mIsVisible = true;

    /**
     * 返回碎片实例
     */
    public static PhotoFragment newInstance(Photo photo) {
        PhotoFragment fragment = new PhotoFragment();
        //动态加载fragment，接受activity传入的值
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_PHOTO, photo);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getArguments() != null) {
            mPhoto = getArguments().getParcelable(KEY_PHOTO);
        }
    }

    @Nullable
    @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_photo, null);
        initView(view);
        return view;
    }

    private void initView(View root) {
        mPhotoIv = root.findViewById(R.id.iv_fragment_photo_photo);
        mPhotoIv.enable();

        mPhotoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsVisible = !mIsVisible;
                sendEvent(mIsVisible);
            }
        });

        Glide.with(getContext()).load(mPhoto.getUri()).transition(withCrossFade()).into(mPhotoIv);
    }

    private void sendEvent(boolean isVisible) {
        Event<PhotoEvent> photoEvent = new Event<>(EventBusCode.fragment2Photo, new PhotoEvent(isVisible));
        EventBusUtil.sendEvent(photoEvent);
    }

}
