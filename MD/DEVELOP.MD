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
```



#### lambda在低版本手机(比如华为4.4pad),通过this::****调用父类的方法,会报错(找不到方法?).