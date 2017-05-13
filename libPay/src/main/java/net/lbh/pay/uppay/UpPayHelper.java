package net.lbh.pay.uppay;

import org.json.JSONException;
import org.json.JSONObject;

import net.lbh.pay.L;
import net.lbh.pay.OnPayListener;
import net.lbh.pay.PayInfo;
import net.lbh.pay.PaymentActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;


/**
 * 
 * 银联支付 helper
 * 
 * @author BaoHong.Li
 * @date 2015-7-21 上午11:38:59
 * @update (date)
 * @version V1.0
 */
public class UpPayHelper {

	private static final String TAG = UpPayHelper.class.getName();

	private static OnPayListener mListener;
	
	 // “00” – 银联正式环境
	// “01” – 银联测试环境，该环境中不发生真实交易
	public static String payMode ="01";

	public void setOnlieMode(boolean isOnlieMode){
		if (isOnlieMode) {
			payMode = "00";
		}else {
			payMode = "01";
		}
	}
	
	public void pay(final Activity activity, PayInfo info,
			OnPayListener listener) {
		pay(activity, info, listener, payMode);
	}
	
	
	public void pay(final Activity activity, PayInfo info,
			OnPayListener listener , String mode) {

		
		mListener = listener;
		 
		PayUrlGenerator payUrlGenerator = new PayUrlGenerator(info);
		String orderInfo = payUrlGenerator.genPayOrder();
		
		if (null != mListener) {
			mListener.onStartPay();
		}
		
		//pay
		Intent intent = new Intent(activity,PaymentActivity.class);
		intent.putExtra("orderInfo", orderInfo);
		activity.startActivity(intent);
	}


	/**
	 * 接收 银联支付回调结果 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 * @return void
	 * @autour BaoHong.Li
	 * @date 2015-7-21 上午11:41:19
	 * @update (date)
	 */
	public static void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		
        /*************************************************
         * 步骤3：处理银联手机支付控件返回的支付结果
         ************************************************/
        if (data == null) {
            return;
        }

        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        
        if (!TextUtils.isEmpty(str)) {
			L.d(TAG, str);
		}
        if (str.equalsIgnoreCase("success")) {
        	
            // 支付成功后，extra中如果存在result_data，取出校验
            // result_data结构见c）result_data参数说明
            if (data.hasExtra("result_data")) {
                String result = data.getExtras().getString("result_data");
                if (!TextUtils.isEmpty(result)) {
        			L.d(TAG, result);
        		}
                try {
                    JSONObject resultJson = new JSONObject(result);
                    String sign = resultJson.getString("sign");
                    String dataOrg = resultJson.getString("data");
                    // 验签证书同后台验签证书
                    // 此处的verify，商户需送去商户后台做验签
//                    2016.4.14 银联更新，去掉客户端验签。
//                    boolean ret = UPPayRSAUtil.verify(dataOrg, sign, payMode);
                    boolean ret = verify(dataOrg, sign, payMode);
                    if (ret) {
                        // 验证通过后，显示支付结果
                        msg = "支付成功！";
                    	if (null != mListener) {
//                			mListener.onPaySuccess(String.valueOf(0),msg);
                			mListener.onPaySuccess();
                		}
                    } else {
                        // 验证不通过后的处理
                        // 建议通过商户后台查询支付结果
                        msg = "支付失败！";
                        if (null != mListener) {
                			mListener.onPayFail(String.valueOf(0),msg);
                		}
                    }
                } catch (JSONException e) {
                	L.w(TAG, e);
                }
            } else {
                // 未收到签名信息
                // 建议通过商户后台查询支付结果
                msg = "支付成功！";
                if (null != mListener) {
//        			mListener.onPaySuccess(String.valueOf(0),msg);
        			mListener.onPaySuccess();
        		}
            }
        
        } else if (str.equalsIgnoreCase("fail")) {
        	  msg = "支付失败！";
        	if (null != mListener) {
    			mListener.onPayFail(String.valueOf(0),msg);
    		}
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
            if (null != mListener) {
            	mListener.onPayFail(String.valueOf(0),msg);
            }
        }
	

	}

	public static void handleIntent(Intent intent, Context context) {

	}

	/// 2016.4.14 银联更新，去掉客户端验签。
  private static boolean verify(String msg, String sign64, String mode) {
        // 此处的verify，商户需送去商户后台做验签
        return true;

    }

}
