![image](https://github.com/meteorOSS/wechat-pay/assets/61687266/862f4901-828c-4f39-aa00-7c9cb302d584)


基于 [WeChatBc](https://github.com/meteorOSS/WeChatBc) 实现

## 功能

监听二维码收款进行转发，可一定程度作为收款方案

通过以下地址请求订单是否存在

> /query?orderID=备注

不存在时返回状态码 `-1200`

查询成功示例:

``` json
{
"code": 200,
"data": {
"amount": 1,
"notes": "买一瓶可乐",
"time": 1709116512918
},
"message": "success"
}
```


### 配置文件

``` yaml
port: 8080 # 挂载端口号
```

## 使用方法
放入wechatbc的plugins文件夹，随后重启服务
