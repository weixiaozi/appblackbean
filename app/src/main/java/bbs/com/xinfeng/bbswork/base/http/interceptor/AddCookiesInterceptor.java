package bbs.com.xinfeng.bbswork.base.http.interceptor;

import android.content.Context;

import java.io.IOException;

import bbs.com.xinfeng.bbswork.utils.SharedPrefUtil;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {
    private Context context;

    public AddCookiesInterceptor(Context context) {
        super();
        this.context = context;

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            Request.Builder builder = chain.request().newBuilder();

            String cookie = SharedPrefUtil.getString("Cookie", null);
            if (cookie != null) {
                builder.addHeader("Cookie", cookie);
            }
            return chain.proceed(builder.build());
        } catch (Exception e) {
            return new Response.Builder().build();
        }
    }
}