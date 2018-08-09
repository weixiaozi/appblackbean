package bbs.com.xinfeng.bbswork.base.http.interceptor;

import android.text.TextUtils;

import java.io.IOException;

import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.utils.ArmsUtils;
import bbs.com.xinfeng.bbswork.utils.LogUtil;
import bbs.com.xinfeng.bbswork.utils.SystemUtil;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static bbs.com.xinfeng.bbswork.base.Constant.user_agent;

/**
 * Created by dell on 2017/11/29.
 */

public class HeadInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder request = chain.request().newBuilder();
        if (!TextUtils.isEmpty(Constant.token))
            request.addHeader("Authorization", Constant.token);
        LogUtil.i("token...", Constant.token);
        request.addHeader("Accept", "application/json");
        if (TextUtils.isEmpty(user_agent))
            user_agent = "|app_android|v:" + SystemUtil.getAppVersion() + "|s:" + ArmsUtils.encodeToMD5(SystemUtil.getMAC(App.getInstance()));
        request.header("User-Agent", user_agent);
        return chain.proceed(request.build());
    }
}
