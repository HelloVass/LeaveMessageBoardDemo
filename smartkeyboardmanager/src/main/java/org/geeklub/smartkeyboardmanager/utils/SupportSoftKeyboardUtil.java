package org.geeklub.smartkeyboardmanager.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by HelloVass on 16/3/2.
 */
public class SupportSoftKeyboardUtil {

  private static final String TAG = SupportSoftKeyboardUtil.class.getSimpleName();

  private final static String NAME_PREF_SOFT_KEYBOARD = "name_pref_soft_keyboard";

  private static final String KEY_PREF_SOFT_KEYBOARD_HEIGHT = "key_pref_soft_keyboard_height";

  // “表情键盘”默认高度 240dp
  private static final int DEFAULT_SOFT_KEYBOARD_HEIGHT = 240;

  public SupportSoftKeyboardUtil() {
    //  构造函数私有化
  }

  // 得到“软键盘”高度
  public static int getSupportSoftKeyboardHeight(Activity activity) {

    int softKeyboardHeight = getCurrentSoftInputHeight(activity);

    // 如果当前的键盘高度大于零，赶紧保存下来
    if (softKeyboardHeight > 0) {
      SharedPreferences sharedPreferences =
          activity.getSharedPreferences(NAME_PREF_SOFT_KEYBOARD, Context.MODE_PRIVATE);
      sharedPreferences.edit().putInt(KEY_PREF_SOFT_KEYBOARD_HEIGHT, softKeyboardHeight).apply();
    }

    // 如果当前“软键盘”高度等于零，可能是被隐藏了，也可能是我的锅，那就使用本地已经保存键盘高度
    if (softKeyboardHeight == 0) {
      SharedPreferences sharedPreferences =
          activity.getSharedPreferences(NAME_PREF_SOFT_KEYBOARD, Context.MODE_PRIVATE);
      softKeyboardHeight = sharedPreferences.getInt(KEY_PREF_SOFT_KEYBOARD_HEIGHT,
          DensityUtil.dip2px(activity, DEFAULT_SOFT_KEYBOARD_HEIGHT));
    }

    return softKeyboardHeight;
  }

  // 软键盘是否显示
  public static boolean isSoftKeyboardShown(Activity activity) {
    return getCurrentSoftInputHeight(activity) != 0;
  }

  /**
   * 得到虚拟按键的高度
   *
   * @return 虚拟按键的高度
   */
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) private static int getNavigationBarHeight(
      Context context) {

    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

    // 获取可用的高度
    DisplayMetrics defaultDisplayMetrics = new DisplayMetrics();
    windowManager.getDefaultDisplay().getMetrics(defaultDisplayMetrics);
    int usableHeight = defaultDisplayMetrics.heightPixels;

    // 获取实际的高度
    DisplayMetrics realDisplayMetrics = new DisplayMetrics();
    windowManager.getDefaultDisplay().getRealMetrics(realDisplayMetrics);
    int realHeight = realDisplayMetrics.heightPixels;

    return realHeight > usableHeight ? realHeight - usableHeight : 0;
  }

  /**
   * 得到当前软键盘的高度
   *
   * @return 软键盘的高度
   */
  public static int getCurrentSoftInputHeight(Activity activity) {

    Rect rect = new Rect();

    activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

    int screenHeight = activity.getWindow().getDecorView().getRootView().getHeight();

    int softInputHeight = screenHeight - rect.bottom;

    // Android LOLLIPOP 以上的版本才有"虚拟按键"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      softInputHeight -= getNavigationBarHeight(activity);
    }

    // excuse me?
    if (softInputHeight < 0) {
      Log.e(TAG, "excuse me，键盘高度小于0？");
    }

    return softInputHeight;
  }
}
