* Switch的click事件返回是,onCheckChange事件已经回调了,所以isChecked()是切换后的状态 <br>
* TextPaint 不需要考虑textWidth, textHeight只需要关注fontMetrics.bottom <br>
* 设置fullscreen的activity,通过设置windowBackground没有效果. <br>
* level-list.xml 必须从大到小排列. <br>
* Module 的proguard不能穿透到嵌套的Module中(即App引用Lib库,要额外的把proguard贴到App的proguard文件中) <br>
* WiFi状态WifiManager.WIFI_STATE_ENABLED之后,需要一个时间间隔才能读到ssid,否则会读到<unknown ssid> <br>
* android:textColorTertiary 作用于EditText <br>
* If you are using Kotlin, replace annotationProcessor with kapt.----be careful---- <br>
* android:colorControlActivated：表示CheckBox、EditText、Switch、Spinner、RadioButton等控件激活时候的色调，与之相同作用的一个属性：android:colorAccent。作用也是一样的。 <br>
* android:colorControlNormal：表示CheckBox、EditText、Switch、Spinner、RadioButton等控件默认时的色调 <br>
* Switch的onClick事件,回调的是selected参数,setSelected给该view的checked赋值<br>
  非焦点view,还是正常的isChecked/setChecked操作状态!
* EditText 默认使用的Theme颜色是 textColorPrimary
* ToolBar 菜单字体颜色由theme控制, ThemeOverlay.AppCompat.Dark.ActionBar, 全局控制可由toolBarStyle属性设置
* Menu 相关theme配置 android:actionMenuTextAppearance actionOverflowMenuStyle
* Dialog theme  alertDialogTheme, dialogTheme
>
  
----

#### compatibility
* WebView 在5.1及其以下 用androidx.appcompat:appcompat:1.1.0 有Error inflating class android.webkit.WebView 这bug
* 需要downgrade到appcompat:1.0.2, 或者自定义WebView, https://stackoverflow.com/questions/41025200/android-view-inflateexception-error-inflating-class-android-webkit-webview
* TelephonyManager可以get获取的设备表示类参数,在android10/sdk29后,都无法获取,throw SecurityException
 

#### SpecialTips 
* proguard-android.txt 在SDK/tools路径下
* lambda在低版本手机(比如华为4.4pad),通过this::****调用父类的方法,会报错(找不到方法?).
* byte类型,如果向上强制转换的时候,高位填的是1 , 在做字节运算的时候,需要特别注意. byte & 0xFF
* Activity嵌套Fragment时,如果多个Fragment含有相同的View和id,做LhBase或之类的集合时,要用View为参数,否则Fragment保存现场会有问题,显示最有一个操作的UI


#### activity给第三方app调用
```
manifest.xml intent-filter必须指定Category(一般指定android.intent.category.DEFAULT即可)
是否指定exported=true,没有实际效果(或许是测试包的原因)
通过setComponent("pkgName", "pkgName.clzName")一样可以打开activity.
uri方式打开activity还没测试.
```


---

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
