package geeklub.org.messageboarddemo.widget.action_menu.factory;

import android.view.View;
import geeklub.org.messageboarddemo.widget.action_menu.product.CreatorActionMenu;

/**
 * Created by HelloVass on 16/1/13.
 */
public class ActionMenuFactory {

  public static CreatorActionMenu produceCreatorActionMenu(View view) {
    return new CreatorActionMenu(view);
  }
}
