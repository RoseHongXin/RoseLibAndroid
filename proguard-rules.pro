-optimizationpasses 5          # 指定代码的压缩级别
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法
-keepattributes InnerClasses,LineNumberTable,Exceptions

#####################ButterKnife#####################
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
#####################ButterKnife#####################

#####################Fastjson#####################
-dontwarn com.alibaba.fastjson.**
-keepclasseswithmembernames class com.alibaba.fastjson.** { *;}
#####################Fastjson#####################

#####################Jackson#####################
-dontwarn com.fasterxml.jackson.annotation.**
-keep class com.fasterxml.jackson.annotation.** { *; }
-dontwarn com.fasterxml.jackson.core.**
-keep class com.fasterxml.jackson.core.** { *; }
-dontwarn com.fasterxml.jackson.databind.**
-keep class com.fasterxml.jackson.databind.** { *; }
#####################Jackson#####################

#####################OkHttp3#####################
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**
#####################OkHttp3#####################

#####################Retrofit#####################
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
#####################Retrofit#####################

#####################RxJava RxAndroid#####################
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
#####################RxJava RxAndroid#####################

#### this lib
-keep public class hx.** { *;}
-keep public class in.srain.cube.** { *;}
