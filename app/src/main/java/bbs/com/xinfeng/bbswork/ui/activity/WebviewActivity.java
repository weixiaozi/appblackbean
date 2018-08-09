package bbs.com.xinfeng.bbswork.ui.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import bbs.com.xinfeng.bbswork.R;
import bbs.com.xinfeng.bbswork.base.BaseActivity;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.databinding.ActivityWebviewBinding;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import bbs.com.xinfeng.bbswork.mvp.presenter.TestPresenter;
import bbs.com.xinfeng.bbswork.mvp.presenter.WebviewPresenter;
import bbs.com.xinfeng.bbswork.utils.SystemUtil;

import static android.util.Base64.NO_WRAP;
import static bbs.com.xinfeng.bbswork.base.Constant.GAME_PATH;

public class WebviewActivity extends BaseActivity<ActivityWebviewBinding, WebviewPresenter> implements NetContract.INetView {

    private WebView mWebView;

    @Override
    public void showLoading(int tag) {
        startLoading();
    }

    @Override
    public void hideLoading(int tag) {
        stopLoading();
    }

    @Override
    public void showError(ErrorBean errorBean, int tag) {

    }

    @Override
    protected WebviewPresenter creatPresenter() {
        return new WebviewPresenter(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected int initView(Bundle savedInstanceState) {
        return R.layout.activity_webview;
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void initData(Bundle savedInstanceState) {
        if (getIntent().getBooleanExtra("head", false)) {
            mBinding.basebar.llayoutBarRoot.setVisibility(View.VISIBLE);
            mBinding.basebar.barLeftClick.setVisibility(View.VISIBLE);
            mBinding.basebar.barLeftClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            mBinding.basebar.barTxtTitle.setText(!TextUtils.isEmpty(getIntent().getStringExtra("webtitle")) ? getIntent().getStringExtra("webtitle") : "");
        } else {
            mBinding.basebar.llayoutBarRoot.setVisibility(View.GONE);
        }

        mWebView = new WebView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(params);
        mBinding.parent.addView(mWebView);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowContentAccess(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setUserAgentString(settings.getUserAgentString() + "|app_android|v" + SystemUtil.getAppVersion());

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.addJavascriptInterface(this, "phonePlus");
        mWebView.setWebViewClient(new MyWebViewClient(this));
        mWebView.setWebChromeClient(new WebChromeClient());
        HashMap<String, String> map = new HashMap<>();
        map.put("Authorization", Constant.token);
        showLoading(0);
        String weburl = getIntent().getStringExtra("weburl");
        if (getIntent().getBooleanExtra("linksrc", false) && !weburl.startsWith("http")) {
            weburl = "http://" + getIntent().getStringExtra("weburl");
        }
        mWebView.loadUrl(weburl, map);
    }

    static class MyWebViewClient extends WebViewClient {
        private WeakReference<WebviewActivity> mReference;

        public MyWebViewClient(WebviewActivity activity) {
            mReference = new WeakReference<>(activity);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http")) {
                mReference.get().mWebView.loadUrl(url);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mReference.get().hideLoading(0);
            super.onPageFinished(view, url);
        }

    }


    @Override
    public void setData(ErrorBean errorBean, int tag) {

    }

    @Override
    public void progress(int precent, int tag) {

    }

    @JavascriptInterface
    public void finishPage() {
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
}
