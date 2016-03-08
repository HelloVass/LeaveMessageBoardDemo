package geeklub.org.messageboarddemo.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import geeklub.org.messageboarddemo.R;

/**
 * Created by HelloVass on 16/3/1.
 */
public class FaceTextEditText extends EditText implements View.OnTouchListener {

  private Drawable mFaceTextIcon;

  private OnFaceTextIconClickListener mOnFaceTextIconClickListener;

  private OnTouchListener mOnTouchListener;

  public FaceTextEditText(Context context) {
    super(context);
    init(context);
  }

  public FaceTextEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public FaceTextEditText(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    // 获取用户设置的“颜文字”图标
    mFaceTextIcon = getCompoundDrawables()[2];

    if (mFaceTextIcon == null) {
      mFaceTextIcon = ContextCompat.getDrawable(context, R.mipmap.ic_trigger_emotion_keyboard);
    }

    Drawable wrappedFaceTextIcon = DrawableCompat.wrap(mFaceTextIcon);
    DrawableCompat.setTint(wrappedFaceTextIcon, getCurrentHintTextColor());
    mFaceTextIcon = wrappedFaceTextIcon;
    mFaceTextIcon.setBounds(0, 0, mFaceTextIcon.getIntrinsicWidth(),
        mFaceTextIcon.getIntrinsicHeight());

    showFaceTextIcon();
    super.setOnTouchListener(this);
  }

  private void showFaceTextIcon() {
    mFaceTextIcon.setVisible(true, false);
    Drawable[] compoundDrawables = getCompoundDrawables();
    setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], mFaceTextIcon,
        compoundDrawables[3]);
  }

  @Override public boolean onTouch(View v, MotionEvent event) {
    float downX = event.getX();
    if (mFaceTextIcon.isVisible()
        && downX > getWidth() - getPaddingRight() - mFaceTextIcon.getIntrinsicWidth()) {
      if (event.getAction() == MotionEvent.ACTION_UP) {
        if (mOnFaceTextIconClickListener != null) {
          mOnFaceTextIconClickListener.onFaceTextIconClick();
        }
        return true;
      }
    }
    return mOnTouchListener != null && mOnTouchListener.onTouch(v, event);
  }

  public interface OnFaceTextIconClickListener {
    void onFaceTextIconClick();
  }

  public void setOnFaceTextIconClickListener(
      OnFaceTextIconClickListener onFaceTextIconClickListener) {
    mOnFaceTextIconClickListener = onFaceTextIconClickListener;
  }

  @Override public void setOnTouchListener(OnTouchListener onTouchListener) {
    mOnTouchListener = onTouchListener;
  }
}
