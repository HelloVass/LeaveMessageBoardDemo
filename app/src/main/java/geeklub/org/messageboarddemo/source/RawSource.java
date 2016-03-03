package geeklub.org.messageboarddemo.source;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import la.juju.android.ftil.entities.FaceText;
import la.juju.android.ftil.source.FaceTextProvider;

/**
 * Created by HelloVass on 16/2/26.
 */
public class RawSource implements FaceTextProvider {

  private Context mContext;

  private int mResId;

  public RawSource(Context context, int resId) {
    mContext = context;
    mResId = resId;
  }

  @Override public List<FaceText> provideFaceTextList() {

    String tempLine;
    StringBuilder result = new StringBuilder();

    BufferedReader bufferedReader =
        new BufferedReader(new InputStreamReader(mContext.getResources().openRawResource(mResId)));

    try {
      while ((tempLine = bufferedReader.readLine()) != null) result.append(tempLine);

      return new Gson().fromJson(result.toString(), new TypeToken<List<FaceText>>() {
      }.getType());
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
