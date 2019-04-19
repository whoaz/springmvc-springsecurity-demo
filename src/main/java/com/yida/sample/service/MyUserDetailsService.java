package com.yida.sample.service;


import com.yida.sample.model.User;
import com.yida.sample.model.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service("myUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {

	private final static Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

	@Autowired
	private UserService userService;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String ssoId) throws UsernameNotFoundException {

		User user = userService.findBySSO(ssoId);
		logger.info("user:{} ", user);
		if (Objects.isNull(user)) {
			logger.info("User not found ");
			throw new UsernameNotFoundException("Username not found");
		}

		return new org.springframework.security.core.userdetails.User(user.getSsoId(), user.getPassword(),
				true, true, true, true, getGrantedAuthorities(user));
	}

	private List<GrantedAuthority> getGrantedAuthorities(User user) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (UserProfile userProfile : user.getUserProfiles()) {
			logger.info("userProfile:{} ", userProfile);
			authorities.add(new SimpleGrantedAuthority("ROLE_" + userProfile.getType()));
		}
		logger.info("authorities:{} ", authorities);
		return authorities;
	}
}
