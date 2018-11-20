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