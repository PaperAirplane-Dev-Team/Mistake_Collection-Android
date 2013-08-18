package org.papdt.miscol.ui;

import org.papdt.miscol.R;
import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.bean.MistakeOperationException;
import org.papdt.miscol.dao.DatabaseHelper;
import org.papdt.miscol.ui.fragment.FragmentAddMistake0;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ActivityAddMistake extends Activity {

	private final static String TAG = "ActivityAddMistake";

	private FragmentManager mFragmentManager;
	private FragmentTransaction mTransaction;
	private DatabaseHelper mDbHelper;

	public final static String[] TAGS = { "First", "Second" };
	public final static String KEY = "Mistake";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addmistake);
		mFragmentManager = getFragmentManager();
		mDbHelper = DatabaseHelper.getInstance(this);
		initializeActionBar();
		startStepOne();
		Log.d(TAG, TAG + "已完成初始化");
	}

	private void startStepOne() {
		Fragment fragment = new FragmentAddMistake0();
		mTransaction = mFragmentManager.beginTransaction();
		mTransaction.add(R.id.fl_content, fragment, TAGS[0]);
		mTransaction.attach(fragment).show(fragment).commit();
		mFragmentManager.executePendingTransactions();
	}

	private void initializeActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// 有了这个向上按钮我们不再需要取消按钮
		getActionBar().setTitle(R.string.add_mistake);
	}

	public void finishAdding(Mistake m) {
		Log.i(TAG, "收到错题添加请求, 错题信息:" + m.toString());
		try {
			mDbHelper.insertMistake(m);
		} catch (MistakeOperationException e) {
			e.printStackTrace();
		}
		Toast.makeText(getApplicationContext(), R.string.add_mistake_succeed,
				Toast.LENGTH_SHORT).show();
		finish();
	}

}