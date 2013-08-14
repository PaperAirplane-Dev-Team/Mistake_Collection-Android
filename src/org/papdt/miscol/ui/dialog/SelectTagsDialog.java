package org.papdt.miscol.ui.dialog;

import org.papdt.miscol.R;
import org.papdt.miscol.ui.fragment.FragmentAddMistake0;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SelectTagsDialog extends DialogFragment {
	private FragmentAddMistake0 mFragment;
	public static final String TAG = "SelectTagsDialog";

	public void setFragment(FragmentAddMistake0 fragment) {
		mFragment = fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LinearLayout layout = new LinearLayout(getActivity());
		layout.setOrientation(LinearLayout.VERTICAL);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		String[] allTags = (String[]) mFragment.mAllTags
				.toArray(new String[] { "" });
		if (allTags != null && allTags.length > 0) {
			mFragment.mCheckBoxes = new CheckBox[allTags.length];
			for (int i = 0; i < mFragment.mAllTags.size(); i++) {
				mFragment.mCheckBoxes[i] = new CheckBox(getActivity());
				mFragment.mCheckBoxes[i].setText(allTags[i]);
				if (mFragment.mTags.contains(allTags[i])) {
					mFragment.mCheckBoxes[i].setChecked(true);
				}
				layout.addView(mFragment.mCheckBoxes[i], params);
			}
		}
		DialogInterface.OnClickListener selectListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					if (mFragment.mCheckBoxes.length > 0) {
						for (CheckBox cb : mFragment.mCheckBoxes) {
							if (cb.isChecked()) {
								mFragment.mTags.add(cb.getText().toString());
							} else {
								mFragment.mTags.remove(cb.getText().toString());
							}
						}
					}
					mFragment.showTags();
					break;
				}
			}
		};
		TextView tvAddTag = new TextView(getActivity());
		tvAddTag.setGravity(Gravity.CENTER);
		tvAddTag.setBackgroundResource(R.drawable.customized_button_background);
		tvAddTag.setText(R.string.add_category);
		tvAddTag.setTextAppearance(getActivity(),
				android.R.attr.textAppearanceLarge);
		tvAddTag.setTextSize(20);
		// 为什么同是textAppearanceLarge差别这么大……只好手工设置textSize
		tvAddTag.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				final EditText et = new EditText(getActivity());
				et.setHint(R.string.add_tag_hint);
				final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							if (!TextUtils.isEmpty(et.getText())) {
								mFragment.mTags.add(et.getText().toString());
								mFragment.mAllTags.add(et.getText().toString());
								mFragment.showTags();
								dialog.dismiss();
							} else {
								Toast.makeText(getActivity(),
										R.string.add_tag_empty,
										Toast.LENGTH_SHORT).show();
							}
							break;
						}
					}
				};
				dismiss();

				new AlertDialog.Builder(getActivity())
						.setTitle(R.string.add_tag_title).setView(et)
						.setPositiveButton(android.R.string.ok, listener)
						.setNegativeButton(android.R.string.cancel, listener)
						.show();
			}
		});
		layout.addView(tvAddTag, params);
		return new AlertDialog.Builder(getActivity())
				.setTitle(R.string.add_tag_title).setView(layout)
				.setPositiveButton(android.R.string.ok, selectListener)
				.setNegativeButton(android.R.string.cancel, selectListener)
				.create();
	}

}
