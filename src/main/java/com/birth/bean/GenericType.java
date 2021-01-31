package com.birth.bean;

public class GenericType<T> {
	
	private Class<T> type;

	public Class<T> getType() {
		return type;
	}

	public void setType(Class<T> type) {
		this.type = type;
	}
}
