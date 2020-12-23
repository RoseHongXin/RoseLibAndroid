package rose.android.jlib.kit.info;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetworkUtil {

	public static boolean connected(Context ctx) {
		try {
			if (ctx != null) {
				ConnectivityManager connectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
				if (connectivity != null) {
					NetworkInfo info = connectivity.getActiveNetworkInfo();
					if (info != null && info.isConnected()) {
						if (info.getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public static boolean isWifiConnected(Context ctx) {
		try {
			ConnectivityManager mConnectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = mConnectivity.getActiveNetworkInfo();
			int netType = -1;
			if(info != null){
				netType = info.getType();
			}
			if (netType == ConnectivityManager.TYPE_WIFI) {
				return info.isConnected();
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isMobileConnected(Context ctx) {
		try {
			ConnectivityManager mConnectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = mConnectivity.getActiveNetworkInfo();
			int netType = -1;
			if(info != null){
				netType = info.getType();
			}
			if (netType == ConnectivityManager.TYPE_MOBILE) {
				return info.isConnected();
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public static int getConnectedType(Context ctx) {
		if (ctx != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	@SuppressLint("DefaultLocale")
	public static String getLocalIpAddress(Context ctx) {
		WifiManager wifiManager = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		// 获取32位整型IP地址
		int ipAddress = wifiInfo.getIpAddress();
		// 返回整型地址转换成“*.*.*.*”地址
		return String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
	}

	/**
	 * 返回网段号码 如：192.168.80.
	 * */
	public static String getNetworkSegment(Context ctx) {
		WifiManager wifiManager = (WifiManager) ctx.getApplicationContext().getSystemService(android.content.Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		try {
			return String.format("%d.%d.%d.", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff));// .toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 返回网段最后一个数字
	 * */
	public static int getNetworkLastNo(String ipAddress1) {
		if (ipAddress1 == null || ipAddress1.equals("")) return 0;
		String[] split = ipAddress1.split("\\.");
		if (split == null || split.length == 0) return 0;
		String s = split[split.length - 1];
		try {
			return Integer.parseInt(s);
		}catch (Exception e){
			return 0;
		}
	}

	/**
	 * 获取GPRS的IP
	 * @return
	 */
	public static String getIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

}
