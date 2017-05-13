package net.lbh.pay;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 
 * Security information for Pay
 * <p>
 * alipay , wxpay
 * 
 * @author BaoHong.Li
 * @date 2015-7-16 上午11:35:03
 * @update (date)
 * @version V1.0
 */
public class ConstantKeys {

	private static boolean isInitAliPayKeys;
	private static boolean isInitWxPayKeys;
	private static boolean isInitUpPayKeys;

	private static final String TAG = ConstantKeys.class.getName();

	private static final String ALIPAY_PARTNER_ID = "ALIPAY_PARTNER_ID";
	private static final String ALIPAY_SELLER_ID = "ALIPAY_SELLER_ID";
	private static final String ALIPAY_PRIVATE_KEY = "ALIPAY_PRIVATE_KEY";
	private static final String ALIPAY_PUBLIC_KEY = "ALIPAY_PUBLIC_KEY";

	private static final String WXPAY_APP_ID = "WXPAY_APP_ID";
	private static final String WXPAY_MCH_ID = "WXPAY_MCH_ID";
	private static final String WXPAY_API_KEY = "WXPAY_API_KEY";

	private static final String UPPAY_PUBLICKEY_PM_MODULUS = "UPPAY_PUBLICKEY_PM_MODULUS";
	private static final String UPPAY_PUBLIC_EXPONENT = "UPPAY_PUBLIC_EXPONENT";
	private static final String UPPAY_PUBLIC_KEY_PRODUCT_MODULUS = "UPPAY_PUBLIC_KEY_PRODUCT_MODULUS";

	private ConstantKeys() {
	}

	/**
	 * 
	 * 通过 AndroidManifest.xml 配置参数初始化配置参数
	 * 
	 * @return boolean
	 * @autour BaoHong.Li
	 * @date 2015-7-17 上午10:02:41
	 * @update (date)
	 */
	public static boolean initKeys(Activity activity) {
		if (!isInitAliPayKeys) {
			initAliPayKeys(getMetaData(activity, ALIPAY_PARTNER_ID),
					getMetaData(activity, ALIPAY_SELLER_ID),
					getMetaData(activity, ALIPAY_PRIVATE_KEY),
					getMetaData(activity, ALIPAY_PUBLIC_KEY));
		}

		if (!isInitWxPayKeys) {
			initWxPayKeys(getMetaData(activity, WXPAY_APP_ID),
					getMetaData(activity, WXPAY_MCH_ID),
					getMetaData(activity, WXPAY_API_KEY));
		}

		if (!isInitUpPayKeys) {
			initUpPayKeys(getMetaData(activity, UPPAY_PUBLICKEY_PM_MODULUS),
					getMetaData(activity, UPPAY_PUBLIC_EXPONENT),
					getMetaData(activity, UPPAY_PUBLIC_KEY_PRODUCT_MODULUS));

		}
		return true;
	}

	// 在application应用<meta-data>元素。
	public static String getMetaData(Activity activity, String key) {

		ApplicationInfo appInfo = null;
		String value = "";
		try {
			appInfo = activity.getPackageManager().getApplicationInfo(
					activity.getPackageName(), PackageManager.GET_META_DATA);
			value = String.valueOf(appInfo.metaData.get(key));
			L.d(TAG, "meta-data :key: " + key);
			L.d(TAG, "  meta-data :value: " + value);
		} catch (NameNotFoundException e) {
			L.w(TAG, e.getMessage());
		}

		return value;
	}

	/**
	 * 
	 * 初始化 支付宝 配置参数
	 * 
	 * @param partnerId
	 * @param sellerId
	 * @param privateKey
	 * @param publicKey
	 * @return boolean
	 * @autour BaoHong.Li
	 * @date 2015-7-17 上午9:51:55
	 * @update (date)
	 */
	public static boolean initAliPayKeys(String partnerId, String sellerId,
			String privateKey, String publicKey) {
		AliPay.PARTNER_ID = partnerId;
		AliPay.SELLER_ID = sellerId;
		AliPay.PRIVATE_KEY = privateKey;
		AliPay.ALIPAY_PUBLIC_KEY = publicKey;
		isInitAliPayKeys = true;
		return true;

	}

	/**
	 * 初始化 微信支付 配置参数
	 * 
	 * @param partnerId
	 * @param mchId
	 * @param privateKey
	 * @param publicKey
	 * @return boolean
	 * @autour BaoHong.Li
	 * @date 2015-7-17 上午9:58:46
	 * @update (date)
	 */
	public static boolean initWxPayKeys(String appId, String mchId,
			String appKey) {
		WxPay.APP_ID = appId;
		WxPay.MCH_ID = mchId;
		WxPay.API_KEY = appKey;
		isInitWxPayKeys = true;
		return true;

	}

	public static boolean initUpPayKeys(String PublicKeyPMModulus,
			String publicExponent, String PublicKeyProductModulus) {
		UpPay.PublicKeyPMModulus = PublicKeyPMModulus;
		UpPay.PublicKeyProductModulus = PublicKeyProductModulus;
		UpPay.publicExponent = publicExponent;
		isInitUpPayKeys = true;
		return true;

	}

	/**
	 * alipay keys
	 * 
	 * @author BaoHong.Li
	 * @date 2015-7-16 上午11:37:45
	 * @update (date)
	 * @version V1.0
	 */
	public static class AliPay {

		/**
		 * 合作者身份 ID parameter "partner" in pay interface
		 */
		public static String PARTNER_ID = "";

		/**
		 * 企业支付宝帐号 parameter "seller_id" in pay interface
		 */
		public static String SELLER_ID = "";

		/**
		 * 参数编码字符集，目前只支持 utf-8 parameter "_input_charset" in pay interface
		 */
		public static String INPUT_CHARSET = "utf-8";

		/**
		 * 签名方式，目前只支持 RSA parameter "sign_type" in pay interface
		 */
		public static String SIGN_TYPE = "RSA";

		/**
		 * 商户私钥 parameter "sign" in pay interface
		 */
		public static String PRIVATE_KEY = "";

		public static String ALIPAY_PUBLIC_KEY = "";
	}

	/**
	 * wx pay keys
	 * 
	 * @author BaoHong.Li
	 * @date 2015-7-16 上午11:37:55
	 * @update (date)
	 * @version V1.0
	 */
	public static class WxPay {
		/**
		 * appid 请同时修改 androidmanifest.xml里面，.PayActivityd里的属性
		 * <p>
		 * <b><i> data android:scheme="wxb4ba3c02aa476ea1"<i><b> 为新设置的appid
		 * */
		public static String APP_ID = "";

		/*** 商户号 */
		public static String MCH_ID = "";

		/*** API密钥，在商户平台设置 */
		public static String API_KEY = "";

	}

	/**
	 * UpPay keys
	 * <p>
	 * PublicKeyPMModulus 换成PM环境商户验签的公钥模数
	 * </p>
	 * <p>
	 * PublicKeyProductModulus 换成生产环境商户验签的公钥模数
	 * </p>
	 * <p>
	 * publicExponent 默认值：　65537
	 * </p>
	 * 
	 * @author BaoHong.Li
	 * @date 2016-3-3 下午4:54:16
	 * @update (date)
	 * @version V1.0
	 */
	public static class UpPay {
		public static String PublicKeyPMModulus = "";
		public static String publicExponent = "65537";
		public static String PublicKeyProductModulus = "";

	}

}
