package org.papdt.miscol.ui.dialog;

import org.papdt.miscol.R;
import org.papdt.miscol.bean.Mistake;
import org.papdt.miscol.bean.MistakeOperationException;
import org.papdt.miscol.dao.DatabaseHelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class DeleteMistakeDialog extends DialogFragment implements
		OnClickListener {
	public static final String KEY = "Mistake";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setTitle(R.string.del_title).setMessage(R.string.del_content)
				.setIcon(android.R.drawable.ic_input_delete)
				.setPositiveButton(android.R.string.yes, this)
				.setNegativeButton(android.R.string.cancel, this);
		return builder.create();
	}

	@Override
	public void onClick(DialogInterface view, int button) {
		switch (button) {
		case DialogInterface.BUTTON_POSITIVE:
			Mistake m = getArguments().getParcelable(KEY);
			DatabaseHelper dbHelper = DatabaseHelper.getInstance(null);
			try {
				dbHelper.deleteMistake(m);
			} catch (MistakeOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//TODO 独立在Thread
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			this.dismiss();
			break;
		}

	}
}
