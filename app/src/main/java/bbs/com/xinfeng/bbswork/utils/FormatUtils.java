package bbs.com.xinfeng.bbswork.utils;

import android.os.Message;
import android.text.TextUtils;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.logging.Handler;

import bbs.com.xinfeng.bbswork.base.App;
import bbs.com.xinfeng.bbswork.base.Constant;
import bbs.com.xinfeng.bbswork.ui.service.SocketService;

/**
 * Created by qiang on 2016/4/19.
 */
public class FormatUtils {
    private static Calendar mCalendar;
    private static Calendar mCalendarNow;
    private static DecimalFormat df = new DecimalFormat("#,###.###");
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static int getNowDayOfWeek() {
        return getDayOfWeek(new Date());
    }

    public static int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static long getTodayMSByTime(int hour, int minute) {
        long now = System.currentTimeMillis();
        Date nowDate = new Date(now);
        nowDate.setHours(hour);
        nowDate.setMinutes(minute);
        nowDate.setSeconds(0);
        return nowDate.getTime();
    }

    public static String getFormatMSTime(long ms, String format) {
        Date date = new Date(ms);
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static String setTime(String createtime) {
        try {
            long nowTime = System.currentTimeMillis() / 1000;
            long betweenTime = (nowTime - Long.parseLong(createtime));
            if (mCalendar == null) {
                mCalendarNow = Calendar.getInstance();
                mCalendar = Calendar.getInstance();
            }
            mCalendarNow.setTimeInMillis(nowTime * 1000);
            mCalendar.setTimeInMillis(Long.parseLong(createtime) * 1000);
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH) + 1;
            int day = mCalendar.get(Calendar.DATE);
            int nowYear = mCalendarNow.get(Calendar.YEAR);
            int nowMonth = mCalendarNow.get(Calendar.MONTH) + 1;
            int nowDay = mCalendarNow.get(Calendar.DATE);
            if (year == nowYear && month == nowMonth && day == nowDay) {
                if (betweenTime < 60) {
                    return "刚刚";
                } else if (betweenTime >= 60 && betweenTime < 3600) {
                    return betweenTime / 60 + "分钟前";
                } else if (betweenTime >= 3600 && betweenTime < 14400) {//4小时以内
                    return betweenTime / 60 / 60 + "小时前";
                } else {
                    int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                    int min = mCalendar.get(Calendar.MINUTE);
                    String sHour = hour + "";
                    String sMin = min + "";
                    if (hour < 10)
                        sHour = "0" + hour;
                    if (min < 10)
                        sMin = "0" + min;
                    return sHour + ":" + sMin;
                }
            } else if (year == nowYear && month == nowMonth && nowDay == day + 1) {
                int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                int min = mCalendar.get(Calendar.MINUTE);
                String sHour = hour + "";
                String sMin = min + "";
                if (hour < 10)
                    sHour = "0" + hour;
                if (min < 10)
                    sMin = "0" + min;
                return "昨天 " + sHour + ":" + sMin;
            } else if (year == nowYear && nowMonth == month + 1 && betweenTime < 86400) {
                int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                int min = mCalendar.get(Calendar.MINUTE);
                String sHour = hour + "";
                String sMin = min + "";
                if (hour < 10)
                    sHour = "0" + hour;
                if (min < 10)
                    sMin = "0" + min;
                return "昨天 " + sHour + ":" + sMin;
            } else if (nowYear == year + 1 && betweenTime < 86400) {
                int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                int min = mCalendar.get(Calendar.MINUTE);
                String sHour = hour + "";
                String sMin = min + "";
                if (hour < 10)
                    sHour = "0" + hour;
                if (min < 10)
                    sMin = "0" + min;
                return "昨天 " + sHour + ":" + sMin;
            } else if (nowYear == year) {
                String sMonth = month + "";
                String sDay = day + "";
                if (month < 10)
                    sMonth = "0" + month;
                if (day < 10)
                    sDay = "0" + day;

                int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                int min = mCalendar.get(Calendar.MINUTE);
                String sHour = hour + "";
                String sMin = min + "";
                if (hour < 10)
                    sHour = "0" + hour;
                if (min < 10)
                    sMin = "0" + min;

                return sMonth + "-" + sDay + " " + sHour + ":" + sMin;
            } else {
                String sMonth = month + "";
                String sDay = day + "";
                if (month < 10)
                    sMonth = "0" + month;
                if (day < 10)
                    sDay = "0" + day;
                return year + "-" + sMonth + "-" + sDay;
            }
        } catch (Exception e) {
            return createtime;
        }
    }

    public static String setTimeNo(String createtime) {
        long nowTime = System.currentTimeMillis() / 1000;
        if (mCalendar == null) {
            mCalendarNow = Calendar.getInstance();
            mCalendar = Calendar.getInstance();
        }
        mCalendarNow.setTimeInMillis(nowTime * 1000);
        mCalendar.setTimeInMillis(Long.parseLong(createtime) * 1000);
        String da = "上午";
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int min = mCalendar.get(Calendar.MINUTE);
        if (hour > 12) {
            hour = hour - 12;
            da = "下午";
        }
        return da + " " + hour + ":" + min;

    }

    public static String setTime1(String createtime) {
        long nowTime = System.currentTimeMillis() / 1000;
        long betweenTime = (nowTime - Long.parseLong(createtime));
        if (betweenTime < 60) {
            return "刚刚";
        } else if (betweenTime >= 60 && betweenTime < 3600) {
            return betweenTime / 60 + "分钟前";
        } else if (betweenTime >= 3600 && betweenTime < 86400) {
            return betweenTime / 60 / 60 + "小时前";
        } else if (betweenTime >= 86400 && betweenTime < 2592000) {
            return betweenTime / 60 / 60 / 24 + "天前";
        } else if (betweenTime >= 2592000 && betweenTime < 31104000) {
            return betweenTime / 60 / 60 / 24 / 30 + "个月前";
        } else {
            return "1年前";
        }
    }


    public static String moneyFormat(String num) {
        double money = Double.parseDouble(num);
        if (money >= 100000000) {
            double moneyYI = money / 100000000;
            DecimalFormat df = new DecimalFormat("#.#");
            return df.format(moneyYI) + "亿";
        } else {
            return df.format(money);
        }
    }

    /**
     * 时间转化为1:20:30 liqiang
     *
     * @param
     * @return
     */

    private static StringBuilder mFormatBuilder;
    private static Formatter mFormatter;

    public static String stringForTime(int totalSeconds) {
        if (mFormatBuilder == null || mFormatter == null) {
            mFormatBuilder = new StringBuilder();
            mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        }
        int seconds = totalSeconds % 60;

        int minutes = (totalSeconds / 60) % 60;

        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds)
                    .toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }

    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long date_temp = Long.valueOf(s);
        Date date = new Date(date_temp * 1000L);
        res = simpleDateFormat.format(date);
        return res;
    }/*
    /*
     * 将时间戳转换为时间
     */

    public static String stampToSpecificDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long date_temp = Long.valueOf(s);
        Date date = new Date(date_temp * 1000L);

        res = simpleDateFormat.format(date);
        return res;
    }/*
  * 将时间戳转换为时间
  */

    public static String stampToDateMonthly(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        long lt = new Long(s);
        Date date = new Date(lt * 1000L);
        res = simpleDateFormat.format(date);
        return res;
    }

    private static SimpleDateFormat todayFormat = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat yestodayFormat = new SimpleDateFormat("MM-dd HH:mm");
    private static SimpleDateFormat lastYearFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    private static SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static String year;
    private static String day;

    static {
        Date date = new Date();
        year = yearFormat.format(date);
        day = dayFormat.format(date);
        updataTime();

    }

    private static void updataTime() {
        App.getInstance().mHandler.postAtTime(new Runnable() {
            @Override
            public void run() {
                Date date = new Date();
                year = yearFormat.format(date);
                day = dayFormat.format(date);
                updataTime();
            }
        }, (getTimesnight() - 1000 * 60));
    }

    public static long getTimesnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis());
    }

    public static String dataToUseData(String timeData) {
        if (TextUtils.isEmpty(timeData))
            return "";
        try {
            Date date = format.parse(timeData);
            if (!year.equals(yearFormat.format(date))) {
                return lastYearFormat.format(date);
            } else if (!day.equals(dayFormat.format(date))) {
                return yestodayFormat.format(date);
            } else {
                return todayFormat.format(date);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeData;
    }

    //时间转时间戳
    public static long getStringToDate(String dateString) {
        Date date = new Date();
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
}
