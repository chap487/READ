package com.racesaucy.read.persistence;

//
// *** Not used. Maybe need something like this for hibernate lazzy queries ***
//

import org.hibernate.SessionFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HibernateUtil {
	
	public static SessionFactory sessionFactory;
	
	private static boolean hasInitialized = false;
	public static SessionFactory createSessionFactory() throws Exception {
//	    ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//	    sessionFactory = (SessionFactory) context.getBean("sessionFactory");
//	    context.close();

		return sessionFactory;
	}

	public static void init() throws Exception {
		if (hasInitialized) return;
		createSessionFactory();
		hasInitialized = true;
	}
	
}
