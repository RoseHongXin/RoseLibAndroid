package rose.android.jlib.kit;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.*;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;

import java.util.List;

/**
 * Wifi manager class for getting the WIFI details and SSID gateway parameters
 *
 * @author raviteja
 */

@SuppressLint("DefaultLocale")
public class WiFiMgrUtil {

    private final String TAG = "WiFiInfoUtil";

    private final int STATE_NONE = 0;
    private final int STATE_AVAILABLE = 1;
    private final int STATE_LOST = 2;

    private final ConnectivityManager mConnMgr;
    private WifiManager mWiFiMgr = null;
    private WifiInfo mWiFiInfo = null;
    private Context mCtx = null;
    private int mState = STATE_NONE;


    private final BroadcastReceiver mWiFiStateReceiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
            if(!TextUtils.equals(intent.getAction(), WifiManager.NETWORK_STATE_CHANGED_ACTION)) { return; }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network network = mConnMgr == null ? null : mConnMgr.getActiveNetwork();
                NetworkCapabilities capabilities = mConnMgr == null ? null : mConnMgr.getNetworkCapabilities(network);
                if(capabilities != null
                        && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                    onWiFiState(STATE_AVAILABLE);
                }else{
                    onWiFiState(STATE_LOST);
                }
            }else{
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if(info == null || info.getType() != ConnectivityManager.TYPE_WIFI) { return; }
                NetworkInfo.DetailedState state = info.getDetailedState();
                if(state == NetworkInfo.DetailedState.CONNECTED){
                    onWiFiState(STATE_AVAILABLE);
                }else{
                    onWiFiState(STATE_LOST);
                }
            }
        }
    } ;

    public void onWiFiAvailable(){}
    public void onWiFiLost(){}
    public void onDestroy(){
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            }else{
                mCtx.unregisterReceiver(mWiFiStateReceiver);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public WiFiMgrUtil(Context ctx) {
        this.mCtx = ctx;
        mWiFiMgr = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mWiFiInfo = mWiFiMgr != null ? mWiFiMgr.getConnectionInfo() : null;
        mConnMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback(){
                @Override public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                    onWiFiState(STATE_AVAILABLE);
                }
                @Override public void onLost(@NonNull Network network) {
                    super.onLost(network);
                    onWiFiState(STATE_LOST);
                }
            };
            NetworkRequest rqst = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .build();
            mConnMgr.registerNetworkCallback(rqst, callback);
        }else{
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            ctx.registerReceiver(mWiFiStateReceiver, intentFilter);
        }
    }
    private void onWiFiState(int state){
        switch (state){
            case STATE_AVAILABLE:
                if(mState != STATE_AVAILABLE){
                    mWiFiInfo = mWiFiMgr.getConnectionInfo();
                    onWiFiAvailable();
                    mState = STATE_AVAILABLE;
                    Log.i(TAG, "onAvailable");
                }
                Log.i(TAG, "onAvailable ......");
                break;
            case STATE_LOST:
                if(mState != STATE_LOST){
                    onWiFiLost();
                    mState = STATE_LOST;
                    Log.i(TAG, "onLost......");
                }
                Log.i(TAG, "onLost......");
                break;
        }
    }

    public boolean isConnected(){ return mState == STATE_AVAILABLE; }
    public String getBSSID() { return removeSSIDQuotes(mWiFiInfo == null ? "" : mWiFiInfo.getBSSID()); }
    public String getSSID() { return removeSSIDQuotes(mWiFiInfo == null ? "" : mWiFiInfo.getSSID()); }
    public String getIp() {
        if(mWiFiInfo == null) return "";
        int ipval = mWiFiInfo.getIpAddress();
        return String.format("%d.%d.%d.%d", (ipval & 0xff), (ipval >> 8 & 0xff), (ipval >> 16 & 0xff), (ipval >> 24 & 0xff));
    }
    public String getGatewayIp() {
        if(mWiFiInfo == null) return "";
        int gatwayVal = mWiFiMgr.getDhcpInfo().gateway;
        return (String.format("%d.%d.%d.%d", (gatwayVal & 0xff), (gatwayVal >> 8 & 0xff), (gatwayVal >> 16 & 0xff), (gatwayVal >> 24 & 0xff)));
    }

    public boolean is24GHz() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return is24GHz(mWiFiInfo == null ? 0: mWiFiInfo.getFrequency());
        }
        return true;
    }

    public boolean is5GHz() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { return is5GHz(mWiFiInfo == null ? 0: mWiFiInfo.getFrequency()); }
        return false;
    }

    /**
     * @return -1--未知信号频率
     * 0--2.4GHZ
     * 2--5GHZ
     */
    public int getFrequency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            boolean g2Hz = is24GHz(mWiFiInfo.getFrequency());
            return g2Hz ? 0 : 1;
        } else {
            List<ScanResult> scanResults = mWiFiMgr.getScanResults();
            for (ScanResult result : scanResults) {
                if (result.BSSID.equalsIgnoreCase(mWiFiInfo.getBSSID())
                        && result.SSID.equalsIgnoreCase(mWiFiInfo.getSSID()
                        .substring(1, mWiFiInfo.getSSID().length() - 1))) {
                    boolean g2Hz = is24GHz(result.frequency);
                    return g2Hz ? 0 : 1;
                }
            }
        }
        return -1;
    }

    ////////////////////////////////////////////////////
    private String removeSSIDQuotes(String ssid) {
        if(ssid == null || ssid.equals("")) return "";
        if (ssid.startsWith("\"") && ssid.endsWith("\"")) { ssid = ssid.substring(1, ssid.length() - 1); }
        return ssid;
    }
    public static boolean is24GHz(int freq) { return freq > 2400 && freq < 2500; }
    public static boolean is5GHz(int freq) { return freq > 4900 && freq < 5900; }
}
