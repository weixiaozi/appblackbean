package bbs.com.xinfeng.bbswork.base.http.interceptor;

import android.content.Context;


import java.io.IOException;

import io.reactivex.Flowable;
import bbs.com.xinfeng.bbswork.utils.SharedPrefUtil;
import okhttp3.Interceptor;
import okhttp3.Response;

public class ReceivedCookiesInterceptor implements Interceptor {
	private Context context;

	public ReceivedCookiesInterceptor(Context context) {
		super();
		this.context = context;

	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		try {
			Response originalResponse = chain.proceed(chain.request());
			//这里获取请求返回的cookie
			if (!originalResponse.headers("Set-Cookie").isEmpty()) {
				final StringBuffer cookieBuffer = new StringBuffer();
				Flowable.fromIterable(originalResponse.headers("Set-Cookie")).map(s -> {
					String[] cookieArray = s.split(";");
					return cookieArray[0];
				}).subscribe(s -> {
					cookieBuffer.append(s).append(";");
				});
				SharedPrefUtil.putString("Cookie",cookieBuffer.toString());
			}
			return originalResponse;
		} catch (Exception e) {
			return new Response.Builder().build();
		}
	}
}