package rose.android.jlib.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;
import rose.android.jlib.R;

/**
 * Created by RoseHongXin on 2018/5/14 0014.
 */

public class DialogPool {

    private static int textNewLineCharacterCount(String text){
        if(TextUtils.isEmpty(text)) return 0;
        char[] chars = text.toCharArray();
        int count = 0;
        for(char ch : chars){
            if(ch == '\n') count++;
        }
        return count;
    }

    public static AlertDialog Toast(@NonNull Activity act, Object msg){
        return Toast(act, msg, null);
    }
    public static AlertDialog Toast(@NonNull Activity act, Object msg, DialogInterface.OnClickListener listener){
        if(act == null) return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if(act.isFinishing() || act.isDestroyed()) return null;
        }else{
            if(act.isFinishing()) return null;
        }
        final String decoMsg = text(act, msg);
        AlertDialog dlg = new AlertDialog.Builder(act)
                .setTitle(decoMsg)
                .setCancelable(false)
                .setNeutralButton(android.R.string.ok, listener)
                .create();
        DialogHelper.BtnCenter(dlg, dialog -> { loadTitle(decoMsg, dlg); });
        dlg.show();
        return dlg;
    }
    public static AlertDialog Confirm(@NonNull Activity act, Object msg, DialogInterface.OnClickListener listener){
        return CONFIRM(act, new Object[]{null, msg}, listener);
    }
    public static AlertDialog Confirm(@NonNull Activity act, Object msg, Object positiveBt, DialogInterface.OnClickListener listener){
        return CONFIRM(act, new Object[]{null, msg, positiveBt}, listener);
    }
    public static AlertDialog Confirm(@NonNull Activity act, Object[] texts, DialogInterface.OnClickListener listener){
        return CONFIRM(act, texts, listener);
    }
    public static AlertDialog CONFIRM(@NonNull Activity act, Object[] texts, DialogInterface.OnClickListener ... listeners){
        if(act == null) return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if(act.isFinishing() || act.isDestroyed()) return null;
        }else{
            if(act.isFinishing()) return null;
        }
        String title = text(act, texts != null && texts.length > 0 ? texts[0] : null);
        String msg = text(act, texts != null && texts.length > 1 ? texts[1] : null);
        String positiveBt = text(act, texts != null && texts.length > 2 ? texts[2] : null);
        String negativeBt = text(act, texts != null && texts.length > 3 ? texts[3] : null);
        String positiveBtnTxt = TextUtils.isEmpty(positiveBt) ? act.getString(android.R.string.ok) : positiveBt;
        String negativeBtnTxt = TextUtils.isEmpty(negativeBt) ? act.getString(android.R.string.cancel) : negativeBt;
        DialogInterface.OnClickListener confirmListener = listeners.length > 0 ? listeners[0] : null;
        DialogInterface.OnClickListener cancelListener = listeners.length > 1 ? listeners[1] : null;
        AlertDialog.Builder builder = new AlertDialog.Builder(act)
                .setCancelable(false)
                .setPositiveButton(positiveBtnTxt, confirmListener)
                .setNegativeButton(negativeBtnTxt, cancelListener);
        AlertDialog dlg;
        if(TextUtils.isEmpty(title)){
            dlg = builder.setTitle(msg).create();
            DialogHelper.BtnFlat(dlg, dialog -> loadTitle(msg, dlg));
        }else{
            dlg = builder.setTitle(title).setMessage(msg).create();
            DialogHelper.BtnFlat(dlg, dialog -> loadTitle(title, dlg));
        }
        dlg.show();
        return dlg;
    }

    private static void loadTitle(String msg, AlertDialog dialog){
        int count = textNewLineCharacterCount(msg);
        TextView _tv_ = ((AlertDialog)dialog).findViewById(R.id.alertTitle);
        if(count == 0 && _tv_ != null){
            TextPaint paint = _tv_.getPaint();
            count = (int) (paint.measureText(msg) / (_tv_.getWidth() - _tv_.getPaddingLeft() - _tv_.getPaddingRight()) + 0.5f);
        }
        if(_tv_ != null){ _tv_.setLines(_tv_.getLineCount() + count); }
    }

    private static String text(Context ctx, Object text){
        if(text == null) return "";
        String txtStr = "";
        if(text instanceof String) {
            txtStr = (String) text;
        }else if(text instanceof Integer) {
            int resId = (int)text;
            if(resId != -1) txtStr = ctx.getString(resId);
        }
        return txtStr;
    }

    public static AlertDialog NewContextDlg(Context ctx, @StyleRes int style) {
        AlertDialog dialog = new AlertDialog.Builder(ctx, style)
                .create();
        if (dialog.getWindow() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            } else {
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
            }
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
        return dialog;
    }
}
