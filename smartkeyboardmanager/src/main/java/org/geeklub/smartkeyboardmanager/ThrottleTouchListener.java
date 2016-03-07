package org.geeklub.smartkeyboardmanager;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by HelloVass on 16/3/7.
 */
public abstract class ThrottleTouchListener implements View.OnTouchListener {

  private long mLastTouchTime = 0L;

  private final static long TOUCH_SKIP_DURATION = 300L;

  @Override public boolean onTouch(View v, MotionEvent event) {

    long currentTime = System.currentTimeMillis();

    long touchTimeInterval = currentTime - mLastTouchTime;

    if (event.getAction() == MotionEvent.ACTION_UP && touchTimeInterval > TOUCH_SKIP_DURATION) {
      onThrottleTouch();
      mLastTouchTime = currentTime;
    }

    return false;
  }

  public abstract void onThrottleTouch();
}
