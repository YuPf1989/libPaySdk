package com.example.testpaysdk;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import net.lbh.pay.OnPayListener;
import net.lbh.pay.PayAgent;
import net.lbh.pay.PayAgent.PayType;
import net.lbh.pay.PayInfo;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements
		View.OnClickListener {

	private ProgressDialog progressDialog;
	private Button aliPayBtn, wxPayBtn, upPayBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// 初始化 支付组件
		PayAgent payAgent = PayAgent.getInstance();
		payAgent.setDebug(true);
		
//		payAgent.initAliPayKeys(partnerId, sellerId, privateKey, publicKey)
//		payAgent.initWxPayKeys("wx5e0d459762737b8c", "1264771501", "HP22EVMVEW7Z6ZBNZ56MKSB2Y3LP2T4H");
//		payAgent.initWxPayKeys("wx79e95c3cb4556117", "1264869501", "PUGMB239LWENZEDMW9RTTDWRZT83JWYL");
		
//		初始化 银联支付 所需的 验签 参数
//		payAgent.initUpPayKeys(PublicKeyPMModulus, publicExponent, PublicKeyProductModulus);
		
		payAgent.initPay(this);
		
		initViews();

	}

	private void initViews() {
		progressDialog = new ProgressDialog(MainActivity.this);

		aliPayBtn = (Button) findViewById(R.id.alipay);
		wxPayBtn = (Button) findViewById(R.id.weichatpay);
		upPayBtn = (Button) findViewById(R.id.uppay);

		aliPayBtn.setOnClickListener(this);
		wxPayBtn.setOnClickListener(this);
		upPayBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.alipay:

			PayInfo payInfo = new PayInfo();
			payInfo.setSubject("商品名称");
			payInfo.setPrice("20");
			payInfo.setNotifyUrl("www.cs.not");
			payInfo.setBody("商品描述");
			payInfo.setOrderNo("201507211420020069452");
			testPay(PayAgent.PayType.ALIPAY, payInfo);
			break;

		case R.id.weichatpay:
			PayInfo info = new PayInfo();
			info.setOrderNo("201507211420020069452");
			testPay(PayAgent.PayType.WECHATPAY, info);
			break;

		case R.id.uppay:
			
			requestTestOrderNo();
			
			break;

		default:
			break;
		}

	}
	
	/**
	 *
	 * 网银 请求支付订单号
	* @return void
	* @autour BaoHong.Li
	* @date 2015年7月22日16:42:36
	* @update (date)
	 */
	private void requestTestOrderNo(){
		
		new AsyncTask<String, Integer, String>() {
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog.setTitle("请求订单中...");
				progressDialog.show();
			}
			
			@Override
			protected String doInBackground(String... params) {
				
				   String tn = null;
			        InputStream is;
			        try {

			            String url = "http://202.101.25.178:8080/sim/gettn";

			            URL myURL = new URL(url);
			            URLConnection ucon = myURL.openConnection();
			            ucon.setConnectTimeout(120000);
			            is = ucon.getInputStream();
			            int i = -1;
			            ByteArrayOutputStream baos = new ByteArrayOutputStream();
			            while ((i = is.read()) != -1) {
			                baos.write(i);
			            }

			            tn = baos.toString();
			            is.close();
			            baos.close();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }

			        Log.i(getClass().getName(), "response :" +tn);
			       
				return tn;
			}
			
			
			
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				
				progressDialog.dismiss();
				
				// 获取到 订单后，调起支付
				PayInfo inf = new PayInfo();
				inf.setOrderNo(result);
				testPay(PayAgent.PayType.UPPAY, inf);
			}
			
			
		}.execute();

     
    
	}

	/**
	 *
	 *  调起 支付平台
	* @param payType
	* @param payInfo
	* @return void
	* @autour BaoHong.Li
	* @date 2015-7-21 ����2:39:21 
	* @update (date)
	 */
	private void testPay(PayType payType, PayInfo payInfo) {

		PayAgent.getInstance().onPay(payType, this, payInfo,
				new OnPayListener() {

					@Override
					public void onStartPay() {
						
						progressDialog.setTitle("加载中...");
						progressDialog.show();
					}

					@Override
					public void onPaySuccess() {
//						public void onPaySuccess(String code, String msg) {
						
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
	}

}
