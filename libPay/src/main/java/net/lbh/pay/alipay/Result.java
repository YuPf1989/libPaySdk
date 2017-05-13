package net.lbh.pay.alipay;

import java.util.HashMap;
import java.util.Map;

import net.lbh.pay.ConstantKeys;
import net.lbh.pay.L;

import org.json.JSONObject;

import android.text.TextUtils;

public class Result {

	private static final Map<String, String> sResultStatus;

	private String mResult;
	String resultCode = null;
	String resultMsg = null;
	String memo = null;
	String result = null;
	boolean isSignOk = false;

	static {
		sResultStatus = new HashMap<String, String>();
		sResultStatus.put("9000", "操作成功");
		sResultStatus.put("4000", "系统异常");
		sResultStatus.put("4001", "数据格式不正确");
		sResultStatus.put("4003", "该用户绑定的支付宝账户被冻结或不允许支付");
		sResultStatus.put("4004", "该用户已解除绑定");
		sResultStatus.put("4005", "绑定失败或没有绑定");
		sResultStatus.put("4006", "订单支付失败");
		sResultStatus.put("4010", "重新绑定账户");
		sResultStatus.put("6000", "支付服务正在进行升级操作");
		sResultStatus.put("6001", "用户中途取消支付操作");
		sResultStatus.put("7001", "网页支付失败");
	}

	public Result(String result) {
		this.mResult = result;
	}

	public String getResult() {
		String src = mResult.replace("{", "");
		src = src.replace("}", "");
		return getContent(src, "memo=", ";result");
	}

	public void parseResult() {

		try {
			String src = mResult.replace("{", "");
			src = src.replace("}", "");
			resultCode = getContent(src, "resultStatus=", ";memo");
			if (sResultStatus.containsKey(resultCode)) {
				resultMsg = sResultStatus.get(resultCode);
			} else {
				resultMsg = "其他错误";
			}
			resultMsg += "(" + resultCode + ")";

			memo = getContent(src, "memo=", ";result");
			result = getContent(src, "result=", null);
			isSignOk = checkSign(result);
		} catch (Exception e) {
			L.e(e.getMessage(), e);
		}
	}

	public JSONObject string2JSON(String src, String split) {
		JSONObject json = new JSONObject();

		try {
			String[] arr = src.split(split);
			for (int i = 0; i < arr.length; i++) {
				String[] arrKey = arr[i].split("=");
				json.put(arrKey[0], arr[i].substring(arrKey[0].length() + 1));
			}
		} catch (Exception e) {
			L.e(e.getMessage(), e);
		}

		return json;
	}

	private boolean checkSign(String result) {
		if (TextUtils.isEmpty(result))
			return false;
		boolean retVal = false;
		try {
			JSONObject json = string2JSON(result, "&");

			// int pos = result.indexOf("&sign_type=");
			int pos = result.indexOf("&sign=");
			String signContent = result.substring(0, pos);

			String signType = json.getString("sign_type");
			signType = signType.replace("\"", "");

			String sign = json.getString("sign");
			sign = sign.replace("\"", "");

			if (signType.equalsIgnoreCase("RSA")) {
				retVal = Rsa.doCheck(signContent, sign,
						ConstantKeys.AliPay.ALIPAY_PUBLIC_KEY);
			}
		} catch (Exception e) {
			L.e("Result", "Exception =" + e.getMessage(), e);
		}
		L.i("Result", "checkSign =" + retVal);
		return retVal;
	}

	private String getContent(String src, String startTag, String endTag) {
		String content = src;
		int start = src.indexOf(startTag);
		start += startTag.length();

		try {
			if (endTag != null) {
				int end = src.indexOf(endTag);
				content = src.substring(start, end);
			} else {
				content = src.substring(start);
			}
		} catch (Exception e) {
			L.e(e.getMessage(), e);
		}

		return content;
	}
}
