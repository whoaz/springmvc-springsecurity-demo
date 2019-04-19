package com.yida.sample.service;

import com.yida.sample.model.UserProfile;

import java.util.List;

public interface UserProfileService {
	UserProfile findById(int id);

	UserProfile findByType(String type);

	List<UserProfile> findAll();

}
