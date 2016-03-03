package geeklub.org.messageboarddemo.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by HelloVass on 16/3/2.
 */
public class FaceTextEmotionKeyboardController {

  private static final String TAG = FaceTextEmotionKeyboardController.class.getSimpleName();

  private Activity mActivity;

  private View mContentView;

  private View mFaceTextInputLayout;

  private EditText mEditText;

  private View mFaceTextTrigger;

  private InputMethodManager mInputMethodManager;

  private SupportSoftKeyboardUtil mSupportSoftKeyboardUtil;

  public FaceTextEmotionKeyboardController(Builder builder) {
    mActivity = builder.mNestedActivity;
    mContentView = builder.mNestedContentView;
    mFaceTextInputLayout = builder.mNestedFaceTextInputLayout;
    mEditText = builder.mNestedEditText;
    mFaceTextTrigger = builder.mNestedFaceTextTrigger;
    mInputMethodManager = builder.mNestedInputMethodManager;
    mSupportSoftKeyboardUtil = builder.mNestedSupportSoftKeyboardUtil;
    setUpCallbacks();
  }

  public boolean interceptBackPressed() {
    // 如果颜文字键盘还在显示，中断 back 操作
    if (mFaceTextInputLayout.isShown()) {
      hideFaceTextInputLayout();
      return true;
    }
    return false;
  }

  private void setUpCallbacks() {
    mEditText.requestFocus();
    mEditText.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && mFaceTextInputLayout.isShown()) {
          Log.i(TAG, "mEditText.onTouch()");
          hideFaceTextInputLayout();
        }
        return false;
      }
    });

    mFaceTextTrigger.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Log.i(TAG, "mFaceTextTrigger.onClick()");
        if (mFaceTextInputLayout.isShown()) { // "颜文字键盘"显示
          hideFaceTextInputLayout();
        } else { // "颜文字键盘"隐藏
          if (mSupportSoftKeyboardUtil.isSoftKeyboardShown()) { // "软键盘"显示
            showFaceTextInputLayout();
          } else { // "软键盘"隐藏
            dismissFaceTextInputLayout();
          }
        }
      }
    });
  }

  /**
   * 显示颜文字键盘
   */
  private void showFaceTextInputLayout() {

    mFaceTextInputLayout.setVisibility(View.VISIBLE);

    ObjectAnimator showAnimator = ObjectAnimator.ofFloat(mFaceTextInputLayout, "alpha", 0.0F, 1.0F);
    showAnimator.setDuration(300);
    showAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    showAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        mFaceTextInputLayout.getLayoutParams().height =
            mSupportSoftKeyboardUtil.getSupportSoftKeyboardHeight();
      }
    });
    showAnimator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationStart(Animator animation) {
        super.onAnimationStart(animation);
        lockContentViewHeight();
        hideSoftKeyboard();
      }

      @Override public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        unlockContentViewHeight();
      }
    });
    showAnimator.start();
  }

  /**
   * 隐藏颜文字键盘
   */
  private void hideFaceTextInputLayout() {
    ObjectAnimator hideAnimator = ObjectAnimator.ofFloat(mFaceTextInputLayout, "alpha", 1.0F, 0.0F);
    hideAnimator.setDuration(300);
    hideAnimator.setInterpolator(new AccelerateInterpolator());
    hideAnimator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationStart(Animator animation) {
        lockContentViewHeight();
        showSoftKeyboard();
      }

      @Override public void onAnimationEnd(Animator animation) {
        mFaceTextInputLayout.setVisibility(View.GONE);
        unlockContentViewHeight();
      }
    });
    hideAnimator.start();
  }

  /**
   * 隐藏颜文字键盘
   */
  private void dismissFaceTextInputLayout() {
    ObjectAnimator dismissAnimator =
        ObjectAnimator.ofFloat(mFaceTextInputLayout, "alpha", 1.0F, 0.0F);
    dismissAnimator.setDuration(300);
    dismissAnimator.setInterpolator(new AccelerateInterpolator());
    dismissAnimator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationStart(Animator animation) {
        showSoftKeyboard();
      }

      @Override public void onAnimationEnd(Animator animation) {
        mFaceTextInputLayout.setVisibility(View.GONE);
      }
    });
    dismissAnimator.start();
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

    private View mNestedFaceTextInputLayout;

    private EditText mNestedEditText;

    private View mNestedFaceTextTrigger;

    private InputMethodManager mNestedInputMethodManager;

    private SupportSoftKeyboardUtil mNestedSupportSoftKeyboardUtil;

    public Builder(Activity activity) {
      this.mNestedActivity = activity;
    }

    public Builder setContentView(View contentView) {
      this.mNestedContentView = contentView;
      return this;
    }

    public Builder setFaceTextInputLayout(View inputLayout) {
      this.mNestedFaceTextInputLayout = inputLayout;
      return this;
    }

    public Builder setEditText(EditText editText) {
      this.mNestedEditText = editText;
      return this;
    }

    public Builder setFaceTextEmotionTrigger(View trigger) {
      this.mNestedFaceTextTrigger = trigger;
      return this;
    }

    public FaceTextEmotionKeyboardController create() {
      initFieldsWithDefaultValue();
      return new FaceTextEmotionKeyboardController(this);
    }

    private void initFieldsWithDefaultValue() {
      this.mNestedInputMethodManager =
          (InputMethodManager) mNestedActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
      this.mNestedSupportSoftKeyboardUtil = SupportSoftKeyboardUtil.getInstance(mNestedActivity);
      mNestedActivity.getWindow()
          .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
              | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
  }
}
