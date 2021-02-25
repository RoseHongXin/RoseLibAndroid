###### realm
* realm dao如果有写构造方法,一定要指明一个空参构造方法,否则无法编译
* realm RealmSchema.RealmObjectSchema.addField  小写类型是nullable,大写类型是required.要注意
>* 10.*.* 版本後
>* 不需要指定proguard
>* 如果library使用,要添加@RealmModule(library = true, allClasses = true)類
>* app引用library,不使用realm,要指定RealmModule,並且要有dao存在,否則項目build會有問題  


###### okhttp
* okhttp3, retryOnConnectionFailure(false),如果服务端被kill了,再次请求,会报一个[java.net.SocketException: sendto failed: ECONNRESET (Connection reset by peer)],只有设置成true,才不会出现这种报错!
* okhttp  builder.addHeader("Connection", "close");  避免unexpected end of stream on ** 这个错误


###### 其他
> //计步轨迹Googlemap计算距离向下兼容,某些低版本手机,有些jar没有
* 一般某些聊天的SDK都依赖于libsqlite.so（环信、融云），不过由于这个包从来没有变化，使用的是系统默认提供的(/system/lib/)。在Android 6.x及以下的平台可以运行。
 Android7.x执行更严格的安全检查，禁止使用系统目录的内容。所以如果希望在7.x以上版本，需要把系统目录的libsqlite.so拷贝出来，也放在自己app对应指令目录下。

###### linux
* 用A腳本運行B,如果B中有調用sh,如果A未正常退出,B中的sh調用也會有問題(可能導致B的正常運行)
