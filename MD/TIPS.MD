```
ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
//            if(am != null) am.killBackgroundProcesses(getPackageName());
//            try {
//                Method forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);
//                forceStopPackage.setAccessible(true);
//                forceStopPackage.invoke(am, getPackageName());
//            }catch (Exception e){}
            new Handler().postDelayed(() -> {
                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());

            }, 100);
```


> Switch的click事件返回是,onCheckChange事件已经回调了,所以isChecked()是切换后的状态
> TextPaint 不需要考虑textWidth, textHeight只需要关注fontMetrics.bottom
> WiFi状态WifiManager.WIFI_STATE_ENABLED之后,需要一个时间间隔才能读到ssid,否则会读到<unknown ssid>