package org.papdt.miscol.ui;

import org.papdt.miscol.R;
import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.ui.fragment.FragmentAddMistake0;
import org.papdt.miscol.utils.MyLogger;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

public class ActivityAddMistake extends Activity {

	private final static String TAG = "ActivityAddMistake";

	private FragmentManager mFragmentManager;
	private FragmentTransaction mTransaction;

	public final static String[] TAGS = { "First", "Second" };
	public final static String KEY = "Mistake";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addmistake);
		mFragmentManager = getFragmentManager();
		initializeActionBar();
		startStepOne();
		// XXX 此类需要大改，不能直接照搬ActivityMain
		MyLogger.d(TAG, TAG + "已完成初始化");
	}

	@SuppressWarnings("deprecation")
	private void startStepOne() {
		Fragment fragment = new FragmentAddMistake0();
		mTransaction = mFragmentManager.beginTransaction();
		mTransaction.add(R.id.fl_content, fragment, TAGS[0]).addToBackStack(
				TAGS[0]);
		mTransaction.attach(fragment).show(fragment).commit();
		mFragmentManager.executePendingTransactions();
	}

	private void initializeActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// 有了这个向上按钮我们不再需要取消按钮
		getActionBar().setTitle("添加错题");
	}

	public void finishAdding(Mistake m) {
		// TODO 将mistake写入数据库
		MyLogger.i(TAG, "收到错题添加请求, 错题信息:" + m.toString());
		Toast.makeText(getApplicationContext(), "添加成功!", Toast.LENGTH_SHORT).show();
		finish();
	}

}