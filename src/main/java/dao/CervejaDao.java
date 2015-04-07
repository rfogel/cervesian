package dao;

import java.util.List;

import model.Cerveja;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import exception.DaoException;
import filter.CervejaFilter;

@Transactional(rollbackFor = DaoException.class)
public class CervejaDao extends Dao
{
	@SuppressWarnings("unchecked")
	public List<Cerveja> getCerveja() 
	{
		List<Cerveja> result = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from Cerveja c");
			result = query.list();
			session.close();
		} 
		catch (HibernateException e) 
		{
			e.printStackTrace();
			session.close();
		}

		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Cerveja> getCervejaByFilter(CervejaFilter filter) 
	{
		List<Cerveja> result = null;
		try
		{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from Cerveja c");
			result = query.list();
			session.close();
		} 
		catch (HibernateException e) 
		{
			e.printStackTrace();
			session.close();
		}

		return result;
	}
}
