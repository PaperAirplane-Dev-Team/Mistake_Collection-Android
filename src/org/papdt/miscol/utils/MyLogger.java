package org.papdt.miscol.utils;

import org.papdt.miscol.BuildConfig;

import android.util.Log;

/**
 * 自行实现的日志类,会判断是否在调试状态,与android.util.Log相同功能
 */
public class MyLogger {

	public static final int NULL = -1;

	public static int d(String tag, String msg) {
		if (BuildConfig.DEBUG)
			return Log.d(tag, msg);
		return NULL;
	}

	public static int d(String tag, String msg, Throwable tr) {
		if (BuildConfig.DEBUG)
			return Log.d(tag, msg, tr);
		return NULL;
	}

	public static int e(String tag, String msg) {
		if (BuildConfig.DEBUG)
			return Log.e(tag, msg);
		return NULL;
	}

	public static int e(String tag, String msg, Throwable tr) {
		if (BuildConfig.DEBUG)
			return Log.e(tag, msg, tr);
		return NULL;
	}

	public static String getStackTraceString(Throwable tr) {
		if (BuildConfig.DEBUG)
			return Log.getStackTraceString(tr);
		return null;
	}

	public static int i(String tag, String msg) {
		if (BuildConfig.DEBUG)
			return Log.i(tag, msg);
		return NULL;
	}

	public static int i(String tag, String msg, Throwable tr) {
		if (BuildConfig.DEBUG)
			return Log.i(tag, msg, tr);
		return NULL;
	}

	public static boolean isLoggable(String tag, int level) {
		if (BuildConfig.DEBUG)
			return Log.isLoggable(tag, level);
		return false;
	}

	public static int println(int priority, String tag, String msg) {
		if (BuildConfig.DEBUG)
			return Log.println(priority, tag, msg);
		return NULL;
	}

	public static int v(String tag, String msg) {
		if (BuildConfig.DEBUG)
			return Log.v(tag, msg);
		return NULL;
	}

	public static int v(String tag, String msg, Throwable tr) {
		if (BuildConfig.DEBUG)
			return Log.v(tag, msg, tr);
		return NULL;
	}

	public static int w(String tag, String msg) {
		if (BuildConfig.DEBUG)
			return Log.w(tag, msg);
		return NULL;
	}

	public static int w(String tag, String msg, Throwable tr) {
		if (BuildConfig.DEBUG)
			return Log.w(tag, msg, tr);
		return NULL;
	}

	public static int wtf(String tag, String msg) {
		if (BuildConfig.DEBUG)
			return Log.wtf(tag, msg);
		return NULL;
	}

	public static int wtf(String tag, String msg, Throwable tr) {
		if (BuildConfig.DEBUG)
			return Log.wtf(tag, msg, tr);
		return NULL;
	}

	public static int wtf(String tag, Throwable tr) {
		if (BuildConfig.DEBUG)
			return Log.wtf(tag, tr);
		return NULL;
	}
}
