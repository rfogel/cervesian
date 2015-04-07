package dao;

import model.ModelPersistency;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = HibernateException.class)
public class Dao 
{	
	protected static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
	private Transaction transaction;
	protected Session session;

	public Dao()
    {
        try
        {
            Configuration configuration = new Configuration().configure();
            serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        catch (HibernateException he)
        {
            System.err.println("Error creating Session: " + he);
            throw new ExceptionInInitializerError(he);
        }
    }	

	public SessionFactory getSessionFactory() {
		return Dao.sessionFactory;
	}

	public static void setSessionFactory(SessionFactory sessionFactory) {
		Dao.sessionFactory = sessionFactory;
	}

	public boolean save(ModelPersistency object) {
		try {
			if (object == null) {
				return false;
			}
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.save(object);
			transaction.commit();
			session.close();
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
			session.close();
			return false;
		}
		return true;
	}

	public boolean saveOrUpdate(ModelPersistency object) {
		try {
			if (object == null) {
				return false;
			}
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(object);
			transaction.commit();
			session.close();
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
			session.close();
			return false;
		}
		return true;
	}

	public boolean update(ModelPersistency object) {
		try {
			if (object == null) {
				return false;
			}
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.update(object);
			transaction.commit();
			session.close();
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
			session.close();
			return false;
		}
		return true;
	}

	public boolean delete(ModelPersistency object) {
		//try {
			if (object == null) {
				return false;
			}
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.delete(object);
			transaction.commit();
			session.close();
		/*} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
			session.close();
			return false;
		}*/
		return true;
	}
	

	public boolean databaseConnectionIsUp() {
		session = sessionFactory.openSession();
		if (session.isConnected() && session.isOpen()) {
			session.close();
			return true;
		}
		session.close();
		return false;
	}
}
