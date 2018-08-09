package bbs.com.xinfeng.bbswork.utils;

import android.widget.Toast;

import bbs.com.xinfeng.bbswork.base.App;


/**
 * 吐司工具
 * Created by sh
 */
public class ToastUtil {
    private static Toast toast;

    public static void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(App.getInstance(), text, Toast.LENGTH_SHORT);
        } else {
            //如果toast不为空，则直接更改当前toast的文本
            App.getInstance().mHandler.post(() -> {
                try {
                    toast.setText(text);
                } catch (Exception e) {
                }
            });
        }
        toast.show();
    }
}
