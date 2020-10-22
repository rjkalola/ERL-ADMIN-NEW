package com.app.erladmin.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import com.app.erladmin.BuildConfig;
import com.app.imagepicker.Model.FileWithPath;

public class ImagePickerUtility {
    private Activity a;
    private int requestCode;
    private String mCurrentPhotoPath;

    public ImagePickerUtility(Activity a) {
        this.a = a;
    }

    public void pickImage(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        a.startActivityForResult(Intent.createChooser(intent, "Select File"), requestCode);
    }

    @SuppressLint("ObsoleteSdkInt")
    public void openCamera(int requestCode) {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                a.startActivityForResult(takePictureIntent, requestCode);
            } else {
                FileWithPath fileWithPath = AppUtils.createImageFile("", AppConstant.Type.CAMERA, AppConstant.FileExtension.JPG);
                mCurrentPhotoPath = fileWithPath.getFilePath();
                if (fileWithPath.getFile() != null) {
                    Uri photoURI = FileProvider.getUriForFile(a,
                            BuildConfig.APPLICATION_ID + ".provider",
                            fileWithPath.getFile());
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    a.startActivityForResult(takePictureIntent, requestCode);
                }
            }
        } catch (Exception e) {

        }
    }

    public void startCrop(Uri sourceUri, int ratioX, int ratioY, String outputExtension) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.IntentKey.IMAGE_URI, sourceUri.toString());
        bundle.putInt(AppConstant.IntentKey.CROP_RATIO_X, ratioX);
        bundle.putInt(AppConstant.IntentKey.CROP_RATIO_Y, ratioY);
        bundle.putString(AppConstant.IntentKey.FILE_EXTENSION, outputExtension);
//        ((BaseActivity) a).moveActivityForResult(a, CropImageActivity.class, false, false, AppConstant.IntentKey.REQUEST_CROP_IMAGE, bundle);
    }

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

}
