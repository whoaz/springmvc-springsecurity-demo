package com.yida.sample.service;

import com.yida.sample.dao.UserDao;
import com.yida.sample.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User findById(int id) {
		return userDao.findById(id);
	}

	@Override
	public User findBySSO(String sso) {
		return userDao.findBySSO(sso);
	}

	@Override
	public void saveUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userDao.save(user);
	}


	@Override
	public void updateUser(User user) {
		User entity = findById(user.getId());
		if (Objects.nonNull(entity)) {
			entity.setSsoId(user.getSsoId());
			if (!Objects.equals(user.getPassword(), entity.getPassword())) {
				entity.setPassword(passwordEncoder.encode(user.getPassword()));
			}
			entity.setFirstName(user.getFirstName());
			entity.setLastName(user.getLastName());
			entity.setEmail(user.getEmail());
			entity.setUserProfiles(user.getUserProfiles());
		}
	}


	@Override
	public void deleteUserBySSO(String sso) {
		userDao.deleteBySSO(sso);
	}

	@Override
	public List<User> findAllUsers() {
		return userDao.findAllUsers();
	}

	@Override
	public boolean isUserSSOUnique(Integer id, String sso) {
		User user = findBySSO(sso);
		return Objects.isNull(user) || Objects.equals(user.getId(), id);
	}
}
