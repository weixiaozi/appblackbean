package bbs.com.xinfeng.bbswork.mvp.model;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import bbs.com.xinfeng.bbswork.base.http.ErrorCode;
import bbs.com.xinfeng.bbswork.domin.DownLoadBean;
import bbs.com.xinfeng.bbswork.domin.ErrorBean;
import bbs.com.xinfeng.bbswork.mvp.contract.NetContract;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static bbs.com.xinfeng.bbswork.domin.ErrorBean.TYPE_DEAL;

/**
 * Created by dell on 2017/11/22.
 */

public class UpdataApkModel extends NetModel {

    private Call newCall;

    public UpdataApkModel(NetContract.OnDataLoadingListener dataLoadingListener, Context activity) {
        super(dataLoadingListener, activity);
    }

    public File downloadApk(String url, String path, String name, int tag) {
        OkHttpClient client = new OkHttpClient().newBuilder().
                connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).build();
        try {
            Request request = new Request.Builder().url(url).build();
            newCall = client.newCall(request);
        } catch (IllegalArgumentException e) {
            dataLoadingListener.onError(new ErrorBean(ErrorCode.ERROR_CODE_DOWNLOAD_ILLEGAL, ErrorCode.ERROR_DESC_DOWNLOAD_ILLEGAL + "\n" + e, TYPE_DEAL), tag);
            return null;
        }

        try {
            return saveFile(newCall.execute(), path, name, tag);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private File saveFile(Response response, String path, String name, int tag) {
        InputStream is = null;
        byte[] buf = new byte[2048];
        FileOutputStream fos = null;

        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0L;
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, name);
            fos = new FileOutputStream(file);
            int lastPercent = 0;
            int len1;
            while ((len1 = is.read(buf)) != -1) {
                sum += (long) len1;
                fos.write(buf, 0, len1);
                int percent = (int) ((float) sum * 100f / (float) total);
                if (percent - lastPercent > 1) {
                    dataLoadingListener.onProgress(percent, tag, false);
                    lastPercent = percent;
                }
            }
            dataLoadingListener.onProgress(100, tag, true);
            dataLoadingListener.onSuccess(new DownLoadBean(file.getAbsolutePath()), tag, false);
            fos.flush();
            return file;
        } catch (Exception e) {
            dataLoadingListener.onError(new ErrorBean(ErrorCode.ERROR_CODE_DOWNLOAD_IO, ErrorCode.ERROR_DESC_DOWNLOAD_IO + "\n" + e, TYPE_DEAL), tag);
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException var22) {
                dataLoadingListener.onError(new ErrorBean(ErrorCode.ERROR_CODE_DOWNLOAD_IO, ErrorCode.ERROR_DESC_DOWNLOAD_IO + "\n" + var22, TYPE_DEAL), tag);
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException var21) {
                dataLoadingListener.onError(new ErrorBean(ErrorCode.ERROR_CODE_DOWNLOAD_IO, ErrorCode.ERROR_DESC_DOWNLOAD_IO + "\n" + var21, TYPE_DEAL), tag);
            }
        }
    }
}
