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

###### 退出App
```java
//            am.killBackgroundProcesses(packageName)   //1
//            val forceStopPackage = am.javaClass.getDeclaredMethod("forceStopPackage", String :: class.java)
//            forceStopPackage.isAccessible = true
//            forceStopPackage.invoke(am, packageName)  //2
//            android.os.Process.killProcess(android.os.Process.myPid())      //4
//            System.exit(0)                            //3
            ///////////////////5
//            val startMain = Intent(Intent.ACTION_MAIN)
//            startMain.addCategory(Intent.CATEGORY_HOME)
//            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(startMain)
//            System.exit(0)
            ///////////////////5
```

```
重新点击Desktop icon 或者通知栏通知, 显示上次退出要home的页面bug修复(launchMode和从哪里启动有关[ES安装后启动就有问题,点击桌面图标就是OK的]).
Intent notifyIntent = new Intent(this, AMain.class);
        notifyIntent.setAction(Intent.ACTION_MAIN);
        notifyIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP | Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS | Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
```

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


```java
 _chart.getXAxis().setAxisMaximum(labelCount - 1);
 _chart.getXAxis().setAxisMinimum(0f);
 _chart.setMaxVisibleValueCount(labelCount);
```
