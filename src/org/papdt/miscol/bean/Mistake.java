package org.papdt.miscol.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

public class Mistake implements Cloneable, Serializable, Parcelable {

	private static final long serialVersionUID = 1358856916991350617L;
	@SuppressLint("SimpleDateFormat")
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat(
			"yyyy/MM/dd HH:m:ss");

	private int id = -1;
	private String addTime;
	private String lastModifyTime = "NO";
	private String title;
	private int typeId = -1;
	private String typeName;
	private String questionText;
	private int questionPhotoId = -1;
	private String questionPhotoPath;
	private String answerText;
	private int answerPhotoId = -1;
	private String answerPhotoPath;
	private String lastReviewTime = "NO";
	private int reviewTimes = -1;
	private int reviewCorrectTimes = -1;
	private double correctRate = -1.0d;
	private int subjectId = -1;
	private String subjectName;
	private int gradeId = -1;
	private String gradeName;
	private int[] tagIds;
	private String[] tagNames;
	private boolean isStarred;

	public Mistake(String title, String questionText) {
		this.title = title;
		this.questionText = questionText;
		this.addTime = sDateFormat.format(new Date());
	}

	@Deprecated
	public Mistake() {
	}

	@Override
	public String toString() {
		/*
		 * 手动拼接JSON！！还不用StringBuilder！我又不能够了！ 不知道有org.json么……
		 * 虽然我不知道这个方法存在的意义要说用来导出吧信息还不全但是看不下去了重写了它
		 */
		JSONObject jobj = new JSONObject();
		try {
			jobj.put("id", id);
			jobj.put("title", title);
			jobj.put("questionText", questionText);//我说不加引号还重复是要干嘛？
			jobj.put("answerText", answerText);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jobj.toString();
	}

	public String getAddTime() {
		return addTime;
	}

	public String getTitle() {
		return title;
	}

	public int getTypeId() {
		return typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public String getQuestionText() {
		return questionText;
	}

	public int getQuestionPhotoId() {
		return questionPhotoId;
	}

	public String getQuestionPhotoPath() {
		return questionPhotoPath;
	}

	public String getAnswerText() {
		return answerText;
	}

	public int getAnswerPhotoId() {
		return answerPhotoId;
	}

	public String getAnswerPhotoPath() {
		return answerPhotoPath;
	}

	public String getLastReviewTime() {
		return lastReviewTime;
	}

	public int getReviewTimes() {
		return reviewTimes;
	}

	public int getReviewCorrectTimes() {
		return reviewCorrectTimes;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public int getGradeId() {
		return gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public int[] getTagIds() {
		return tagIds;
	}

	public String[] getTagNames() {
		return tagNames;
	}

	public String getLastModifyTime() {
		return lastModifyTime;
	}

	public boolean isStarred() {
		return isStarred;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public void setQuestionPhotoId(int questionPhotoId) {
		this.questionPhotoId = questionPhotoId;
	}

	public void setQuestionPhotoPath(String questionPhotoPath) {
		this.questionPhotoPath = questionPhotoPath;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	public void setAnswerPhotoId(int answerPhotoId) {
		this.answerPhotoId = answerPhotoId;
	}

	public void setAnswerPhotoPath(String answerPhotoPath) {
		this.answerPhotoPath = answerPhotoPath;
	}

	public void setLastReviewTime(String lastReviewTime) {
		this.lastReviewTime = lastReviewTime;
	}

	public void setReviewTimes(int reviewTimes) {
		this.reviewTimes = reviewTimes;
	}

	public void setReviewCorrectTimes(int reviewCorrectTimes) {
		this.reviewCorrectTimes = reviewCorrectTimes;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public void setTagIds(int[] tagIds) {
		this.tagIds = tagIds;
	}

	public void setTagNames(String[] tagNames) {
		this.tagNames = tagNames;
	}

	public void setStarred(boolean isStarred) {
		this.isStarred = isStarred;
	}

	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getCorrectRate() {
		return correctRate;
	}

	public void setCorrectRate(double correctRate) {
		this.correctRate = correctRate;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeSerializable(this);
	}

	public static final Parcelable.Creator<Mistake> CREATOR = new Parcelable.Creator<Mistake>() {

		@Override
		public Mistake createFromParcel(Parcel source) {
			return (Mistake) source.readSerializable();
		}

		@Override
		public Mistake[] newArray(int size) {
			return new Mistake[size];
		}
	};
}
