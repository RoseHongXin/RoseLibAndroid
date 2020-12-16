##### Git操作:
```
SSL_ERROR_SYSCALL 错误
    Git支持三种协议：git://、ssh://和http://，本来push的时候应该走ssh隧道的，设置了http代理走了http的代理，于是就提交不了
    git config --global --unset http.proxy
git remote add origin https://username:password@[url]
clone 出现Permission denied (publickey)  用https://

git remote add origin https://RoseHongXin:hong299110huang@github.com/RoseHongXin/RoseLibAndroid.git

git checkout ./--fileName   舍弃本地修改[全部/指定文件]

出现443错误, git config --global http.proxy //  git config --global --unset http.proxy  看是否能解决

git clone --depth=1   xxx   慢,只拉最近一次commit,会快很多(完整的commit历史不会被拉下来)

git配置可直接修改 .git/config 文件
```

##### Svn操作:
- 已经加入版本管理的文件, 在.idea添加ignore path后,要commit一次,才会生效
