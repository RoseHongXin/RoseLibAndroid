package hx.kit.view;

import android.annotation.SuppressLint;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Created by RoseHongXin on 2017/8/18 0018.
 */

public class NavHelper {

    //BottomNavigationView require item shift if menu item count > 3,
    @SuppressLint("RestrictedApi")
    public  static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
            item.setShifting(false);
            item.setChecked(item.getItemData().isChecked());
        }

//        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
//        try {
//            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
//            shiftingMode.setAccessible(true);
//            shiftingMode.setBoolean(menuView, false);
//            shiftingMode.setAccessible(false);
//            for (int i = 0; i < menuView.getChildCount(); i++) {
//                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
//                //noinspection RestrictedApi
//            item.setShiftingMode(false);
//                item.setShifting(false);
//                // set once again checked code, so view will be updated
//                //noinspection RestrictedApi
//                item.setChecked(item.getItemData().isChecked());
//            }
//        } catch (NoSuchFieldException e) {
//            Log.e("NavHelper", "Unable to get shift mode field", e);
//        } catch (IllegalAccessException e) {
//            Log.e("NavHelper", "Unable to change code of shift mode", e);
//        }

    }
}
