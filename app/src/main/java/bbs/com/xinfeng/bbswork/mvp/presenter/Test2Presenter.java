package bbs.com.xinfeng.bbswork.mvp.presenter;

import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.model.Test2Model;
import bbs.com.xinfeng.bbswork.utils.LoadMorePageUtil;

import static bbs.com.xinfeng.bbswork.utils.LoadMorePageUtil.NO_DATA;
import static bbs.com.xinfeng.bbswork.utils.LoadMorePageUtil.NO_MORE_DATA;

/**
 * Created by dell on 2017/11/23.
 */

public class Test2Presenter extends NetWithRefreshPresenter {
    private Test2Model mModel;
    private LoadMorePageUtil collectPageUtil;

    public Test2Presenter(NetContract.INetView netView, NetContract.IRefreshAndLoadMoreView refreshVeiw) {
        super(netView, refreshVeiw);
        netModel = mModel = new Test2Model(this, netView.provideActivity());
    }

    public void getCollectMoredata(int more) {
        if (collectPageUtil == null)
            collectPageUtil = new LoadMorePageUtil();
        int loadMorePage = collectPageUtil.getLoadMorePage();
        if (loadMorePage == NO_DATA) {
            refreshView.noData();
        } else if (loadMorePage == NO_MORE_DATA) {
            refreshView.noMoreData();
        } else {
            mModel.getCollectData(collectPageUtil.getSingleSize(), loadMorePage + "", more);
        }
    }

    public void getCollectRefreshdata(int normal) {
        if (collectPageUtil == null)
            collectPageUtil = new LoadMorePageUtil();
        mModel.getCollectData(collectPageUtil.getSingleSize(), collectPageUtil.getRefreshPage(), normal);
    }

    public void resetPageTotal(String pageTotal, String currentPage) {
        if (collectPageUtil != null) {
            collectPageUtil.setPageTotal(Integer.parseInt(pageTotal));
            collectPageUtil.setCurrentPage(Integer.parseInt(currentPage));

        }
    }
}
