package com.onetech.intelinfo.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import com.onetech.intelinfo.model.Employee;


public class EmployeeDaoImpl implements EmployeeDao{
	
	@Autowired
	private SessionFactory sessionFactory;
	Session session;
	

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public EmployeeDaoImpl(){}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public EmployeeDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
	
	public void save(Employee emp) {
		session = sessionFactory.openSession();
		Transaction tx = null;
		int id = 0;
		
		try {
			tx = session.beginTransaction();
			id = (Integer) session.save(emp);
			tx.commit();
		} catch (Exception e) {
			if(tx != null)
				tx.rollback();
		}
		finally{
			System.out.println("the dastabase with the id "+ id +" integrated successfully");
			session.close();
		}
		
	}
}
