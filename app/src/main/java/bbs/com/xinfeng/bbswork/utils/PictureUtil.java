package bbs.com.xinfeng.bbswork.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;

import bbs.com.xinfeng.bbswork.base.Constant;

/**
 * Created by dell on 2018/4/4.
 */

public class PictureUtil {
    public static final int SELECT_PHOTO = 1;
    public static final int TAKE_PHOTO = 2;
    private Uri imageUri;

    public void selectPic(Activity activity) {
        CharSequence[] items = {"相册", "相机"};
        new AlertDialog.Builder(activity)
                .setTitle("选择图片来源")
                .setItems(items, (dialog, which) -> {
                    if (which == 0) {
//                        Intent intent = new Intent("android.intent.action.GET_CONTENT");
//                        intent.setType("image/*");
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_PICK);
                        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        activity.startActivityForResult(intent, SELECT_PHOTO);
                    } else {
                        new RxPermissions(activity).request(Manifest.permission.CAMERA).subscribe(aBoolean -> {
                            if (aBoolean) {
                                //创建File对象，用于存储拍照后的图片
                                File outputImage = new File(activity.getExternalCacheDir(), "output_image.jpg");
                                try {
                                    if (outputImage.exists()) {
                                        outputImage.delete();
                                    }
                                    outputImage.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (Build.VERSION.SDK_INT >= 24) {
                                    imageUri = FileProvider.getUriForFile(activity, Constant.AUTHORITY, outputImage);
                                } else {
                                    imageUri = Uri.fromFile(outputImage);
                                }
                                //启动相机程序
                                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                activity.startActivityForResult(intent, TAKE_PHOTO);
                            } else {
                                ToastUtil.showToast("您拒绝了使用相机的权限,请开启后再使用此功能");
                            }
                        });
                    }
                })
                .create().show();
    }

}
