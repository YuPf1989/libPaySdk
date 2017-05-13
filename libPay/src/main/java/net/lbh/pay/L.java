package net.lbh.pay;

import android.text.TextUtils;
import android.util.Log;

/**
 * 支付组件 日志管理类
 * 
 * @author BaoHong.Li
 * @date 2015-7-16 上午11:23:32
 * @update (date)
 * @version V1.0
 */
public class L {
	public static String LOG_TAG;
	public static boolean isDebug = false;
	public static String customTagPrefix = "";

	private static String generateTag(StackTraceElement caller) {
		String tag = "%s.%s(L:%d)";
		String callerClazzName = caller.getClassName();
		callerClazzName = callerClazzName.substring(callerClazzName
				.lastIndexOf(".") + 1);
		tag = String.format(tag, callerClazzName, caller.getMethodName(),
				caller.getLineNumber());
		tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":"
				+ tag;
		return tag;
	}

	static {
		LOG_TAG = L.class.getName();
	}

	static StackTraceElement getCallerStackTraceElement() {
		return Thread.currentThread().getStackTrace()[4];
	}

	public static void d(String tag, String msg) {
		if (isDebug) {
			StackTraceElement caller = getCallerStackTraceElement();
			Log.d(tag, caller + "\n" + msg);
		}
	}

	public static void d(String msg, Throwable tr) {
		if (isDebug) {
			StackTraceElement caller = getCallerStackTraceElement();
			String tag = generateTag(caller);

			Log.d(tag, msg, tr);
		}
	}

	public static void d(Object c, String msg) {
		if (isDebug) {
			StackTraceElement caller = getCallerStackTraceElement();
			Log.d(c.getClass().getName(), caller + "\n" + msg);
		}
	}

	public static void e(String tag, String msg) {
		if (isDebug) {
			StackTraceElement caller = getCallerStackTraceElement();
			Log.e(tag, caller + "\n" + msg);
		}
	}

	public static void e(String tag, String msg, Throwable tr) {
		Log.e(tag, msg, tr);
	}

	public static void e(String msg, Throwable tr) {
		if (isDebug) {
			StackTraceElement caller = getCallerStackTraceElement();
			String tag = generateTag(caller);

			Log.e(tag, msg, tr);
		}
	}

	public static void e(Object c, String msg) {
		if (isDebug) {
			StackTraceElement caller = getCallerStackTraceElement();
			Log.e(c.getClass().getName(), caller + "\n" + msg);

		}
	}

	public static void i(String tag, String msg) {
		if (isDebug) {
			StackTraceElement caller = getCallerStackTraceElement();
			Log.i(tag, caller + "\n" + msg);
		}
	}

	public static void i(Object c, String msg) {
		if (isDebug) {
			StackTraceElement caller = getCallerStackTraceElement();
			Log.i(c.getClass().getName(), caller + "\n" + msg);
		}
	}

	public static void i(String msg, Throwable tr) {
		if (isDebug) {
			StackTraceElement caller = getCallerStackTraceElement();
			String tag = generateTag(caller);

			Log.i(tag, msg, tr);
		}
	}

	public static void print(String tag, String msg) {
	}

	public static void v(String tag, String msg) {
		if (isDebug)
			Log.v(tag, msg);
	}

	public static void v(String msg, Throwable tr) {
		if (isDebug) {
			StackTraceElement caller = getCallerStackTraceElement();
			String tag = generateTag(caller);

			Log.v(tag, msg, tr);
		}
	}

	public static void w(String tag, String msg) {
		if (isDebug) {
			StackTraceElement caller = getCallerStackTraceElement();
			Log.w(tag, caller + "\n" + msg);
		}
	}

	public static void w(String msg, Throwable tr) {
		if (isDebug) {
			StackTraceElement caller = getCallerStackTraceElement();
			String tag = generateTag(caller);
			Log.w(tag, msg, tr);
		}
	}

	public static void w(Throwable tr) {
		if (isDebug) {
			StackTraceElement caller = getCallerStackTraceElement();
			String tag = generateTag(caller);

			Log.w(tag, tr);
		}
	}
}
