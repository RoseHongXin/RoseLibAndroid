```
ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for(ActivityManager.RunningServiceInfo rsi : am.getRunningServices(1000)){
//            Log4Android.e(this, "------------------" + rsi.pid + " " + rsi.process + " " + rsi.service + " " + rsi.clientPackage + " " + rsi.clientLabel);
//        }
        for(ActivityManager.RunningAppProcessInfo rsi : am.getRunningAppProcesses()){
            Log4Android.e(this, "------------------" + rsi.pid + "\t" + rsi.processName + "\t" + rsi.importance + "\t" + rsi.importanceReasonPid + "\t" + rsi.importanceReasonComponent);
        }
```


#### lambda在低版本手机(比如华为4.4pad),通过this::****调用父类的方法,会报错(找不到方法?).