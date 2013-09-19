package org.papdt.miscol.bean;

public class CategoryInfo {

	public static final int NULL = -1;

	public static interface TYPE {
		int TAGS = 0, GRADES = 1, SUBJECTS = 2;
	}

	private String name;
	private int id, count, subCount = NULL,type;

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public int getCount() {
		return count;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getSubCount() {
		return subCount;
	}

	public void setSubCount(int subCount) {
		this.subCount = subCount;
	}

	public void addCount() {
		this.count++;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
