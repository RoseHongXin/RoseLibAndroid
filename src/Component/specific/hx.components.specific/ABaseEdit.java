package hx.components.specific;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import hx.components.ABase;
import hx.kit.async.RxBus;
import hx.lib.R;
import hx.widget.TopBarHelper;


public abstract class ABaseEdit extends ABase {

    protected static final String TITLE = "title";
    protected static final String TXT = "txt";
    protected static final String HINT = "hint";
    protected static final String INPUTTYPE = "inputType";
    protected static final String REQ_CODE = "reqCode";

    EditText _et;
    ImageView _iv_delete;
    TopBarHelper mTbHelper;

    boolean hasModify  = false;
    String title;
    String txt;
    String hint;
    int inputType;
    int reqCode;

    public abstract RxBus getRxBus();
    //public static abstract void start(Activity mAct, String title, String txt, String hint, int inputType, int reqCode);
    /*{
        Intent intent = new Intent(mAct, AEdit.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(TXT, txt);
        intent.putExtra(HINT, hint);
        intent.putExtra(INPUTTYPE, inputType);
        intent.putExtra(REQ_CODE, reqCode);
        mAct.startActivity(intent);
    }*/

    private void retreive(Intent intent){
        title = intent.getStringExtra(TITLE);
        txt = intent.getStringExtra(TXT);
        hint = intent.getStringExtra(HINT);
        inputType = intent.getIntExtra(INPUTTYPE, 0);
        reqCode = intent.getIntExtra(REQ_CODE, 0);
        if(TextUtils.isEmpty(txt)) _et.setHint(hint);
        else _et.setText(txt);
        _et.setInputType(inputType);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_edit);
        loadViews();
        retreive(getIntent());
        mTbHelper.right(View.GONE);
        _et.addTextChangedListener(new TextWatcher() {
            String originalText;
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                originalText = charSequence.toString();
            }
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {
                String newText = editable.toString();
                if(!TextUtils.equals(newText, originalText)){
                    hasModify = true;
                    mTbHelper.right(View.VISIBLE);
                }
            }
        });
//        WidgetObservable.text(_et)
//                .subscribe(onTextChangeEvent -> {
//                    hasModify = true;
//                    _tv_save.setVisibility(View.VISIBLE);
//                });
    }

    private void loadViews(){
        mTbHelper = TopBarHelper.obtain(this);
        mTbHelper.title(title);
        mTbHelper.text(R.string.HX_save, view -> {
                    RxBus rxBus = getRxBus();
                    if(rxBus == null) return;
                    RbEdit edit = new RbEdit();
                    edit.reqCode = reqCode;
                    edit.txt = _et.getText().toString();
                    rxBus.post(edit);
                    finish();
                });
        _et = (EditText)findViewById(R.id._et);
         _iv_delete = (ImageView)findViewById(R.id._iv_delete);
        _iv_delete.setOnClickListener(view-> _et.setText(""));
    }
}
