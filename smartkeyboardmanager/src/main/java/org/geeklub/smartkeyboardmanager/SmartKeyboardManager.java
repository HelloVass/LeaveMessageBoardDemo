package org.geeklub.smartkeyboardmanager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import org.geeklub.smartkeyboardmanager.utils.SupportSoftKeyboardUtil;

/**
 * Created by HelloVass on 16/3/2.
 */
public class SmartKeyboardManager {

  private final static long DURATION_SWITCH_EMOTION_KEYBOARD = 150L;

  private Activity mActivity;

  private View mContentView;

  private View mEmotionKeyboard;

  private EditText mEditText;

  private View mEmotionTrigger;

  private InputMethodManager mInputMethodManager;

  private SupportSoftKeyboardUtil mSupportSoftKeyboardUtil;

  public SmartKeyboardManager(Builder builder) {
    mActivity = builder.mNestedActivity;
    mContentView = builder.mNestedContentView;
    mEmotionKeyboard = builder.mNestedEmotionKeyboard;
    mEditText = builder.mNestedEditText;
    mEmotionTrigger = builder.mNestedEmotionTrigger;
    mInputMethodManager = builder.mNestedInputMethodManager;
    mSupportSoftKeyboardUtil = builder.mNestedSupportSoftKeyboardUtil;
    setUpCallbacks();
  }

  private void setUpCallbacks() {
    // 设置 EditText 监听器
    mEditText.requestFocus();
    mEditText.setOnTouchListener(new ThrottleTouchListener() {
      @Override public void onThrottleTouch() {
        if (mEmotionKeyboard.isShown()) {
          hideEmotionKeyboardByLockContentViewHeight();
        }
      }
    });

    // 为“颜文字表情键盘“切换按钮设置监听器
    mEmotionTrigger.setOnTouchListener(new ThrottleTouchListener() {
      @Override public void onThrottleTouch() {
        if (mEmotionKeyboard.isShown()) { // "颜文字键盘"显示
          hideEmotionKeyboardByLockContentViewHeight();
        } else { // "颜文字键盘"隐藏
          if (mSupportSoftKeyboardUtil.isSoftKeyboardShown()) { // "软键盘"显示
            showEmotionKeyboardByLockContentViewHeight();
          } else { // "软键盘"隐藏
            showEmotionKeyboardWithoutLockContentViewHeight();
          }
        }
      }
    });
  }

  public boolean interceptBackPressed() {
    // 如果颜文字键盘还在显示，中断"back"操作
    if (mEmotionKeyboard.isShown()) {
      hideEmotionKeyboardWithoutSoftKeyboard();
      return true;
    }
    return false;
  }

  /**
   * 显示“颜文字键盘”，隐藏软键盘(锁定“ContentView”的高度)
   */
  private void showEmotionKeyboardByLockContentViewHeight() {

    mEmotionKeyboard.setVisibility(View.VISIBLE);
    mEmotionKeyboard.getLayoutParams().height =
        mSupportSoftKeyboardUtil.getSupportSoftKeyboardHeight();

    ObjectAnimator showAnimator = ObjectAnimator.ofFloat(mEmotionKeyboard, "alpha", 0.0F, 1.0F);
    showAnimator.setDuration(DURATION_SWITCH_EMOTION_KEYBOARD);
    showAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    showAnimator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationStart(Animator animation) {
        lockContentViewHeight();
        hideSoftKeyboard();
      }

      @Override public void onAnimationEnd(Animator animation) {
        unlockContentViewHeight();
      }
    });
    showAnimator.start();
  }

  /**
   * 隐藏“颜文字键盘”，显示“软键盘”（锁定“ContentView”的高度）
   */
  private void hideEmotionKeyboardByLockContentViewHeight() {
    ObjectAnimator hideAnimator = ObjectAnimator.ofFloat(mEmotionKeyboard, "alpha", 1.0F, 0.0F);
    hideAnimator.setDuration(DURATION_SWITCH_EMOTION_KEYBOARD);
    hideAnimator.setInterpolator(new AccelerateInterpolator());
    hideAnimator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationStart(Animator animation) {
        lockContentViewHeight();
        showSoftKeyboard();
      }

      @Override public void onAnimationEnd(Animator animation) {
        mEmotionKeyboard.setVisibility(View.GONE);
        unlockContentViewHeight();
      }
    });
    hideAnimator.start();
  }

  /**
   * 显示“颜文字键盘”，隐藏“软键盘”(不锁定“ContentView”的高度)
   */
  private void showEmotionKeyboardWithoutLockContentViewHeight() {
    mEmotionKeyboard.setVisibility(View.VISIBLE);
    mEmotionKeyboard.getLayoutParams().height =
        mSupportSoftKeyboardUtil.getSupportSoftKeyboardHeight();

    ObjectAnimator showAnimator = ObjectAnimator.ofFloat(mEmotionKeyboard, "alpha", 0.0F, 1.0F);
    showAnimator.setDuration(DURATION_SWITCH_EMOTION_KEYBOARD);
    showAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    showAnimator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationStart(Animator animation) {
        hideSoftKeyboard();
      }
    });
    showAnimator.start();
  }

  /**
   * 隐藏"颜文字键盘"，不显示“软键盘”
   */
  private void hideEmotionKeyboardWithoutSoftKeyboard() {
    ObjectAnimator hideAnimator = ObjectAnimator.ofFloat(mEmotionKeyboard, "alpha", 1.0F, 0.0F);
    hideAnimator.setDuration(DURATION_SWITCH_EMOTION_KEYBOARD);
    hideAnimator.setInterpolator(new AccelerateInterpolator());
    hideAnimator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        mEmotionKeyboard.setVisibility(View.GONE);
      }
    });
    hideAnimator.start();
  }

  /**
   * 隐藏软键盘
   */
  private void hideSoftKeyboard() {
    mInputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
  }

  /**
   * 显示软键盘
   */
  private void showSoftKeyboard() {
    mEditText.requestFocus();
    mInputMethodManager.showSoftInput(mEditText, 0);
  }

  /**
   * 锁定 ContentView 的高度
   */
  private void lockContentViewHeight() {
    LinearLayout.LayoutParams layoutParams =
        (LinearLayout.LayoutParams) mContentView.getLayoutParams();
    layoutParams.height = mContentView.getHeight();
    layoutParams.weight = 0.0F;
  }

  /**
   * 解锁 ContentView 的高度
   */
  private void unlockContentViewHeight() {
    ((LinearLayout.LayoutParams) mContentView.getLayoutParams()).weight = 1.0F;
  }

  public static class Builder {

    private Activity mNestedActivity;

    private View mNestedContentView;

    private View mNestedEmotionKeyboard;

    private EditText mNestedEditText;

    private View mNestedEmotionTrigger;

    private InputMethodManager mNestedInputMethodManager;

    private SupportSoftKeyboardUtil mNestedSupportSoftKeyboardUtil;

    public Builder(Activity activity) {
      this.mNestedActivity = activity;
    }

    public Builder setContentView(View contentView) {
      this.mNestedContentView = contentView;
      return this;
    }

    public Builder setEmotionKeyboard(View emotionKeyboard) {
      this.mNestedEmotionKeyboard = emotionKeyboard;
      return this;
    }

    public Builder setEditText(EditText editText) {
      this.mNestedEditText = editText;
      return this;
    }

    public Builder setFaceTextEmotionTrigger(View trigger) {
      this.mNestedEmotionTrigger = trigger;
      return this;
    }

    public SmartKeyboardManager create() {
      initFieldsWithDefaultValue();
      return new SmartKeyboardManager(this);
    }

    private void initFieldsWithDefaultValue() {
      this.mNestedInputMethodManager =
          (InputMethodManager) mNestedActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
      this.mNestedSupportSoftKeyboardUtil = new SupportSoftKeyboardUtil(mNestedActivity);
      mNestedActivity.getWindow()
          .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
              | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
  }
}
