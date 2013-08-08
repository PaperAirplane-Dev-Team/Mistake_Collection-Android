package org.papdt.miscol.bean;

public class QueryCondition {
	private static final int FROM = 0, END = 1;
	
	private String addTime[] = null;
	private String lastModifyTime[] = null;
	private String lastReviewTime[] = null;
	private Integer reviewTimes[] = null;
	private Double correctRate[] = null;

	private String title = null;
	private Integer typeIds[] = null;
	private Integer subjectIds[] = null;
	private Integer gradeIds[] = null;
	private Integer tagIds[] = null;
	private boolean isStarred = false;

	public String getTitle() {
		return title;
	}


	public Integer[] getTagIds() {
		return tagIds;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTagIds(Integer[] tagIds) {
		this.tagIds = tagIds;
	}

	public String[] getAddTime() {
		return addTime;
	}

	public String[] getLastModifyTime() {
		return lastModifyTime;
	}

	public String[] getLastReviewTime() {
		return lastReviewTime;
	}

	public Integer[] getReviewTimes() {
		return reviewTimes;
	}

	public Double[] getCorrectRate() {
		return correctRate;
	}

	public void setAddTime(String from, String end) {
		this.addTime[FROM] = from;
		this.addTime[END] = end;
	}

	public void setLastModifyTime(String from, String end) {
		this.lastModifyTime[FROM] = from;
		this.lastModifyTime[END] = end;
	}

	public void setLastReviewTime(String from, String end) {
		this.lastReviewTime[FROM] = from;
		this.lastReviewTime[END] = end;
	}

	public void setReviewTimes(int from, int end) {
		this.reviewTimes[FROM] = from;
		this.reviewTimes[END] = end;
	}

	public void setCorrectRate(double from, double end) {
		this.correctRate[FROM] = from;
		this.correctRate[END] = end;
	}


	public Integer[] getTypeIds() {
		return typeIds;
	}


	public Integer[] getSubjectIds() {
		return subjectIds;
	}


	public Integer[] getGradeIds() {
		return gradeIds;
	}


	public void setTypeIds(Integer[] typeIds) {
		this.typeIds = typeIds;
	}


	public void setSubjectIds(Integer[] subjectIds) {
		this.subjectIds = subjectIds;
	}


	public void setGradeIds(Integer[] gradeIds) {
		this.gradeIds = gradeIds;
	}


	public boolean isStarred() {
		return isStarred;
	}


	public void setStarred(boolean isStarred) {
		this.isStarred = isStarred;
	}
}
