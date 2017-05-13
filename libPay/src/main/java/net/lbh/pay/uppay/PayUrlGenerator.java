package net.lbh.pay.uppay;

import android.text.TextUtils;
import net.lbh.pay.PayInfo;


/**
 *  
 * 构建支付参数，主要是银行返回的 TN，即 ：订单信息为交易流水号。 
 * @author BaoHong.Li
 * @date 2015-7-16 下午4:27:02
 * @update (date)
 * @version V1.0
 */
public class PayUrlGenerator {

	private PayInfo payInfo;


	public PayUrlGenerator(PayInfo payInfo) {
		this.payInfo = payInfo;
	}
	
	public String genPayOrder(){
		
		validatePayInfo(payInfo);
		
		return payInfo.getOrderNo();
	}


	
	/**
	 *验证 支付参数的有效性 
	* @param payInfo
	* @return void
	* @autour BaoHong.Li
	* @date 2015-7-17 上午10:44:11 
	* @update (date)
	 */
	private void validatePayInfo(PayInfo payInfo) {

		if (TextUtils.isEmpty(payInfo.getOrderNo())) {
			throw new IllegalArgumentException(" payInfo.orderNo is  null !");
		}

//		if (TextUtils.isEmpty(payInfo.getBody())) {
//			throw new IllegalArgumentException(" payInfo.body is  null !");
//		}

//		if (TextUtils.isEmpty(payInfo.getSubject())) {
//			throw new IllegalArgumentException(" payInfo.subject is  null !");
//		}
		
//		if (TextUtils.isEmpty(payInfo.getNotifyUrl())) {
//			throw new IllegalArgumentException(" payInfo.notifyUrl is  null !");
//		}
		
	}

}
