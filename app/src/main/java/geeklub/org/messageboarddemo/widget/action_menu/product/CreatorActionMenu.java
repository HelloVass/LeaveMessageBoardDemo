package geeklub.org.messageboarddemo.widget.action_menu.product;

import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import geeklub.org.messageboarddemo.R;

/**
 * Created by HelloVass on 16/1/13.
 */
public class CreatorActionMenu implements IActionMenu {

  private final static int GROUP_ID = 1;

  private final static int ACTION_DELETE_ID = 100;

  private final static int ORDER = 33;

  private final static String ACTION_DELETE = "删除";

  private PopupMenu mPopupMenu;

  private OnActionMenuItemClickListener mOnActionMenuItemClickListener;

  public CreatorActionMenu(View view) {
    mPopupMenu = new PopupMenu(view.getContext(), view);
    mPopupMenu.inflate(R.menu.popup_menu_leave_msg);
    mPopupMenu.getMenu().add(GROUP_ID, ACTION_DELETE_ID, ORDER, ACTION_DELETE);
    mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
          case ACTION_DELETE_ID:
            mOnActionMenuItemClickListener.deleteAction();
            return true;

          default:
            return false;
        }
      }
    });
  }

  @Override public void show() {
    mPopupMenu.show();
  }

  public interface OnActionMenuItemClickListener {
    void deleteAction();
  }

  public void setOnActionMenuItemClickListener(
      OnActionMenuItemClickListener onActionMenuItemClickListener) {
    mOnActionMenuItemClickListener = onActionMenuItemClickListener;
  }
}
