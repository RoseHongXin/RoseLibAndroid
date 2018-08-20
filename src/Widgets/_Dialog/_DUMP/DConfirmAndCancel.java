package hx.widget.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import hx.lib.R;

/**
 * Created by Rose on 3/2/2017.
 */

public class DConfirmAndCancel {

    public static AlertDialog showOriginal(Activity act, String title, String msg, DialogInterface.OnClickListener onConfirmClick){
        AlertDialog d =  new AlertDialog.Builder(act).setTitle(title)
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton(R.string.HX_confirm, (dialog, which) -> {
                    dialog.dismiss();
                    if(onConfirmClick != null) onConfirmClick.onClick(dialog, which);
                })
                .setNegativeButton(R.string.HX_cancel, (dialog, which) -> {
                  dialog.dismiss();
                })
                .create();
        d.show();
        return d;
    }

    public static AlertDialog show(Activity act, Callback cb, View.OnClickListener listener0, View.OnClickListener listener1){
        AlertDialog.Builder builder = new AlertDialog.Builder(act, R.style.Dialog_WithBt);
        View layout  = act.getLayoutInflater().inflate(R.layout.d_confirm_and_cancel, null);
        AlertDialog dialog = builder.setView(layout).setCancelable(false).create();

        TextView _tb_tv_title = (TextView)layout.findViewById(R.id._tb_tv_title);
        TextView _tv_content = (TextView)layout.findViewById(R.id._tv_content);
        TextView _bt_0 = (TextView)layout.findViewById(R.id._bt_0);
        TextView _bt_1 = (TextView)layout.findViewById(R.id._bt_1);

        cb.onViewsSetup(_tb_tv_title, _tv_content, _bt_0, _bt_1);
        _bt_0.setOnClickListener(view -> {
            dialog.dismiss();
            if(listener0 != null) listener0.onClick(_bt_0);
        });
        _bt_1.setOnClickListener(view -> {
            dialog.dismiss();
            if(listener1 != null) listener1.onClick(_bt_1);
        });

        DialogHelper.padding(dialog, 24, 24);

        dialog.show();
        return dialog;
    }

    public static AlertDialog show(Activity act, String content, View.OnClickListener cb){
        return show(act, "", content, cb);
    }

    public static  AlertDialog show(Activity act, String title, String content, View.OnClickListener cb){
        return show(act, title, content, act.getString(R.string.HX_cancel), null, act.getString(R.string.HX_confirm), cb);
    }

    public static  AlertDialog show(Activity act, String title, String content, View.OnClickListener cb0, View.OnClickListener cb1){
        return show(act, title, content, act.getString(R.string.HX_cancel), cb0, act.getString(R.string.HX_confirm), cb1);
    }

    public static AlertDialog show(Activity act, String title, String content, String bt0, View.OnClickListener cb0, String bt1, View.OnClickListener cb1){
        AlertDialog dialog = show(act, (_tb_tv_title, _tv_content, _bt_0, _bt_1) -> {
            if(TextUtils.isEmpty(title)) _tb_tv_title.setVisibility(View.GONE);
            else _tb_tv_title.setText(title);
            _tv_content.setText(content);
            _bt_0.setText(bt0);
            _bt_1.setText(bt1);
        }, cb0, cb1);
        dialog.show();
        return dialog;
    }

    public interface Callback{
        void onViewsSetup(TextView _tb_tv_title, TextView _tv_content, TextView _bt_0, TextView _bt_1);
    }

}
