package bbs.com.xinfeng.bbswork.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseFragment;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.databinding.FragmentMessageBinding;
import bbs.com.xinfeng.bbswork.databinding.FragmentMessageHeadBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.domin.MesReduceBus;
import bbs.com.xinfeng.bbswork.domin.MessageFragmentBean;
import bbs.com.xinfeng.bbswork.domin.MessageFragmentInnerBean;
import bbs.com.xinfeng.bbswork.domin.MessageFragmentInnerBean_;
import bbs.com.xinfeng.bbswork.domin.MessageNumObjectBox;
import bbs.com.xinfeng.bbswork.domin.MessageNumObjectBox_;
import bbs.com.xinfeng.bbswork.domin.NavigatorBus;
import bbs.com.xinfeng.bbswork.domin.NoticeChangeBean;
import bbs.com.xinfeng.bbswork.domin.PrivateChatItemBean;
import bbs.com.xinfeng.bbswork.domin.PrivateMesChangedBean;
import bbs.com.xinfeng.bbswork.domin.SendToServerIsSuccessBean;
import bbs.com.xinfeng.bbswork.domin.SetMesStatusBean;
import bbs.com.xinfeng.bbswork.domin.SocketStatusBus;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.MessagePresenter;
import bbs.com.xinfeng.bbswork.ui.activity.ClickMeActivity;
import bbs.com.xinfeng.bbswork.ui.activity.ContactsActivity;
import bbs.com.xinfeng.bbswork.ui.activity.NotificationActivity;
import bbs.com.xinfeng.bbswork.ui.activity.PrivateChatActivity;
import bbs.com.xinfeng.bbswork.ui.activity.ReplyMeActivity;
import bbs.com.xinfeng.bbswork.ui.activity.SystemNotifyActivity;
import bbs.com.xinfeng.bbswork.ui.adapter.MessageFragmentAdapter;
import bbs.com.xinfeng.bbswork.utils.SharedPrefUtil;
import bbs.com.xinfeng.bbswork.utils.ToastUtil;
import bbs.com.xinfeng.bbswork.widget.popwindow.MessageFragmentPoppubWindow;
import io.objectbox.Box;
import io.objectbox.query.Query;

import static bbs.com.xinfeng.bbswork.mvp.model.MessageModel.SOCKET_MES_TAG;
import static bbs.com.xinfeng.bbswork.ui.activity.MainActivity.Second_unread_news;

/**
 * Created by dell on 2017/10/24.
 */

public class MessageFragment extends BaseFragment<FragmentMessageBinding, MessagePresenter> implements NetContract.INetView {
    private final int SetStatus_tag = 100;
    private Box<MessageNumObjectBox> box;
    private Box<MessageFragmentInnerBean> boxMes;
    private MessageNumObjectBox mesNumObject;
    private List<MessageFragmentInnerBean> mDatas = Collections.synchronizedList(new ArrayList<>());
    private MessageFragmentAdapter mAdapter;
    private FragmentMessageHeadBinding headBinding;
    private String globalCurrent;
    public static int ResumeChatId;//进入某个私聊时，不更新该私聊消息
    private MessageFragmentPoppubWindow mesPop;
    private HashMap<Integer, Integer> unreadMap = new HashMap();
    private int authorId;
    private boolean isHttp;
    private Query<MessageFragmentInnerBean> boxQueryMes;
    private Query<MessageNumObjectBox> boxQuery;

    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {

    }

    @Override
    public void showError(ErrorBean errorBean, int tag) {
        if (errorBean.androidType.equals(ErrorBean.TYPE_SHOW)) {
            ToastUtil.showToast(errorBean.desc);
        }
        if (tag == SOCKET_MES_TAG) {
            isHttp = false;
            if (errorBean instanceof MessageFragmentBean) {
                if (TextUtils.equals(errorBean.androidcode, "160001")) {
                    globalSychronizeList();
                }
            }
            mBinding.swipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void setData(ErrorBean errorBean, int tag) {
        switch (tag) {
            case SOCKET_MES_TAG:
                getActivity().runOnUiThread(() -> {
                    if (errorBean instanceof SendToServerIsSuccessBean) {
                        SendToServerIsSuccessBean bean = (SendToServerIsSuccessBean) errorBean;
                        if (bean.getA() == 8) {
                            if (bean.getStatus() != 1) {
                                mBinding.swipeLayout.setRefreshing(false);
                            }
                        }
                    }
                });
                if (errorBean instanceof MessageFragmentBean) {
                    isHttp = false;
                    MessageFragmentBean bean = (MessageFragmentBean) errorBean;
                    globalCurrent = bean.getData().getGlobal();
                    SharedPrefUtil.putString(Constant.mes_current_key, globalCurrent);
                    if (bean.getData().getTypeX() == 1) {
                        for (MessageFragmentInnerBean info : bean.getData().getList()) {
                            if (info.getId() != ResumeChatId && info.getLast_uid() != authorId) {
                                if (unreadMap.get(info.getId()) != null)
                                    info.setN(info.getN() + unreadMap.get(info.getId()));
                                unreadMap.put(info.getId(), info.getN());
                            } else {
                                info.setN(0);
                                unreadMap.put(info.getId(), 0);
                            }

                            /*for (MessageFragmentInnerBean newData : mDatas) {
                                if (info.getId() == newData.getId()) {
                                    info.setImg(newData.getImg());
                                    info.setTid(newData.getTid());
                                    info.setLabel(newData.getLabel());
                                    info.setType(newData.getType());
                                    if (info.getId() != ResumeChatId) {
                                        info.setN(newData.getN() + info.getN());
                                    } else {
                                        info.setN(0);
                                    }
                                    break;
                                }
                            }*/
                        }
                        mDatas.clear();
                        mDatas.addAll(bean.getData().getList());
                        mAdapter.notifyDataSetChanged();
                    } else if (bean.getData().getTypeX() == 2) {
                        if (bean.getData().getList().size() > 0) {
                            for (MessageFragmentInnerBean info : bean.getData().getList()) {
                                if (info.getId() != ResumeChatId && info.getLast_uid() != authorId) {
                                    if (unreadMap.get(info.getId()) != null)
                                        info.setN(info.getN() + unreadMap.get(info.getId()));
                                    unreadMap.put(info.getId(), info.getN());
                                } else {
                                    info.setN(0);
                                    unreadMap.put(info.getId(), 0);
                                }

                                for (MessageFragmentInnerBean newData : mDatas) {
                                    if (info.getId() == newData.getId()) {
                                        /*info.setImg(newData.getImg());
                                        info.setTid(newData.getTid());
                                        info.setLabel(newData.getLabel());
                                        info.setType(newData.getType());
                                        if (info.getId() != ResumeChatId) {
                                            info.setN(newData.getN() + info.getN());
                                        } else {
                                            info.setN(0);
                                        }*/
                                        mDatas.remove(newData);
                                        break;
                                    }
                                }
                            }
                            mDatas.addAll(0, bean.getData().getList());
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        mDatas.clear();
                        mDatas.addAll(0, bean.getData().getList());
                        mAdapter.notifyDataSetChanged();
                    }
                    mBinding.swipeLayout.setRefreshing(false);
                    updateUnreadMes();
                }

                if (errorBean instanceof PrivateMesChangedBean) {
                    PrivateMesChangedBean bean = (PrivateMesChangedBean) errorBean;
                    if (bean.getT() != 2 && !TextUtils.isEmpty(globalCurrent)) {
                        if (!isHttp)
                            sychronizeList(globalCurrent, "", System.currentTimeMillis());
                    } else {
                        globalSychronizeList();
                    }
                }
                break;
            case SetStatus_tag:
                SetMesStatusBean bean = (SetMesStatusBean) errorBean;
                for (int i = 0; i < mDatas.size(); i++) {
                    if (bean.getSession_id() == mDatas.get(i).getId()) {
                        unreadMap.remove(bean.getSession_id());
                        mAdapter.remove(i);
                        updateUnreadMes();
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void progress(int precent, int tag) {

    }

    @Override
    protected MessagePresenter creatPresenter() {
        return new MessagePresenter(this);
    }

    @Override
    protected void initEvent() {
        headBinding.llayoutReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesNumObject.setReplyNum(0);
                notifyMesNum(mesNumObject);
                startActivity(new Intent(provideActivity(), ReplyMeActivity.class));
            }
        });
        headBinding.llayoutClickme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesNumObject.setClikNum(0);
                notifyMesNum(mesNumObject);
                startActivity(new Intent(provideActivity(), ClickMeActivity.class));
            }
        });
        headBinding.llayoutNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesNumObject.setNotifyNum(0);
                notifyMesNum(mesNumObject);
                startActivity(new Intent(provideActivity(), NotificationActivity.class));
            }
        });
       /* headBinding.llayoutOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesNumObject.setOperateNum(0);
                notifyMesNum(mesNumObject);
                Intent intent = new Intent(provideActivity(), SystemNotifyActivity.class);
                intent.putExtra("noticetype", 2);
                startActivity(intent);
            }
        });*/
        headBinding.llayoutSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesNumObject.setSystemNotifyNum(0);
                notifyMesNum(mesNumObject);
                Intent intent = new Intent(provideActivity(), SystemNotifyActivity.class);
                intent.putExtra("noticetype", 1);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.fragment_message;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.basebar.barTxtTitle.setText(R.string.message_title);
        mBinding.basebar.barRightPic.setImageResource(R.drawable.icon_mes_contacts);
        mBinding.basebar.barRightClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(provideActivity(), ContactsActivity.class));
            }
        });
        if (App.getInstance().socketStatus != 2)
            mBinding.txtChatSocketview.setVisibility(View.VISIBLE);
        authorId = SharedPrefUtil.getInt(Constant.userid_key, 0);
        initBoxMesList();
        initAdapter();
        addHeader();
        initBox();
        globalCurrent = SharedPrefUtil.getString(Constant.mes_current_key, "");
    }

    private void initBoxMesList() {
        boxMes = App.mApp.getmBoxStore().boxFor(MessageFragmentInnerBean.class);
        boxQueryMes = boxMes.query().equal(MessageFragmentInnerBean_.uid_android, authorId).build();
        List<MessageFragmentInnerBean> all = boxQueryMes.find();
        if (all != null && all.size() > 0) {
            for (MessageFragmentInnerBean innerBean : all) {
                if (innerBean.getN() > 0) {
                    unreadMap.put(innerBean.getId(), innerBean.getN());
                }
            }
            mDatas.addAll(all);
        }
    }

    private void initBox() {
        box = App.mApp.getmBoxStore().boxFor(MessageNumObjectBox.class);
        boxQuery = box.query().equal(MessageNumObjectBox_.user_id, authorId).build();
        List<MessageNumObjectBox> allMes = boxQuery.find();
        if (allMes != null && allMes.size() > 0) {
            mesNumObject = allMes.get(allMes.size() - 1);
        } else {
            mesNumObject = new MessageNumObjectBox();
        }
        mesNumObject.setOperateNum(0);
        notifyMesNum(mesNumObject);
    }

    private void initAdapter() {
        mAdapter = new MessageFragmentAdapter(mDatas);
        mBinding.recycleMessagelist.setLayoutManager(new LinearLayoutManager(provideActivity(), LinearLayoutManager.VERTICAL, false));
        mBinding.recycleMessagelist.setAdapter(mAdapter);
        mBinding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                App.getInstance().resetReTry();
            }
        });
        mAdapter.setmListen(new MessageFragmentAdapter.onItemOperateListen() {
            @Override
            public void onClickItem(int position) {
                ResumeChatId = mDatas.get(position).getId();
                unreadMap.put(ResumeChatId, 0);
                mDatas.get(position).setN(0);
                mAdapter.refreshNotifyItemChanged(position);
                Intent intent = new Intent(provideActivity(), PrivateChatActivity.class);
                intent.putExtra("userid", mDatas.get(position).getTid());
                intent.putExtra("username", mDatas.get(position).getLabel());
                intent.putExtra("chatid", mDatas.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onLongClickItem(int postion) {
                if (mesPop == null)
                    mesPop = new MessageFragmentPoppubWindow(provideActivity());
                mesPop.setDismissListener(new MessageFragmentPoppubWindow.DismissListener() {
                    @Override
                    public void dismiss() {

                    }

                    @Override
                    public void onDel() {
                        mPresenter.setStatus(mDatas.get(postion).getId(), 3, SetStatus_tag);
                    }

                    @Override
                    public void onReport() {

                    }

                    @Override
                    public void onBlacklist() {

                    }
                });
                mesPop.show();
            }
        });
    }

    private void refresh() {
        if (App.getInstance().socketStatus == 2) {
            if (mDatas.size() == 0) {
                sychronizeList("", "", System.currentTimeMillis());
            } else {
                globalSychronizeList();
          /*  if (!TextUtils.isEmpty(globalCurrent))
                sychronizeList(globalCurrent, "", System.currentTimeMillis());
            else {
                globalSychronizeList();
            }*/
            }
        } else {
            isHttp = false;
            mBinding.swipeLayout.setRefreshing(false);
        }
    }

    private void globalSychronizeList() {
        StringBuffer stringBuffer = new StringBuffer();
        for (MessageFragmentInnerBean info : mDatas) {
            stringBuffer.append(info.getId());
            stringBuffer.append(":");
            stringBuffer.append(info.getCurrent());
            stringBuffer.append(",");
        }
        if (stringBuffer.length() > 0)
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        sychronizeList("", stringBuffer.toString(), System.currentTimeMillis());
    }

    private void addHeader() {
        View headView = getLayoutInflater().inflate(R.layout.fragment_message_head, (ViewGroup) mBinding.recycleMessagelist.getParent(), false);
        headBinding = DataBindingUtil.bind(headView);
        mAdapter.addHeaderView(headView);
    }

    private synchronized void sychronizeList(String gi, String ci, long s) {
        isHttp = true;
        mPresenter.sychronizeList(gi, ci, s);
    }

    private void notifyMesNum(MessageNumObjectBox mesObject) {
        headBinding.txtReplyMes.setVisibility(mesObject.getReplyNum() == 0 ? View.GONE : View.VISIBLE);
        headBinding.txtClickmeMes.setVisibility(mesObject.getClikNum() == 0 ? View.GONE : View.VISIBLE);
        headBinding.txtNoticeMes.setVisibility(mesObject.getNotifyNum() == 0 ? View.GONE : View.VISIBLE);
//        headBinding.txtOperateMes.setVisibility(mesObject.getOperateNum() == 0 ? View.GONE : View.VISIBLE);
        headBinding.txtSystemMes.setVisibility(mesObject.getSystemNotifyNum() == 0 ? View.GONE : View.VISIBLE);

        headBinding.txtReplyMes.setText(mesObject.getReplyNum() + "");
        headBinding.txtClickmeMes.setText(mesObject.getClikNum() + "");
        headBinding.txtNoticeMes.setText(mesObject.getNotifyNum() + "");
//        headBinding.txtOperateMes.setText(mesObject.getOperateNum() + "");
        headBinding.txtSystemMes.setText(mesObject.getSystemNotifyNum() + "");

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NoticeChangeBean.DataBean e) {
        if (e.getTypeX() >= 11 && e.getTypeX() <= 15) {
            switch (e.getTypeX()) {
                case 11:
                    mesNumObject.setReplyNum(mesNumObject.getReplyNum() + e.getNum());
                    break;
                case 12:
                    mesNumObject.setClikNum(mesNumObject.getClikNum() + e.getNum());
                    break;
                case 13:
                    mesNumObject.setNotifyNum(mesNumObject.getNotifyNum() + e.getNum());
                    break;
//                case 14:
//                    mesNumObject.setOperateNum(mesNumObject.getOperateNum() + e.getNum());
//                    break;
                case 15:
                    mesNumObject.setSystemNotifyNum(mesNumObject.getSystemNotifyNum() + e.getNum());
                    break;
            }
            notifyMesNum(mesNumObject);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SocketStatusBus e) {
        if (e.status == 2) {
            mBinding.txtChatSocketview.setVisibility(View.GONE);
            refresh();
        } else {
            isHttp = false;
            mBinding.txtChatSocketview.setVisibility(View.VISIBLE);
            mBinding.swipeLayout.setRefreshing(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MesReduceBus e) {
        if (e.type > 0) {
            switch (e.type) {
                case 101:
                case 104:
                case 111:
                case 112:
                case 113:
                case 201:
                case 211:
                case 212:
                    if (mesNumObject != null && mesNumObject.getNotifyNum() > 0)
                        mesNumObject.setNotifyNum(mesNumObject.getNotifyNum() - 1);
                    break;
                case 311:
                case 312:
                    if (mesNumObject != null && mesNumObject.getReplyNum() > 0)
                        mesNumObject.setReplyNum(mesNumObject.getReplyNum() - 1);
                    break;
                case 321:
                    if (mesNumObject != null && mesNumObject.getClikNum() > 0)
                        mesNumObject.setClikNum(mesNumObject.getClikNum() - 1);
                    break;
                case 401:
                    if (mesNumObject != null && mesNumObject.getSystemNotifyNum() > 0)
                        mesNumObject.setSystemNotifyNum(mesNumObject.getSystemNotifyNum() - 1);
                    break;
//                case 402:
//                    if (mesNumObject != null && mesNumObject.getOperateNum() > 0)
//                        mesNumObject.setOperateNum(mesNumObject.getOperateNum() - 1);
//                    break;
            }
            notifyMesNum(mesNumObject);
        }
    }

    @Override
    protected boolean isBindEventBus() {
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isHttp = false;
        mBinding.swipeLayout.setRefreshing(false);
        boxQuery.remove();
        mesNumObject.setUser_id(authorId);
        box.put(mesNumObject);

        boxQueryMes.remove();
        for (MessageFragmentInnerBean data : mDatas) {
            data.setUid_android(authorId);
        }
        boxMes.put(mDatas);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUnreadMes();
    }

    private void updateUnreadMes() {
        if (mesNumObject != null)
            if (!hasUnreadMes() && mesNumObject.getReplyNum() == 0 && mesNumObject.getClikNum() == 0 &&
                    mesNumObject.getNotifyNum() == 0 && mesNumObject.getOperateNum() == 0 && mesNumObject.getSystemNotifyNum() == 0) {
                EventBus.getDefault().post(new NavigatorBus(1));
            } else {
                NoticeChangeBean.DataBean bean = new NoticeChangeBean.DataBean();
                bean.setTypeX(Second_unread_news);
                EventBus.getDefault().post(bean);
            }
    }

    private boolean hasUnreadMes() {
        for (MessageFragmentInnerBean data : mDatas) {
            if (data.getN() > 0) {
                return true;
            }
        }
        return false;
    }
}
