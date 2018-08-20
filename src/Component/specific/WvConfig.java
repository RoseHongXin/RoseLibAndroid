package hx.components.specific;

import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by rose on 16-9-5.
 */
public class WvConfig {

    public static void config(WebView wv){
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient());
    }

    public static void configWebOfStaticPage(WebView wv){
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient());
    }

    private static void enablecrossdomain(WebView wv){
        try{
            Field field = WebView.class.getDeclaredField("mWebViewCore");
            field.setAccessible(true);
            Object webviewcore = field.get(wv);
            Method method = webviewcore.getClass().getDeclaredMethod("nativeRegisterURLSchemeAsLocal", String.class);
            method.setAccessible(true);
            method.invoke(webviewcore, "http");
            method.invoke(webviewcore, "https");

            Method method1 = webviewcore.getClass().getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
            method1.setAccessible(true);
            method1.invoke(wv.getSettings(), true);

        } catch(Exception   e){
            Log.d("wokao","enablecrossdomain error");
            e.printStackTrace();
        }
    }
}
