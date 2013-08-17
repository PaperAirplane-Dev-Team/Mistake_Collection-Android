package org.papdt.miscol.bean;

public class CategoryInfo {

	private String name;
	private int id, count, subCount;

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
}
