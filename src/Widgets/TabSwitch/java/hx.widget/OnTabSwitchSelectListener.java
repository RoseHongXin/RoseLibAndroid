package hx.widget;

import androidx.annotation.IdRes;
import android.widget.TextView;

/**
 * Created by RoseHongXin on 2017/9/12 0012.
 */

public interface OnTabSwitchSelectListener {
    void onSelected(@IdRes int id, int idx, TextView _tv);
}
