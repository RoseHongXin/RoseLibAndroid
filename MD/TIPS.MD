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
> android:textColorTertiary 作用于EditText
=======

###### MPAndroid
```
MPAndroid  画groupBar时, 遵循:
//(barSpace + barWidth) * THE_COUNT + groupSpace = 1.0f;    //THE_COUNT == 2;
float fromX = -(batWidth + barSpace + groupSpace / 2);
画几组数据,就几组BarDataSet.
想让XAxis lable居中
//        xAxis.setAvoidFirstLastClipping(true);
//        xAxis.setCenterAxisLabels(true);
//        xAxis.setXOffset(0);
//        xAxis.setYOffset(0);
//        xAxis.setSpaceMin(0);
//        xAxis.setSpaceMax(0);
以上方法,都无效果 ????
```


```
一般某些聊天的SDK都依赖于libsqlite.so（环信、融云），不过由于这个包从来没有变化，使用的是系统默认提供的(/system/lib/)。在Android 6.x及以下的平台可以运行。
Android7.x执行更严格的安全检查，禁止使用系统目录的内容。所以如果希望在7.x以上版本，需要把系统目录的libsqlite.so拷贝出来，也放在自己app对应指令目录下。
---------------------
```