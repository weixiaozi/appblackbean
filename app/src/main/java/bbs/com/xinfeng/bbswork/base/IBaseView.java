package bbs.com.xinfeng.bbswork.base;

import android.app.Activity;

import bbs.com.xinfeng.bbswork.domin.ErrorBean;

/**
 * Created by dell on 2017/10/17.
 */

public interface IBaseView {

    void showLoading(int tag);

    void hideLoading(int tag);

    void showError(ErrorBean errorBean, int tag);

    /**
     * 返回Activity
     *
     * @return
     */
    Activity provideActivity();
}
