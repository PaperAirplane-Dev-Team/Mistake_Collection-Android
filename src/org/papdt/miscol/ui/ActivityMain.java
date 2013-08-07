package org.papdt.miscol.ui;

import org.papdt.miscol.R;
import org.papdt.miscol.ui.adapter.DrawerAdapter;
import org.papdt.miscol.ui.adapter.DrawerAdapter.IDrawerNames;
import org.papdt.miscol.ui.fragment.FragmentMain;
import org.papdt.miscol.ui.fragment.FragmentMistakes;
import org.papdt.miscol.utils.MyLogger;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;

import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * App的主Activity <br />
 * 初始化抽屉和首页
 */
public class ActivityMain extends Activity implements IDrawerNames {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private RelativeLayout mRlDrawer;
	private ActionBarDrawerToggle mDrawerToggle;
	private Fragment[] mFragments;
	private FragmentTransaction mTransaction;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mDrawerItemNames;
	private FragmentManager mFragmentManager;
	private final static String TAG = "ActivityMain";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDrawerItemNames = getResources().getStringArray(R.array.drawer_items);
		mFragmentManager = getFragmentManager();
		mFragments = new Fragment[mDrawerItemNames.length];
		initializeDrawer();
		if (savedInstanceState == null) {
			selectItem(MAIN);
			// 默认打开FragmentMain
		}
		MyLogger.d(TAG, TAG + "已完成初始化");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MyLogger.d(TAG, "onCreateOptionsMenu");
		// 在不同的Fragment中分开处理
		return super.onCreateOptionsMenu(menu);
	}


	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		return super.onOptionsItemSelected(item);
	}

	/* The click listener for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update the main content by replacing mFragments
		boolean initialized;
		mTransaction = mFragmentManager.beginTransaction();
		for (Fragment fragment : mFragments) {
			hideFragment(fragment);
		}
		if (mFragments[position] == null) {
			MyLogger.d(TAG, "创建新的 Fragment:" + position);
			initialized = false;
			switch (position) {
			case MAIN:
				mFragments[position] = FragmentMain.getInstance();
				break;
			case ALL_QUESTIONS:
				mFragments[position] = FragmentMistakes.getInstance();
				break;
			// TODO FUCK YOU!!!!!!
			default:
				mFragments[position] = new Fragment();
				// TODO 初始化各Fragment
				break;
			}
		} else {
			MyLogger.d(TAG, "已存在Fragment:" + position);
			initialized = true;
		}
		replaceToFragment(mFragments[position], initialized);
		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(mRlDrawer);
	}

	private void replaceToFragment(Fragment fragment, boolean hasInitialized) {
		if (!hasInitialized) {
			mTransaction.add(R.id.fl_content, fragment);
		}
		mTransaction.attach(fragment).show(fragment).commit();
		mFragmentManager.popBackStack();
		mFragmentManager.executePendingTransactions();
	}

	private void hideFragment(Fragment fragment) {
		if (fragment != null) {
			mTransaction.hide(fragment);
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * 初始化NavigationDrawer <br />
	 * 向ListView中填充程序导航模块
	 */
	private void initializeDrawer() {

		mTitle = getTitle();
		mDrawerTitle = getString(R.string.drawer_title);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main);
		mDrawerList = (ListView) findViewById(R.id.lv_drawer);
		mRlDrawer = (RelativeLayout) findViewById(R.id.rl_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new DrawerAdapter(mDrawerItemNames, this));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
				((FragmentMain) mFragments[MAIN]).showHint();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
				((FragmentMain) mFragments[MAIN]).hideHint();

			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

}
