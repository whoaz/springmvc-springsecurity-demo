package com.yida.sample.dao;

import com.yida.sample.model.User;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository("userDao")
public class UserDaoImpl extends AbstractDao<Integer, User> implements UserDao {
	static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	@Override
	public User findById(int id) {
		User user = getByKey(id);
		if (Objects.nonNull(user)) {
			Hibernate.initialize(user.getUserProfiles());
		}
		return user;
	}

	@Override
	public User findBySSO(String sso) {
		logger.info("SSO : {}", sso);
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("ssoId", sso));
		User user = (User) criteria.uniqueResult();
		if (Objects.nonNull(user)) {
			Hibernate.initialize(user.getUserProfiles());
		}
		return user;
	}

	@Override
	public void save(User user) {
		persist(user);
	}

	@Override
	public void deleteBySSO(String sso) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("ssoId", sso));
		User user = (User) criteria.uniqueResult();
		delete(user);
	}

	@Override
	public List<User> findAllUsers() {
		Criteria criteria = createEntityCriteria().addOrder(Order.asc("firstName"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

}
