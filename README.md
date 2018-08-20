# RoseLibAndroid
Android开发中的一些封装
-








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
```
    