## 支付组件 简要说明
该组件为封装了 微信，支付宝，银联支付， 一键快速集成，几行代码即可集成 微信，支付宝，银联支付。

## 示例:
![image](http://git.oschina.net/liangzc/libPaySdk/raw/master/art/1.jpg)

![image](http://git.oschina.net/liangzc/libPaySdk/raw/master/art/2.jpg)

![image](http://git.oschina.net/liangzc/libPaySdk/raw/master/art/alipay.jpg)

![image](http://git.oschina.net/liangzc/libPaySdk/raw/master/art/uppau.jpg)

![image](http://git.oschina.net/liangzc/libPaySdk/raw/master/art/4.jpg)

![image](http://git.oschina.net/liangzc/libPaySdk/raw/master/art/uppay_success.jpg)


# 测试账号：
1、银联支付：
提供测试使用卡号、手机号信息（此类信息仅供测试，不会发生正式交易）
招商银行借记卡：6226090000000048
    手机号：18100000000
    密码：111101
    短信验证码：123456（先点获取验证码之后再输入）
    证件类型：01身份证
    证件号：510265790128303
    姓名：张三

华夏银行贷记卡：6226388000000095
    手机号：18100000000
    cvn2：248
    有效期：1219
    短信验证码：123456（先点获取验证码之后再输入）
    证件类型：01身份证
    证件号：510265790128303
    姓名：张三

------------------------------------

## 接入指南：
1、导入libSdk 依赖工程

2、配置 AndroidManifest文件（配置内容，请看下文，此处支持 两种方式来配置 第三方支付 参数【①可以在AndroidManifest 对应的meta-data 配置；②支持在代码中配置；选其一即可】）

3、项目中实际使用支付：具体使用看下文 ---> 调起支付 。

------
请配置正确的参数，否则支付宝和微信 会出现无法调起的情况。
----

# //配置 AndroidManifest（只需配置如下参数）


        <!-- 微信支付 参数 appid， 需要替换成你自己的 -->
        <meta-data
            android:name="WXPAY_APP_ID"
            android:value="替换成自己的 app id" >
        </meta-data>
        <meta-data
            android:name="WXPAY_MCH_ID"
            android:value="替换成自己的   MCH_ID" >
        </meta-data>
        <meta-data
            android:name="WXPAY_API_KEY"
            android:value="替换成自己的 api key" >
        </meta-data>
        <!-- 微信支付 参数 end  需要替换成你自己的 -->


        <!-- 支付宝 参数 appid， 需要替换成你自己的 -->  //如果是 超过10位数字，要在前边加 ,Eg: \0223987667567887653
        <meta-data
            android:name="ALIPAY_PARTNER_ID"
            android:value="替换成自己的 partenr id" >
        </meta-data>
        <meta-data
            android:name="ALIPAY_SELLER_ID"
            android:value="替换成自己的 seller id" >
        </meta-data>

        <meta-data
            android:name="ALIPAY_PRIVATE_KEY"
            android:value="替换成自己的 private key" >
        </meta-data>

        <meta-data
            android:name="ALIPAY_PUBLIC_KEY"
            android:value="替换成自己的 public key" >
        </meta-data>
        <!-- 支付宝 参数 end  需要替换成你自己的 -->

# // 初始化支付组件
		PayAgent payAgent = PayAgent.getInstance();
		payAgent.setDebug(true);
		
	// 代码初始化 参数， 此处针对场景，所有参数有 自己app server保管的时候，动态的支付配置下发参数
		payAgent.initAliPayKeys(partnerId, sellerId, privateKey, publicKey);
		payAgent.initWxPayKeys(appId, mchId, appKey)
		//		初始化 银联支付 所需的 验签 参数
		//payAgent.initUpPayKeys(PublicKeyPMModulus, publicExponent, PublicKeyProductModulus);
	// 代码动态初始化为 可选 
		
##		payAgent.initPay(this);  



# // 调起支付 
        PayAgent.getInstance().onPay(payType, this, payInfo,
				new OnPayListener() {

					@Override
					public void onStartPay() {
						
						progressDialog.setTitle("加载中。。。");
						progressDialog.show();
					}

					@Override
					public void onPaySuccess() {
						
						Toast.makeText(MainActivity.this,"支付成功！", 1).show();
						
						if (null != progressDialog) {
							progressDialog.dismiss();
						}

					}

					@Override
					public void onPayFail(String code, String msg) {
						Toast.makeText(MainActivity.this,
								"code:" + code + "msg:" + msg, 1).show();
						Log.e(getClass().getName(), "code:" + code + "msg:" + msg);
						
						if (null != progressDialog) {
							progressDialog.dismiss();
						}
					}
				});
				
				
				
# 

## 支付参数说明：
PayType:
支付的支付方式，目前支持：
- 1、PayAgent.PayType.WECHATPAY（微信支付）；
- 2、PayAgent.PayType.ALIPAY（支付宝）；
- 3、PayAgent.PayType.UPPAY（银联）。

Activity:
调起支付的 Activity

PayInfo：

	/** 商品名称*/
    private String subject;

    /** 商品详细信息  商品的标题/交易标题/订单标题/订单关键字等。该参数最长为128个汉字*/
    private String body;

    /** 商品价格*/
    private String price;

    /** 商品订单号*/
    private String orderNo;
    
    /** 支付通知地址*/
    private String notifyUrl;
    
OnPayListener：
支付监听器： 
- onStartPay()  开始支付，可以在此做 支付前准备提示
- onPaySuccess();  支付成功
- onPayFail(String code, String msg); 支付失败，code和msg 均为第三方原样返回 

# 配置第三方参数说明：
- 1、支付宝：
- 


-----------------------------------------------------------------------------------------------------------------------------------------------
# 注意： 
- 1、支付宝支付，调用支付宝时， 所有参数为必须项
- 2、微信支付，orderNo 为必须项
- 3、银联支付时，orderNo 为必须项
-4、关于支付后，通知回调，只有支付宝是 在客户端手动设置，其余都是在 后台配置。

## 注意事项：
- 1、当测试时，可以使用Debug模式，开启方式为：
PayAgent payAgent = PayAgent.getInstance();
payAgent.setDebug(true);

- 2、调试模式(非正式环境，目前只有 银联)：
PayAgent payAgent = PayAgent.getInstance();
payAgent.setOnlieMode(false);


# 版本說明：

- 1、银联支付：3.3.2
- 2、支付宝：
- 3、微信：

# 更新日志：
- 2016.05.27更新：
- 1、整体迁移到AndroidStudio
- 2、添加第三方支付混淆配置
- 3、使用Gradle简化配置，只需在工程中配置支付参数（appkey等）

## 其他说明：

- 银联支付平台官网：https://open.unionpay.com/ajweb/product/detail?id=3
- 支付宝平台官网：https://doc.open.alipay.com/doc2/detail?treeId=54&articleId=103419&docType=1
- 微信支付平台官网：https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317784&token=&lang=zh_CN
- https://pay.weixin.qq.com/wiki/doc/api/app.php?chapter=11_1