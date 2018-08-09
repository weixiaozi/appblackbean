package bbs.com.xinfeng.bbswork.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.view.View;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.databinding.UpdateDialogLayoutBinding;
import bbs.com.xinfeng.bbswork.domin.Progress;
import bbs.com.xinfeng.bbswork.domin.UpgradeBean;
import bbs.com.xinfeng.bbswork.ui.service.UpdateApkService;

import static bbs.com.xinfeng.bbswork.base.Constant.AUTHORITY;

/**
 * Created by dell on 2018/5/8.
 */

public class UpgradeUtilBackup {
    public static final int where_main = 1;
    public static final int where_setting = 2;

    private boolean isForce;
    private AlertDialog mDialog;
    private UpdateDialogLayoutBinding mBinding;
    private Activity context;
    private UpgradeBean bean;
    private int where;

    public UpgradeUtilBackup(Activity context) {
        this.context = context;
    }

    public void checkUpgrade(UpgradeBean bean, int where) {
        this.where = where;
        if (Integer.parseInt(bean.getVersion()) > SystemUtil.getAppVersion()) {
            isForce = bean.isUpgrade();
            this.bean = bean;
            showUpdataDialog(context, bean);
        }
    }

    private void showUpdataDialog(Activity context, UpgradeBean bean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.update_dialog_layout, null);
        mBinding = DataBindingUtil.bind(view);
        mBinding.txtPointContent.setText(bean.desc);
        if (isForce) {
            mBinding.txtButtonRight.setVisibility(View.VISIBLE);
            mBinding.txtButtonLeft.setVisibility(View.GONE);
        } else {
            mBinding.txtButtonRight.setVisibility(View.VISIBLE);
            mBinding.txtButtonLeft.setVisibility(View.VISIBLE);
            mBinding.txtButtonLeft.setOnClickListener(v -> {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            });
        }
        mBinding.txtButtonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDown(context, bean);
            }
        });
        builder.setCancelable(false);
        builder.setView(view);
        mDialog = builder.create();
        if (!mDialog.isShowing())
            mDialog.show();
    }

    private void startDown(Activity context, UpgradeBean bean) {
        new RxPermissions(context).request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(aBoolean -> {
            if (aBoolean) {
                updateNormal(context, bean);
            } else {
                ToastUtil.showToast("请开启存储权限!");
                if (isForce)
                    context.finish();
            }
        });
    }

    private void updateNormal(Activity context, UpgradeBean versionBean) {
        if (!installApk(context, versionBean)) {
            Intent intent = new Intent(context, UpdateApkService.class);
            intent.putExtra("downurl", versionBean.getUrl());
            intent.putExtra("isFocusUpdate", isForce);
            intent.putExtra("where", where);
            context.startService(intent);

            if (mBinding != null && isForce) {
                mBinding.txtButtonRight.setVisibility(View.GONE);
                mBinding.pbUpdate.setVisibility(View.VISIBLE);
            } else {
                mDialog.dismiss();
            }
        }
    }

    public boolean installApk(Activity context, UpgradeBean bean) {
        boolean isHas = false;
        File file = new File(Constant.STORAGE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] files = file.listFiles();
        if (files == null) {
            return isHas;
        }
        for (File f : files) {
            if (f.getName().contains(".apk")) {
                PackageManager packageManager1 = context.getPackageManager();
                PackageInfo packageInfo1 = packageManager1.getPackageArchiveInfo(f.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
                if (packageInfo1 != null && context.getPackageName().equals(packageInfo1.packageName)) {
                    if (packageInfo1.versionCode >= Integer.valueOf(bean.getVersion())) {
                        isHas = true;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Uri contentUri = FileProvider.getUriForFile(context, AUTHORITY, f);
                            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                            //兼容8.0
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                                if (!hasInstallPermission) {
                                    ToastUtil.showToast("请允许安装未知来源");
                                    startInstallPermissionSettingActivity();
                                    return isHas;
                                }
                            }
                        } else {
                            intent.setDataAndType(Uri.fromFile(f), "application/vnd.android.package-archive");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
                        if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                            context.startActivity(intent);
                        }

                        mDialog.dismiss();
                        return isHas;
                    } else {
                        f.delete();
                    }
                }
            }
        }
        return isHas;
    }

    /**
     * 跳转到设置-允许安装未知来源-页面
     */
    private void startInstallPermissionSettingActivity() {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void updataProgress(Progress p) {
        if (mBinding != null) {
            if (p.isError) {
                mBinding.txtButtonRight.setVisibility(View.VISIBLE);
                mBinding.txtButtonRight.setText("立即重试");
            }

            mBinding.pbUpdate.setProgress(p.progress);
            if (p.progress == 100) {
                mBinding.txtButtonRight.setVisibility(View.VISIBLE);
                mBinding.txtButtonRight.setText("立即安装");
                mBinding.txtButtonRight.setOnClickListener(v -> {
                    installApk(context, bean);
                });
            }
        }
        if (mDialog != null && !mDialog.isShowing())
            mDialog.show();
    }
}
