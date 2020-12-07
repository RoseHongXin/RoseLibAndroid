package hx.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextPaint;
import android.text.TextUtils;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import hx.lib.R;

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

    public static void Toast(@NonNull Activity act, Object msg){
        Toast(act, msg, null);
    }
    public static void Toast(@NonNull Activity act, Object msg, DialogInterface.OnClickListener listener){
        if(act == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if(act.isFinishing() || act.isDestroyed()) return;
        }else{
            if(act.isFinishing()) return;
        }
        final String decoMsg = text(act, msg);
        DialogHelper.BtnCenter(
                new AlertDialog.Builder(act)
                        .setTitle(decoMsg)
                        .setCancelable(false)
                        .setNeutralButton(R.string.HX_confirm, listener),
                dialog -> {
                    int count = textNewLineCharacterCount(decoMsg);
                    TextView _tv_ = ((AlertDialog)dialog).findViewById(R.id.alertTitle);
                    if(count == 0 && _tv_ != null){
                        TextPaint paint = _tv_.getPaint();
                        count = (int) (paint.measureText(decoMsg) / (_tv_.getWidth() - _tv_.getPaddingLeft() - _tv_.getPaddingRight()) + 0.5f);
                    }
                    if(_tv_ != null){ _tv_.setLines(_tv_.getLineCount() + count); }
                })
                .show();
    }
    public static void Confirm(@NonNull Activity act, Object msg, DialogInterface.OnClickListener listener){
        Confirm(act, msg, -1, listener);
    }
    public static void Confirm(@NonNull Activity act, Object msg, Object positiveBt, DialogInterface.OnClickListener listener){
        Confirm(act, new Object[]{null, msg, positiveBt}, listener);
    }
    public static void Confirm(@NonNull Activity act, Object[] texts, DialogInterface.OnClickListener listener){
        if(act == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if(act.isFinishing() || act.isDestroyed()) return;
        }else{
            if(act.isFinishing()) return;
        }
        String title = text(act, texts != null && texts.length > 0 ? texts[0] : null);
        String msg = text(act, texts != null && texts.length > 1 ? texts[1] : null);
        String positiveBt = text(act, texts != null && texts.length > 2 ? texts[2] : null);
        String btTxt = TextUtils.isEmpty(positiveBt) ? act.getString(R.string.HX_confirm) : positiveBt;
        AlertDialog.Builder builder = new AlertDialog.Builder(act)
                .setCancelable(false)
                .setPositiveButton(btTxt, listener)
                .setNegativeButton(R.string.HX_cancel, null);
        AlertDialog dlg;
        if(TextUtils.isEmpty(title)){
            dlg = builder.setTitle(msg).create();
            DialogHelper.BtnFlat(dlg, dialog -> loadTitle(msg, dlg));
        }else{
            dlg = builder.setTitle(title).setMessage(msg).create();
            DialogHelper.BtnFlat(dlg, dialog -> loadTitle(title, dlg));
        }
        dlg.show();
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
}
