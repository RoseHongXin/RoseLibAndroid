# RoseLibAndroid

- colorButtonNormal, colorPressedHighlight, [colorControlHighlight]   用于button的颜色样式显示
- colorControlNormal, colorControlActivated 用于TabSwitch颜色样式显示

- greenDao生成dao层代码,CustomType的Convert必须显示import.(否则识别不到)


```
/// 设置用户信息
    ///
    /// - Parameters:
    ///   - type: 1、个人信息2、目标设置3、绑定设备
    ///   - params: body数据
    ///   - responseHandle: 响应结果
    static func setUserInfo(type:Int, params: [String : Any], responseHandle: @escaping (IOEResponse) -> Void) -> Void {
        var paramsString = "type=\(type)"
        for key in params.keys {
            if let value = params[key] {
                paramsString += "&\(key)=\(value)"
            }
        }
        let url = fullURL(api: "setuser", params: paramsString)

        IOELibrary.request(url: url, method: .get, parameters: nil, responseHandle: responseHandle)
    }
```
    