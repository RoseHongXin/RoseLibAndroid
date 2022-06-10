package rose.android.jlib.kit.view;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import androidx.appcompat.widget.SearchView;

/**
 * Created by RoseHongXin on 2018/2/1 0001.
 */

public class SrvHelper {

    public static SrvHelper handle(SearchView _srv_, Callback cb){
        return new SrvHelper(_srv_, cb, false);
    }

    public static SrvHelper handle(EditText _et_searchKey, View _iv_textClear, Callback cb){
        return new SrvHelper(_et_searchKey, _iv_textClear, cb, true);
    }

    private SrvHelper(EditText _et_searchKey, View _iv_textClear, Callback cb, boolean eagermode){
        if(_iv_textClear != null){
            _iv_textClear.setOnClickListener(v -> {
                _et_searchKey.setText("");
                ViewKit.hideInputMgr(_et_searchKey);
            });
        }
        if(_et_searchKey != null){
            _et_searchKey.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
            _et_searchKey.setOnEditorActionListener((v, actionId, event) -> {
                String text = v.getText().toString();
                if(TextUtils.isEmpty(text)) return true;
                if(cb != null) cb.onSearch(text);
                return true;
            });
            if(eagermode){
                _et_searchKey.addTextChangedListener(new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
                    @Override public void afterTextChanged(Editable s) {
                        String text = s.toString();
                        if(cb != null) cb.onSearch(text);
                    }
                });
            }
            _et_searchKey.setOnFocusChangeListener((v, hasFocus) -> {
                if(!hasFocus) ViewKit.hideInputMgr(_et_searchKey);
            });
        }
    }

    private SrvHelper(SearchView _srv_, Callback cb, boolean eagermode){
        _srv_.setOnCloseListener(() -> {
            ViewKit.hideInputMgr(_srv_);
            return false;
        });
        _srv_.setSubmitButtonEnabled(true);
        _srv_.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(cb != null) cb.onSearch(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(eagermode) { if(cb != null) cb.onSearch(newText); }
                return false;
            }
        });
        _srv_.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) ViewKit.hideInputMgr(_srv_);
        });
    }

    public interface Callback{
        void onSearch(String text);
    }

}
