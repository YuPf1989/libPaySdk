package net.lbh.pay;


public abstract class OnPayListener {
	
	public void onStartPay(){}

	public abstract void onPaySuccess();
//	public abstract void onPaySuccess(String code, String msg);

	public abstract void onPayFail(String code, String msg);

}
