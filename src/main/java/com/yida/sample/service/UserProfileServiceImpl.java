package com.yida.sample.service;

import com.yida.sample.dao.UserProfileDao;
import com.yida.sample.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userProfileService")
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

	@Autowired
	private UserProfileDao userProfileDao;

	@Override
	public UserProfile findById(int id) {
		return userProfileDao.findById(id);
	}

	@Override
	public UserProfile findByType(String type) {
		return userProfileDao.findByType(type);
	}

	@Override
	public List<UserProfile> findAll() {
		return userProfileDao.findAll();
	}
}
