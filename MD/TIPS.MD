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