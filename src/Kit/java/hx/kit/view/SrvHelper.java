package hx.kit.view;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import hx.lib.R;

/**
 * Created by RoseHongXin on 2018/2/1 0001.
 */

public class SrvHelper {

    public static SrvHelper handle(SearchView _srv_, Callback cb){
        return new SrvHelper(_srv_, cb);
    }

//    public static SrvHelper handle(Fragment fra, Callback cb){
//        return handle(fra.getView(), cb);
//    }
    public static SrvHelper handle(Activity act, Callback cb){
        return handle(act.findViewById(android.R.id.content), cb);
    }

//    public static SrvHelper handle(View _v_layout, Callback cb){
//        return new SrvHelper(_v_layout, cb);
//    }

//    private SrvHelper(View _v_layout, Callback cb){
//        EditText _et_searchKey = (EditText) _v_layout.findViewById(R.id._et_searchKey);
//        View _iv_textClear = _v_layout.findViewById(R.id._iv_textClear);
//        if(_iv_textClear != null){
//            _iv_textClear.setOnClickListener(v -> {
//                _et_searchKey.text("");
//                ViewKit.hideInputMgr(_et_searchKey);
//            });
//        }
//        if(_et_searchKey != null){
//            _et_searchKey.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
//            _et_searchKey.setOnEditorActionListener((v, actionId, event) -> {
//                String text = v.getText().toString();
//                if(TextUtils.isEmpty(text)) return true;
//                if(cb != null) cb.onSearch(text);
//                return true;
//            });
//        }
//    }

    private SrvHelper(SearchView _srv_, Callback cb){
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
