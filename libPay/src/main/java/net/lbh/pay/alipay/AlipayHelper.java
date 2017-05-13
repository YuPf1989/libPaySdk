package net.lbh.pay.alipay;

import net.lbh.pay.L;
import net.lbh.pay.PayInfo;
import net.lbh.pay.OnPayListener;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

public class AlipayHelper {

	public interface OnPayResultListener {
		public void onPayResult(String code, String msg);
	}

	protected static final int RQF_PAY = 0;
	private String payInfo;

	private OnPayListener onPayResultListener;

	private Handler handler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case RQF_PAY:
				if (onPayResultListener != null) {
					Result result = new Result((String) msg.obj);
					result.parseResult();
					L.i(AlipayHelper.class.getName(), "pay code:" + result.resultCode);
					if (result.isSignOk && (TextUtils.equals(result.resultCode, "9000"))) {
						onPayResultListener.onPaySuccess();
					} else if (TextUtils.isEmpty(result.resultMsg)) {
						onPayResultListener.onPayFail(result.resultCode, "网络异常，请刷新我的订单再试");
						return;
					} else {
						// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						// 支付结果确认中
						if (TextUtils.equals(result.resultCode, "8000")) {
							onPayResultListener.onPayFail(result.resultCode, result.resultMsg);
							// 支付失败
						} else {
							onPayResultListener.onPayFail(result.resultCode, result.resultMsg);
						}

					}

				}
				break;

			default:
				break;
			}
		}

	};

	public void setOnlieMode(boolean isOnlieMode) {

	}

	public void pay(final Activity activity, PayInfo info, OnPayListener l) {

		this.onPayResultListener = l;

		PayUrlGenerator generator = new PayUrlGenerator(info);
		payInfo = generator.generatePayUrl();

		new Thread(new Runnable() {

			@Override
			public void run() {

				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (null != onPayResultListener) {
							onPayResultListener.onStartPay();
						}
					}
				});

				PayTask aliPay = new PayTask(activity);
				// 设置为沙箱模式，不设置默认为线上环境�?
				// aliPay.setSandBox(true);

				String result = aliPay.pay(payInfo);
				L.i(AlipayHelper.class.getName(), "pay result :" + result);
				Message msg = new Message();
				msg.what = RQF_PAY;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		}).start();
	}
}
