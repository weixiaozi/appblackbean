package bbs.com.xinfeng.bbswork.utils.shortvideo.videorecord.utils;

import android.os.Environment;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.utils.shortvideo.choose.TCConstants;

/**
 * Created by yuejiaoli on 2017/10/11.
 */

public class TCEditerUtil {

    /**
     * 生成编辑后输出视频路径
     *
     * @return
     */
    public static String generateVideoPath() {
        String outputPath = Constant.STORAGE_PATH;
        File outputFolder = new File(outputPath);

        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }
        String current = String.valueOf(System.currentTimeMillis() / 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String time = sdf.format(new Date(Long.valueOf(current + "000")));
        String saveFileName = String.format("TXVideo_%s.mp4", time);
        return outputFolder + "/" + saveFileName;
    }

}
