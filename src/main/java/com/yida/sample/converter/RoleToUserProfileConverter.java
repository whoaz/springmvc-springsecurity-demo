package com.yida.sample.converter;

import com.yida.sample.model.UserProfile;
import com.yida.sample.service.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author zhoukun
 */
@Component
public class RoleToUserProfileConverter  implements Converter<Object, UserProfile>{
	private final static Logger logger = LoggerFactory.getLogger(RoleToUserProfileConverter.class);

	@Autowired
	private UserProfileService userProfileService;

	@Override
	public UserProfile convert(Object ele) {
		Integer id = Integer.parseInt((String) ele);
		UserProfile profile = userProfileService.findById(id);
		logger.info("profile:{} ", profile);
		return profile;
	}
}
