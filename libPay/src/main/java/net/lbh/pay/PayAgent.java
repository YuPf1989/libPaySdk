package net.lbh.pay;

import net.lbh.pay.alipay.AlipayHelper;
import net.lbh.pay.uppay.UpPayHelper;
import net.lbh.pay.wxpay.WechatPayHelper;
import android.app.Activity;
import android.os.Looper;

/**
 * 支付代理类
 * 
 * @author BaoHong.Li
 * @date 2015-7-16 上午11:23:48
 * @update (date)
 * @version V1.0
 */
public class PayAgent {

	public PayType currentPayType;

	private boolean isInit;

	private static volatile PayAgent instance;

	/*** 支付方式 */
	public enum PayType {
		/** 支付宝 */
		ALIPAY,
		/** 微信 */
		WECHATPAY,
		/** 银联支付 */
		UPPAY
	}

	private AlipayHelper mAlipayHelper;
	private WechatPayHelper mWechatpayHelper;
	private UpPayHelper mUpPayHelper;

	private PayAgent() {

	}

	public static PayAgent getInstance() {

		if (null == instance) {

			synchronized (PayAgent.class) {

				if (null == instance) {
					instance = new PayAgent();
				}
			}
		}

		return instance;
	}

	public AlipayHelper getAlipayHelper() {
		if (null == mAlipayHelper) {
			mAlipayHelper = new AlipayHelper();
		}
		return mAlipayHelper;
	}

	public WechatPayHelper getWechatpayHelper() {
		if (null == mWechatpayHelper) {
			mWechatpayHelper = new WechatPayHelper();
		}
		return mWechatpayHelper;
	}

	public UpPayHelper getUpPayHelper() {
		if (null == mUpPayHelper) {
			mUpPayHelper = new UpPayHelper();
		}
		return mUpPayHelper;
	}

	public PayType getCurrentPayType() {
		return currentPayType;
	}

	/***
	 * set debug modle
	 * 
	 * @param debug
	 *            [true or false]
	 * @return void
	 * @autour BaoHong.Li
	 * @date 2015-7-16 上午11:26:07
	 * @update (date)
	 */
	public void setDebug(boolean debug) {
		L.isDebug = debug;
	}

	/**
	 * 
	 * 设置 为正式环境或测试环境【true , false】
	 * 
	 * @return void
	 * @autour BaoHong.Li
	 * @date 2016-3-3 下午2:54:10
	 * @update (date)
	 */
	public void setOnlieMode(boolean isOnlieMode) {
		getAlipayHelper().setOnlieMode(isOnlieMode);
		getUpPayHelper().setOnlieMode(isOnlieMode);
		getWechatpayHelper().setOnlieMode(isOnlieMode);
	}

	/**
	 * 初始化 支付组件
	 * 
	 * @param activity
	 * @return void
	 * @autour BaoHong.Li
	 * @date 2015-7-16 下午5:04:36
	 * @update (date)
	 */
	public synchronized boolean initPay(Activity activity) {
		if (isInit) {
			return true;
		}

		boolean success = true;
		success &= ConstantKeys.initKeys(activity);
		success &= getWechatpayHelper().registerWechatApi(activity);

		isInit = true;
		return success;
	}

	/**
	 * 
	 * 初始化支付宝 所需的appid ,appkey ..
	 * 
	 * @return void
	 * @autour BaoHong.Li
	 * @date 2015-7-17 上午9:43:53
	 * @update (date)
	 */
	public boolean initAliPayKeys(String partnerId, String sellerId,
			String privateKey, String publicKey) {
		return ConstantKeys.initAliPayKeys(partnerId, sellerId, privateKey,
				publicKey);
	}

	/**
	 * 
	 * 初始化微信支付 所需的appid ,appkey ..
	 * 
	 * @return void
	 * @autour BaoHong.Li
	 * @date 2015-7-17 上午9:43:53
	 * @update (date)
	 */
	public boolean initWxPayKeys(String appId, String mchId, String appKey) {
		return ConstantKeys.initWxPayKeys(appId, mchId, appKey);
	}

	/**
	 *
	 * 初始化 银联支付 所需的 验签 参数
	 * 
	 * @param PublicKeyPMModulus
	 * @param publicExponent
	 * @param PublicKeyProductModulus
	 * @return boolean
	 * @autour BaoHong.Li
	 * @date 2016-3-3 下午5:11:48
	 * @update (date)
	 */
	public boolean initUpPayKeys(String PublicKeyPMModulus,
			String publicExponent, String PublicKeyProductModulus) {
		return ConstantKeys.initUpPayKeys(PublicKeyPMModulus, publicExponent,
				PublicKeyProductModulus);
	}

	/**
	 * 
	 * 支付宝 支付 <b>[ 同步调用 <i>即在主（ui）线程调用</i>]</b>
	 * 
	 * @param activity
	 *            : 调起支付 所在的 activity
	 * @param payInfo
	 *            : 支付信息 [订单号，支付金额，商品名称，支付服务器回调地址..]
	 * @return void
	 * @autour BaoHong.Li
	 * @date 2015-7-16 下午2:40:23
	 * @update (date)
	 */
	public void payOfAliPay(Activity activity, PayInfo payInfo,
			OnPayListener listener) {
		onPay(PayAgent.PayType.ALIPAY, activity, payInfo, listener);
	}

	/**
	 * 
	 * 微信支付 <b>[ 同步调用<i> 即在主（ui）线程调用</i>]</b>
	 * 
	 * @param activity
	 *            : 调起支付 所在的 activity
	 * @param payInfo
	 *            : 支付信息 [订单号，支付金额，商品名称，支付服务器回调地址..]
	 * @return void
	 * @autour BaoHong.Li
	 * @date 2015-7-16 下午2:40:38
	 * @update (date)
	 */
	public void payOfWechatPay(Activity activity, PayInfo payInfo,
			OnPayListener listener) {
		onPay(PayAgent.PayType.WECHATPAY, activity, payInfo, listener);
	}

	/**
	 * 银联支付<b>[ 同步调用<i> 即在主（ui）线程调用</i>]</b>
	 * 
	 * @param activity
	 * @param payInfo
	 *            【orderNo,tn 银行流水号】
	 * @param listener
	 * @return void
	 * @autour BaoHong.Li
	 * @date 2015-7-21 下午2:27:29
	 * @update (date)
	 */
	public void payOfUpPay(Activity activity, PayInfo payInfo,
			OnPayListener listener) {
		onPay(PayAgent.PayType.UPPAY, activity, payInfo, listener);
	}

	/**
	 * 调起 支付 <b>[ 同步调用<i>即在主（ui）线程调用</i>]</b>
	 * 
	 * @param payType
	 * @param activity
	 *            调起支付 所在的 activity
	 * @param payInfo
	 *            支付信息 [订单号，支付金额，商品名称，支付服务器回调地址..]
	 *            <p>
	 *            <i>PayInfo -> price
	 *            微信:交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数。</i>
	 *            </p>
	 * @param listener
	 *            支付回调
	 * @return void
	 * @autour BaoHong.Li
	 * @date 2015-7-16 下午3:51:55
	 * @update (date)
	 */
	public void onPay(PayType payType, Activity activity, PayInfo payInfo,
			OnPayListener listener) {

		currentPayType = payType;

		if (!isInit) {
			initPay(activity);
			// throw new
			// IllegalArgumentException(" please call initPay method !");
		}

		if (null == payInfo) {
			throw new IllegalArgumentException(" payinfo  is null!");
		}

		if (null == activity) {
			throw new IllegalArgumentException(" Activity  is null!");
		}

		if (Looper.myLooper() != Looper.getMainLooper()) {
			throw new IllegalArgumentException(Thread.currentThread().getName()
					+ "'. " + "onPay methods must be called on the UI thread. ");
		}

		switch (payType) {

			case ALIPAY :
				getAlipayHelper().pay(activity, payInfo, listener);
				break;

			case WECHATPAY :
				getWechatpayHelper().pay(activity, payInfo, listener);
				break;

			case UPPAY :
				getUpPayHelper().pay(activity, payInfo, listener);
				break;
			default :
				throw new IllegalArgumentException(
						" payType is ALIPAY or WXPAY ");
		}

	}
}
