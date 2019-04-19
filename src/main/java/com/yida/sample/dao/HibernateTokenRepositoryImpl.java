package com.yida.sample.dao;

import com.yida.sample.model.PersistentLogin;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/**
 * @author zhoukun
 */
@Transactional
@Repository("tokenRepositoryDao")
public class HibernateTokenRepositoryImpl extends AbstractDao<String, PersistentLogin> implements PersistentTokenRepository {
	private final static Logger logger = LoggerFactory.getLogger(HibernateTokenRepositoryImpl.class);

	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		logger.info("create New Token for user:{}", token.getUsername());
		PersistentLogin persistentLogin = new PersistentLogin();
		persistentLogin.setUsername(token.getUsername());
		persistentLogin.setSeries(token.getSeries());
		persistentLogin.setLastUsed(LocalDateTime.ofInstant(token.getDate().toInstant(), ZoneId.systemDefault()));
		persist(persistentLogin);
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		logger.info("Updating Token for seriesId : {}", series);
		PersistentLogin persistentLogin = getByKey(series);
		persistentLogin.setToken(tokenValue);
		persistentLogin.setLastUsed(LocalDateTime.ofInstant(lastUsed.toInstant(), ZoneId.systemDefault()));
		update(persistentLogin);
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		logger.info("Fetch Token if any for seriesId : {}", seriesId);
		try {
			Criteria criteria = createEntityCriteria();
			criteria.add(Restrictions.eq("series", seriesId));
			PersistentLogin persistentLogin = (PersistentLogin) criteria.uniqueResult();
			return new PersistentRememberMeToken(persistentLogin.getUsername(), persistentLogin.getSeries(), persistentLogin.getToken()
					, Date.from(persistentLogin.getLastUsed().atZone(ZoneId.systemDefault()).toInstant()));
		} catch (Exception e) {
			logger.error("Token not found...", e);
			return null;
		}

	}

	@Override
	public void removeUserTokens(String username) {
		logger.info("Removing Token if any for user : {}", username);
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("username", username));
		PersistentLogin persistentLogin = (PersistentLogin) criteria.uniqueResult();
		if (Objects.nonNull(persistentLogin)) {
			logger.info("rememberMe was selected");
			delete(persistentLogin);
		}
	}
}
