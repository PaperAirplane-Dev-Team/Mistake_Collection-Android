package org.papdt.miscol.bean;

import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Answer {
	public static interface QUESTION_TYPES {
		final int SELECT = 0;
		final int FILL = 1;
		final int JUDGE = 2;
	}

	public static interface KEYS {
		final String DESCRIPTION = "Description";
		final String JUDGEMENT = "Judgement";
		final String SELECTIONS = "Selections";
	}

	private int mType;
	private HashSet<String> mSelections;
	private boolean mJudgement;
	private String mDescription;

	@SuppressWarnings("unused")
	private Answer() {

	}

	public Answer(int type) {
		if (type > 2 || type < 0) {
			throw new IllegalArgumentException("invalid type id");
		}
		this.mType = type;
		if (type == QUESTION_TYPES.SELECT) {
			mSelections = new HashSet<String>();
		}
	}

	public void addSelection(String selection) {
		if (mType != QUESTION_TYPES.SELECT) {
			throw new UnsupportedOperationException();
		}
		mSelections.add(selection);
	}

	public void setJudgement(boolean judgement) {
		if (mType != QUESTION_TYPES.JUDGE) {
			throw new UnsupportedOperationException();
		}
		mJudgement = judgement;
	}

	public void setDescription(String description) {
		mDescription = description;
	}
	
	public static Answer createAnswerFromJson(String json,int type){
		if (type > 2 || type < 0) {
			throw new IllegalArgumentException("invalid type id");
		}
		Answer a = new Answer(type);
		JSONObject jobj = null;
		try {
			jobj = new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			a.setDescription(jobj.getString(KEYS.DESCRIPTION));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		switch(type){
		case QUESTION_TYPES.JUDGE:
			try {
				a.setJudgement(jobj.getBoolean(KEYS.JUDGEMENT));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		case QUESTION_TYPES.SELECT:
			try {
				JSONArray jarr = jobj.getJSONArray(KEYS.JUDGEMENT);
				int length = jarr.length();
				for(int i = 0;i<length;i++){
					a.addSelection(jarr.getString(i));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		}
		return a;
	}

	@Override
	public String toString() {
		JSONObject jobj = new JSONObject();
		try {
			jobj.put(KEYS.DESCRIPTION, mDescription);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		switch (mType) {
		case QUESTION_TYPES.FILL:
			try {
				jobj.put(KEYS.JUDGEMENT, mJudgement);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return jobj.toString();
		case QUESTION_TYPES.JUDGE:
			JSONArray jarr = new JSONArray();
			for (String sel : mSelections) {
				jarr.put(sel);
			}
			try {
				jobj.put(KEYS.SELECTIONS, jarr);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return jobj.toString();
		}
		return jobj.toString();
	}
}
