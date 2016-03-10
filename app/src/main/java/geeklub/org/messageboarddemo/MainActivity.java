package geeklub.org.messageboarddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import geeklub.org.messageboarddemo.adapter.MessageBoardAdapter;
import geeklub.org.messageboarddemo.entities.LeaveMessage;
import geeklub.org.messageboarddemo.source.RawSource;
import java.util.ArrayList;
import java.util.List;
import la.juju.android.ftil.widgets.FaceTextInputLayout;
import org.geeklub.smartkeyboardmanager.SmartKeyboardManager;

public class MainActivity extends AppCompatActivity
    implements MessageBoardAdapter.OnMessageBoardActionListener {

  private static final String TAG = MainActivity.class.getSimpleName();

  private Toolbar mToolbar;

  private RecyclerView mRecyclerView;

  private EditText mFaceTextEmotionEditText;

  private ImageView mFaceTextEmotionTrigger;

  private FaceTextInputLayout mFaceTextInputLayout;

  private List<LeaveMessage> mLeaveMessageList;

  private MessageBoardAdapter mMessageBoardAdapter;

  private SmartKeyboardManager mSmartKeyboardManager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_leave_msg_board);

    getDummyDataSource();

    mMessageBoardAdapter = new MessageBoardAdapter(this, mLeaveMessageList);
    mMessageBoardAdapter.setOnMessageBoardActionListener(this); // 设置 overflow 按钮点击事件

    setUpViews();
  }

  private void setUpViews() {
    mToolbar = (Toolbar) findViewById(R.id.toolbar);
    mRecyclerView = (RecyclerView) findViewById(R.id.rcv_list);
    mFaceTextEmotionEditText = (EditText) findViewById(R.id.et_face_text_emotion);
    mFaceTextEmotionTrigger = (ImageView) findViewById(R.id.iv_face_text_emotion);
    mFaceTextInputLayout = (FaceTextInputLayout) findViewById(R.id.ftil_keyboard);

    // 设置 Toolbar
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    mToolbar.setNavigationIcon(R.mipmap.ic_launcher);

    // 设置 Recyclerview
    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mRecyclerView.setAdapter(mMessageBoardAdapter);

    // 设置颜文字
    mFaceTextInputLayout.setFaceTextSource(new RawSource(this, R.raw.face_text));

    mSmartKeyboardManager = new SmartKeyboardManager.Builder(this).setContentView(mRecyclerView)
        .setEmotionKeyboard(mFaceTextInputLayout)
        .setEditText(mFaceTextEmotionEditText)
        .setEmotionTrigger(mFaceTextEmotionTrigger)
        .addOnContentViewScrollListener(new SmartKeyboardManager.OnContentViewScrollListener() {
          @Override public void shouldScroll(int distance) {
            mRecyclerView.smoothScrollBy(0, distance);
          }
        })
        .create();
  }

  private void getDummyDataSource() {
    mLeaveMessageList = new ArrayList<>();
    for (int i = 0; i < 33; i++) {
      LeaveMessage message = new LeaveMessage();

      message.user_name = "hellovass -->>" + i;
      message.floor_num = "# -->>" + i;
      message.message_content = "测试内容 -->>" + i;

      mLeaveMessageList.add(message);
    }
  }

  /**
   * 点击 overflow 按钮回调
   */
  @Override public void onOverflowAction(View view, int position) {

  }

  @Override public void onBackPressed() {
    if (!mSmartKeyboardManager.interceptBackPressed()) {
      super.onBackPressed();
    }
  }
}
