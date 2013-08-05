package org.papdt.miscol.utils;

import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesOperator {
	private static final String TAG = "SharedPreferencesOperator";

	@SuppressWarnings("unchecked")
	public static boolean write(Context context, String preferenceName,
			String key, Object value) {
		MyLogger.d(TAG, "向" + preferenceName + "中写入名为" + key + "的键值" + value);
		Editor editor = context.getSharedPreferences(preferenceName,
				Context.MODE_PRIVATE).edit();
		if (value instanceof String)
			editor.putString(key, (String) value);
		else if (value instanceof Boolean)
			editor.putBoolean(key, (Boolean) value);
		else if (value instanceof Integer)
			editor.putInt(key, (Integer) value);
		else if (value instanceof Float)
			editor.putFloat(key, (Float) value);
		else if (value instanceof Long)
			editor.putLong(key, (Long) value);
		else if (value instanceof Set<?>)
			editor.putStringSet(key, (Set<String>) value);
		else {
			MyLogger.w(TAG, "类型不正确，写入失败");
			return false;
		}
		return editor.commit();

	}

	public static Object read(Context context, String preferenceName,
			String key, Object defaultValue) {
		MyLogger.d(TAG, "查询来自" + preferenceName + "的键值" + key);
		SharedPreferences preferences = context.getSharedPreferences(
				preferenceName, Context.MODE_PRIVATE);
		Map<String, ?> allPreferences = preferences.getAll();
		if (allPreferences.containsKey(key)) {
			Object o = allPreferences.get(key);
			MyLogger.d(TAG, "存在该值，为:" + o);
			return o;
		} else {
			MyLogger.d(TAG, "不存在该值，返回指定的默认值:" + defaultValue);
			return defaultValue;
		}
	}
}
