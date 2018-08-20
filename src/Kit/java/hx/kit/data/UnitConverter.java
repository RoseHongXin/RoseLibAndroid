package hx.kit.data;

import android.app.Activity;
import android.content.Context;

/**
 * Created by RoseHongXin on 2017/9/19 0019.
 */

public class UnitConverter {

    public static int dp2px(Context ctx, int dp){
        return (int)(ctx.getResources().getDisplayMetrics().density * dp + 0.5f);
    }
    public static int px2dp(Context ctx, int px){
        return (int)(px / ctx.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static float sp2px(Activity act, float spValue, int type) {
        float scaledDensity = act.getResources().getDisplayMetrics().scaledDensity;
        switch (type) {
            case 0: //chinese
                return spValue * scaledDensity;
            case 1: //number or char
                return spValue * scaledDensity * 10.0f / 18.0f;
            default:
                return spValue * scaledDensity;
        }
    }

}
