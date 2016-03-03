package org.geeklub.smartkeyboardmanager.utils;

import android.content.Context;

/**
 * Created by HelloVass on 16/1/15.
 */
public class DensityUtil {

  public static int px2dip(Context context, float px) {
    float scale = context.getResources().getDisplayMetrics().density;
    return (int) (px / scale + 0.5f);
  }

  public static int dip2px(Context context, float dp) {
    float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dp * scale + 0.5f);
  }
}
