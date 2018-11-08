##### 配置:
```
${Studio}/bin/ idea.properties 配置config和system路径
${Studio}/bin/ studio64.exe.vmoptions 配置运行环境

${gradle} gradle.properties  配置gradle路径, 环境变量配置  GRADLE_USER_HOME

```

> * 其他
 app:compileDebugJavaWithJavac这个错误,看是不是资源文件有重复,drawable和mipmap
 gradlew compileDebug --stacktrace
 gradlew compileDebug --stacktrace -info
 gradlew compileDebug --stacktrace -debug



##### Git操作:
```
SSL_ERROR_SYSCALL 错误:
    Git支持三种协议：git://、ssh://和http://，本来push的时候应该走ssh隧道的，设置了http代理走了http的代理，于是就提交不了
    git config --global --unset http.proxy
git remote add origin https://username:password@[url]
clone 出现Permission denied (publickey)  用https://

git remote add origin https://RoseHongXin:hong299110xin@github.com/RoseHongXin/RoseLibAndroid.git

```



#### lambda在低版本手机(比如华为4.4pad),通过this::****调用父类的方法,会报错(找不到方法?).

*  fastjson,  1.2.49 toJSONString  可能有bug.

#### activity给第三方app调用
```
manifest.xml intent-filter必须指定Category(一般指定android.intent.category.DEFAULT即可)
是否指定exported=true,没有实际效果(或许是测试包的原因)
通过setComponent("pkgName", "pkgName.clzName")一样可以打开activity.
uri方式打开activity还没测试.
```

> * 注意点
    Switch 的onClick事件, 回调的是selected参数, setSelected给该view的checked赋值
    非焦点view,还是正常的isChecked/setChecked操作状态!

> * 获取sha1
    keytool -exportcert -list -v -alias <your-key-name> -keystore <path-to-production-keystore>

# .java 2 .class   , jar包class修改.
```
javac -encoding UTF-8 -nowarn -cp wearable.jar;android.jar com\mediatek\wearableProfiles\WearableClientProfileManager.java

jar uvf wearable.jar com\mediatek\wearableProfiles\WearableClientProfileManager.class

```

# adb wifi 方式调试
```
adb tcpip 5555
adb connect 192.168.15.152  PC和设备在同一网段
adb kill-server

adb shell 进入shell模式
```

# SHA1
```
keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
```