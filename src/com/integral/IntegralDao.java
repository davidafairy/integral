package com.integral;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class IntegralDao extends HibernateDaoSupport {

	@Autowired
	public void setSf(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	public Integral searchIntegral(String verificationCode) {
		String hql = "from Integral where verificationCode like ?";
		List result = this.getHibernateTemplate().find(hql, verificationCode);
		return result.size() > 0 ? (Integral)result.get(0) : null;
	}
	
	public Integral searchIntegralByPaymentFlowNo(String paymentFlowNo) {
		String hql = "from Integral where paymentFlowNo like ? and status = 3";
		List result = this.getHibernateTemplate().find(hql, paymentFlowNo);
		return result.size() > 0 ? (Integral)result.get(0) : null;
	}
	
	public void saveIntegral(Integral integral) {
		this.getHibernateTemplate().save(integral);
	}
	
	public void updateIntegral(Integral integral) {
		this.getHibernateTemplate().update(integral);
	}
}
