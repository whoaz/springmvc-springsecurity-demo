package com.yida.sample.dao;

import com.yida.sample.model.UserProfile;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userProfileDao")
public class UserProfileDaoImpl extends AbstractDao<Integer, UserProfile> implements UserProfileDao {

	@Override
	public List<UserProfile> findAll() {
		Criteria criteria = createEntityCriteria().addOrder(Order.asc("type"));
		return criteria.list();
	}

	@Override
	public UserProfile findByType(String type) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("type", type));
		return (UserProfile) criteria.uniqueResult();
	}

	@Override
	public UserProfile findById(int id) {
		return getByKey(id);
	}
}
