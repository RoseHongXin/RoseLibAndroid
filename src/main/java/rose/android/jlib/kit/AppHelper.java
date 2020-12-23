package rose.android.jlib.kit;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by RoseHongXin on 2017/6/15 0015.
 */

public class AppHelper {

     public static boolean isActForeground(Activity act) {
         if(!isAppForeground(act)) return false;
         ActivityManager am = (ActivityManager) act.getSystemService(Context.ACTIVITY_SERVICE);
         int taskId = act.getTaskId();
         String actName = act.getClass().getName();
         String packageName = act.getPackageName();
         if(Build.VERSION.SDK_INT  >= Build.VERSION_CODES.M) {
             List<ActivityManager.AppTask> tasks = am.getAppTasks();
             for(ActivityManager.AppTask task : tasks) {
                 ActivityManager.RecentTaskInfo info = task.getTaskInfo();
                 if(info.affiliatedTaskId == taskId && info.topActivity.getClassName().equals(actName) && info.topActivity.getPackageName().equals(packageName)) return true;
             }
         }else {
             List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
             if(tasks != null && !tasks.isEmpty() && tasks.get(0).topActivity.getClassName().equals(actName)) return true;
         }
        return false;
    }
    public static boolean isActForeground(Context ctx, String pkgName) {
        ActivityManager am = ctx == null ? null : (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        if(am == null) return false;
        if(Build.VERSION.SDK_INT  >= Build.VERSION_CODES.M) {
            List<ActivityManager.AppTask> tasks = am.getAppTasks();
            for(ActivityManager.AppTask task : tasks) {
                ActivityManager.RecentTaskInfo info = task.getTaskInfo();
                if(info.topActivity.getPackageName().equals(pkgName)) return true;
            }
        }else {
            List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
            if(tasks != null && !tasks.isEmpty() && tasks.get(0).topActivity.getPackageName().equals(pkgName)) return true;
        }
        return false;
    }
    public static boolean isAppForeground(Context ctx) {
        return ctx != null && isAppForeground(ctx, ctx.getPackageName());
    }
    public static boolean isAppForeground(Context ctx, String pkgName){
        ActivityManager am = ctx == null ? null : (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        if(am == null) return false;
        List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
        if(processes == null || processes.isEmpty()) return false;
        for(ActivityManager.RunningAppProcessInfo process : processes){
            if(process.processName.equals(pkgName) && process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) return true;
        }
        return false;
    }
    public static boolean isAppRunning(Context ctx){
        ActivityManager am = ctx == null ? null : (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        if(am == null) return false;
        List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
        if(processes == null || processes.isEmpty()) return false;
        String packageName = ctx.getPackageName();
        for(ActivityManager.RunningAppProcessInfo process : processes){
            if(process.processName.equals(packageName)) return true;
        }
        return false;
    }

    public static boolean ifSwipeApp2Foreground(Context ctx, String pkgName){
        ActivityManager am = ctx == null ? null : (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        if(am == null) return false;
        List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
        if(processes == null || processes.isEmpty()) return false;
        ActivityManager.RunningAppProcessInfo targetProc = null;
        for(ActivityManager.RunningAppProcessInfo process : processes){
            if(process.processName.equals(pkgName)) {
                targetProc = process;
                break;
            }
        }
        if(targetProc != null) {
            List<ActivityManager.AppTask> appTasks = am.getAppTasks();
            if(appTasks == null || appTasks.isEmpty()) return false;
            for(ActivityManager.AppTask task : appTasks){
                ActivityManager.RecentTaskInfo taskInfo = task.getTaskInfo();
                ComponentName componentName = taskInfo.topActivity;
                if(componentName == null) componentName = taskInfo.baseActivity;
                if(componentName == null) componentName = taskInfo.origActivity;
                if(componentName != null){
                    if(TextUtils.equals(componentName.getPackageName(), pkgName)){
                        task.moveToFront();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void start(Activity act, Class<? extends Activity> targetAct){
        start(act, targetAct, 0, null);
    }
    public static void start(Activity act, Class<? extends Activity> targetAct, int flags){
        start(act, targetAct, flags, null);
    }
    public static void start(Activity act, Class<? extends Activity> targetAct, Bundle bundle){
        start(act, targetAct, 0, bundle);
    }
    public static void start(Activity act, Class<? extends Activity> targetAct, int flags, Bundle bundle){
        Intent intent = new Intent(act, targetAct);
        if(flags != 0) intent.setFlags(flags);
        if(bundle != null) intent.putExtras(bundle);
        act.startActivity(intent);
    }

    public static void start(Context ctx, Class<? extends Activity> targetAct){
        Intent intent = new Intent(ctx, targetAct);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }


}

/*
if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP){
             ActivityManager am = ctx == null ? null : (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
             if(am == null) return false;
             List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
             if(processes == null || processes.isEmpty()) return false;
             for(ActivityManager.RunningAppProcessInfo process : processes){
                 if(process.processName.equals(pkgName) && process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) return true;
             }
             return false;
         }

        UsageStatsManager usage = (UsageStatsManager) ctx.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        List<UsageStats> stats = usage.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000*1000, time);
        if (stats != null) {
            SortedMap<Long, UsageStats> runningTask = new TreeMap<Long,UsageStats>();
            for (UsageStats usageStats : stats) {
                runningTask.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (runningTask.isEmpty()) {
                return false;
            }
            String thePkgName =  runningTask.get(runningTask.lastKey()).getPackageName();
            return TextUtils.equals(thePkgName, pkgName);
        }
        return false;
 */
