package com.yida.sample.dao;

import com.yida.sample.model.UserProfile;

import java.util.List;

public interface UserProfileDao {
	List<UserProfile> findAll();

	UserProfile findByType(String type);

	UserProfile findById(int id);
}
