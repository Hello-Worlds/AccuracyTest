package cn.lenovo.accuracytest.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * 工具类
 * Created by chao on 2017/6/7.
 */
public class Utils {
    private static Toast toast;//Toast单例模式

    //短吐司
    public static void toast(final Context context, final CharSequence text) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (toast == null) {
                        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                    } else {
                        toast.setText(text);
                        toast.setDuration(Toast.LENGTH_SHORT);
                    }
                    toast.show();
                }
            });
        }
    }

    //长吐司
    public static void l_toast(final Context context, final CharSequence text) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (toast == null) {
                        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                    } else {
                        toast.setText(text);
                        toast.setDuration(Toast.LENGTH_LONG);
                    }
                    toast.show();
                }
            });
        }
    }

    /**
     * DP转像素
     */
    public static int dp_to_px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int) (dpValue * (scale / 160) + 0.5f);
    }

    /**
     * 像素转DP
     */
    public static int px_to_dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int) ((pxValue * 160) / scale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获得状态栏（通知栏）的高度
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
