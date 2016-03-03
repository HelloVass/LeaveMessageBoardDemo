package geeklub.org.messageboarddemo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import geeklub.org.hellovass.common_adapter.sample.BaseRcvAdapter;
import geeklub.org.hellovass.common_adapter.sample.BaseRecyclerViewHolder;
import geeklub.org.messageboarddemo.R;
import geeklub.org.messageboarddemo.entities.LeaveMessage;
import java.util.List;

/**
 * Created by HelloVass on 16/1/11.
 */
public class MessageBoardAdapter extends BaseRcvAdapter<LeaveMessage> {

  private OnMessageBoardActionListener mOnMessageBoardActionListener;

  public MessageBoardAdapter(Context context, List<LeaveMessage> leaveMessages) {
    super(context, leaveMessages);
  }

  @Override protected void convert(final BaseRecyclerViewHolder holder, LeaveMessage leaveMessage) {
    TextView userName = holder.getView(R.id.tv_user_name);
    TextView floorNum = holder.getView(R.id.tv_floor_num);
    TextView messageContent = holder.getView(R.id.tv_leave_msg);
    ImageView overflowIcon = holder.getView(R.id.iv_over_flow_icon);

    overflowIcon.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (mOnMessageBoardActionListener != null) {
          mOnMessageBoardActionListener.onOverflowAction(v, holder.getAdapterPosition());
        }
      }
    });

    userName.setText(leaveMessage.user_name);
    floorNum.setText(leaveMessage.floor_num);
    messageContent.setText(leaveMessage.message_content);
  }

  @Override protected int getItemViewTypeHV(LeaveMessage leaveMessage) {
    return 33;
  }

  @Override protected int getLayoutResID(int resId) {
    return R.layout.listitem_leave_msg_board;
  }

  public interface OnMessageBoardActionListener {
    void onOverflowAction(View view, int position);
  }

  public void setOnMessageBoardActionListener(OnMessageBoardActionListener listener) {
    mOnMessageBoardActionListener = listener;
  }
}
