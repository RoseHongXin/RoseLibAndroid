# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\android-sdk-windows/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in builder.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-ignorewarnings
-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志
-dontoptimize

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法
-dontwarn
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes InnerClasses,LineNumberTable,Exceptions
-keepattributes *Annotation*
-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class android.support.**{ *;}    # 保持哪些类不被混淆
-keep public class java.lang.invoke.**{ *;}
-keep public class org.apache.lang.**{ *;}
-keep public class org.apache.commons.**{ *;}
-keep public class com.nhaarman.**{ *;}
-keep public class se.emilsjolander.**{ *;}
-keep class android.support.v7.** { *; }
-dontwarn android.support.v7.**
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.app.FragmentActivity
-dontwarn com.support.multidex.**
-keep public class com.support.multidex.**{ *;}
-dontwarn android.support.design.**
-keep public class com.support.design.**{ *;}
-dontwarn android.support.annotations.**
-keep public class com.support.annotations.**{ *;}
#保护泛型
-keepattributes Signature
-keepattributes *Annotation  #不混淆注释
-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
     public static native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class **.R$* {
    public static <fields>;
}
# 保留Serializable 序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
   static final long serialVersionUID;
   private static final java.io.ObjectStreamField[] serialPersistentFields;
   !static !transient <fields>;
   private void writeObject(java.io.ObjectOutputStream);
   private void readObject(java.io.ObjectInputStream);
   java.lang.Object writeReplace();
   java.lang.Object readResolve();
}
# 对于带有回调函数onXXEvent的，不能混淆
-keepclassmembers class * {
    void *(**On*Event);
}

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}


#fastjson
-dontwarn com.alibaba.fastjson.**
-keepclasseswithmembernames class com.alibaba.fastjson.** { *;}

#jackson
-dontwarn com.fasterxml.jackson.annotation.**
-keep class com.fasterxml.jackson.annotation.** { *; }
-dontwarn com.fasterxml.jackson.core.**
-keep class com.fasterxml.jackson.core.** { *; }
-dontwarn com.fasterxml.jackson.databind.**
-keep class com.fasterxml.jackson.databind.** { *; }


##  HXAnroidLib
-keep public class hx.components.**{*;}
-keep public class * extends hx.components.specific.ABaseWeb{*;}
-keep public class hx.kit.**{*;}
-keep public class hx.view.**{*;}
-keep public class hx.widget.**{*;}