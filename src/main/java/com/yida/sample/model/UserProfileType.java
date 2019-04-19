package com.yida.sample.model;

import java.io.Serializable;

public enum UserProfileType implements Serializable {
	USER("USER"),
	DBA("DBA"),
	ADMIN("ADMIN");

	String userProfileType;

	UserProfileType(String userProfileType) {
		this.userProfileType = userProfileType;
	}

	public String getUserProfileType() {
		return userProfileType;
	}
}
