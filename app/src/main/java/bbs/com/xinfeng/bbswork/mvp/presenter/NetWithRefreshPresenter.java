package bbs.com.xinfeng.bbswork.mvp.presenter;


import bbs.com.xinfeng.bbswork.base.IBaseModel;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;

/**
 * Created by dell on 2017/10/17.
 */
public class NetWithRefreshPresenter implements NetContract.INetPresenter, NetContract.OnDataLoadingListener {
    private NetContract.INetView netView;

    public NetContract.IRefreshAndLoadMoreView refreshView;

    public NetWithRefreshPresenter(NetContract.INetView netView, NetContract.IRefreshAndLoadMoreView refreshView) {
        this.netView = netView;
        this.refreshView = refreshView;
    }

    public IBaseModel netModel;

    @Override
    public void destory() {
        hideLoading(0);
        if (netModel != null)
            netModel.cancelAll();
    }


    @Override
    public void startLoading(int tag) {
        netView.showLoading(tag);
    }

    @Override
    public void hideLoading(int tag) {
        netView.hideLoading(tag);
    }

    @Override
    public void onSuccess(ErrorBean o, int tag, boolean isNetWork) {
        netView.setData(o, tag);
        hideLoading(0);
    }

    @Override
    public void onError(ErrorBean errorBean, int tag) {
        netView.showError(errorBean, tag);
        hideLoading(0);
    }

    @Override
    public void onProgress(int percent, int tag, boolean isDone) {
        netView.progress(percent, tag);
    }
}
