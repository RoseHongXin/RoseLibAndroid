##### 配置:
```
${Studio}/bin/ idea.properties 配置config和system路径
${Studio}/bin/ studio64.exe.vmoptions 配置运行环境

${gradle} gradle.properties  配置gradle路径, 环境变量配置  GRADLE_USER_HOME

```

#### Gradle编译
```
app:compileDebugJavaWithJavac这个错误,看是不是资源文件有重复,drawable和mipmap
gradlew compileDebug --stacktrace gradlew compileDebug --stacktrace -info gradlew compileDebug --stacktrace -debug
gradlew processDebugManifest --stacktrace   当出现Manifest merger failed with multiple errors, see logs,可以看到详细信息
gradlew compileDebugJavaWithJavac
gradlew:app:dependencies 查看依赖
```
> shrinkResources 不能用于library
> app无法编译运行检查一下gradle各个version的配置

##### .java 2 .class   , jar包class修改.
```
javac -encoding UTF-8 -nowarn -cp wearable.jar;android.jar com\mediatek\wearableProfiles\WearableClientProfileManager.java

jar uvf wearable.jar com\mediatek\wearableProfiles\WearableClientProfileManager.class

```

##### adb wifi 方式调试
```
adb tcpip 5555
adb connect 192.168.15.152  PC和设备在同一网段
adb kill-server

adb shell 进入shell模式
```

##### SHA1
```
keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
keytool -list -v -keystore "F:/AndroidPro/AndroidSigned" -alias smarthome -storepass dan199858 -keypass dan199858
keytool -list -v -keystore "F:/AndroidPro/AndroidSigned" -alias smartwearable -storepass dan199858 -keypass hongxin
```

- 169.254.X.X是 Windows操作系统 在 DHCP 信息租用失败时自动给客户机分配的IP地址.
- adb shell logcat -v time > logcat.txt //console日志到文件

#### Apk签名打包命令
jarsigner -verbose -keystore AndroidSigned *.apk ammeter
