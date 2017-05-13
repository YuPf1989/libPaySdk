package net.lbh.pay.wxpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.lbh.pay.ConstantKeys;
import net.lbh.pay.L;
import net.lbh.pay.OnPayListener;
import net.lbh.pay.PayInfo;

public class WechatPayHelper {

	private static final String TAG = WechatPayHelper.class.getName();
	
	private static OnPayListener mListener;

	private IWXAPI msgApi;

	public void setOnlieMode(boolean isOnlieMode){
		 
	}
	
	public void pay(final Activity activity, PayInfo info, OnPayListener listener) {
		
		mListener = listener;
		if (null == msgApi) {
			registerWechatApi(activity);
		}
		
		PayUrlGenerator payUrlGenerator = new PayUrlGenerator(info);
		PayReq req = payUrlGenerator.genPayReq();
		
		if (null !=mListener) {
			mListener.onStartPay();
		}
		
		msgApi.sendReq(req);
	}

	/**
	 * 注册 微信sdk 到app 
	* @param activity
	* @return void
	* @autour BaoHong.Li
	* @date 2015-7-17 上午9:05:13 
	* @update (date)
	 */
	public boolean registerWechatApi(final Activity activity) {
		if (null == msgApi) {
			msgApi = WXAPIFactory.createWXAPI(activity, null);
		}
		return msgApi.registerApp(ConstantKeys.WxPay.APP_ID);
	}

	/**
	 * 接收 支付回调
	 * 
	 * @param resp
	 * @return void
	 * @autour BaoHong.Li
	 * @date 2015-7-16 下午4:02:28
	 * @update (date)
	 */
	public static void handleOnResp(BaseResp resp) {

		L.d(TAG, " ====  handleOnResp ,resp:" +resp.toString() +" === ");
		
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX && resp.errCode ==0) {

			if (null !=mListener) {
//				mListener.onPaySuccess(String.valueOf(resp.errCode), resp.errStr);
				mListener.onPaySuccess();
			}
			
	//支付失败
		}else {
			if (null !=mListener) {
				mListener.onPayFail(String.valueOf(resp.errCode), resp.errStr);
			}
		}

	}

	/**
	 * 
	 * @param req
	 * @return void
	 * @autour BaoHong.Li
	 * @date 2015-7-16 下午4:02:41
	 * @update (date)
	 */
	public static void handleonReq(BaseReq req) {
		L.d(TAG, " ====== handleonReq =====");
	}

	public static void handleIntent(Intent intent, Context context) {

	}

}
