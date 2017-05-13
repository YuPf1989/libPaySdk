package net.lbh.pay;

import net.lbh.pay.uppay.UpPayHelper;
import net.lbh.pay.wxpay.WechatPayHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

public class PaymentActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = PaymentActivity.class.getName();

	private IWXAPI api;
	
	private PayAgent payAgent = PayAgent.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handlePayInit();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		if (payAgent.getCurrentPayType() == PayAgent.PayType.WECHATPAY) {
			api.handleIntent(intent, this);
			WechatPayHelper.handleIntent(intent, this);
		} else if (payAgent.getCurrentPayType() == PayAgent.PayType.UPPAY) {
			UpPayHelper.handleIntent(intent, this);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (payAgent.getCurrentPayType() == PayAgent.PayType.UPPAY) {
			UpPayHelper.onActivityResult(requestCode, resultCode, data);
			this.finish();
		}
		
	}

	@Override
	public void onReq(BaseReq req) {
		L.d(TAG, " === wxPay onReq " + req.toString() + " === ");
		WechatPayHelper.handleonReq(req);
	}

	@Override
	public void onResp(BaseResp resp) {
		L.d(TAG, " ==== wxPay onResp ===" + resp.errStr +";code=" + String.valueOf(resp.errCode));
		WechatPayHelper.handleOnResp(resp);
		this.finish();
	}

	private void handlePayInit() {

		switch (payAgent.getCurrentPayType()) {

		case WECHATPAY:
			api = WXAPIFactory.createWXAPI(this, ConstantKeys.WxPay.APP_ID);

			api.handleIntent(getIntent(), this);
			break;
			
			
		case UPPAY:
			String orderInfo = getIntent().getStringExtra("orderInfo");
			if (TextUtils.isEmpty(orderInfo)) {
				
			}
			
//			activity  ——用于启动支付控件的活动对象
//			spId  ——保留使用，这里输入null
//			sysProvider ——保留使用，这里输入null
//			orderInfo   ——订单信息为交易流水号，即TN。 
//			mode   —— 银联后台环境标识，“00”将在银联正式环境发起交易,“01”将在银联测试环境发起交易
//			activity  ——用于启动支付控件的活动对象
//			spId  ——保留使用，这里输入null
//			sysProvider ——保留使用，这里输入null
//			orderInfo   ——订单信息为交易流水号，即TN。 
//			mode   —— 银联后台环境标识，“00”将在银联正式环境发起交易,“01”将在银联测试环境发起交易
			
			 UPPayAssistEx.startPayByJAR(this, PayActivity.class, null, null,
					 orderInfo, UpPayHelper.payMode);
			break;

		default:
			break;
		}
	}
}