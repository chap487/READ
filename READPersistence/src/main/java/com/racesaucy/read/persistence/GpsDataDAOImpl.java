package com.racesaucy.read.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.racesaucy.read.domain.GpsData;
import com.racesaucy.read.domain.SessionPersist;

@Repository("gpsDataDAOImpl")
@Transactional
public class GpsDataDAOImpl implements GpsDataDAO {
	
	@Autowired
    private SessionFactory sessionFactory;
	
	@Override
	public void persistGpsData(GpsData gpsData) {
		sessionFactory.getCurrentSession().persist(gpsData);
	}
	
	@Override
	public GpsData findGpsDataById(Integer id){
		GpsData gpsData = null;
		gpsData = (GpsData) sessionFactory.getCurrentSession().get(GpsData.class, id);
		
	    //Hibernate.initialize(gpsData.getSessionPersist());  
	    
	    return gpsData;
	}
	
	@Override
	public List<GpsData> findGpsDataBySessionPersistId(Integer id){
		String hql="select gpsDataId,dateTime, latitude, latitudeDirection, longitude, longitudeDirection, altitude, legSpeed, legHeading, legTimeSecs, legLength  from GpsData where sessionPersist = " + id + " order by dateTime"; 
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		@SuppressWarnings("unchecked")

		List<GpsData> list = new ArrayList<GpsData>();
		Iterator<Object> itr=query.list().iterator();
		
		while (itr.hasNext()) {
			Object record[] = (Object[])itr.next();
			
			GpsData gpsData = new GpsData(
				(Integer) record[0],
				(Date) record[1],
				(Float) record[2],
				(String) record[3],
				(Float) record[4],
				(String) record[5],
				(Float) record[6],
				(Float) record[7],
				(Float) record[8],
				(Float) record[9],
				(Float) record[10]);

			list.add(gpsData);
		}
		return list;
	}
	
	@Override
	public List<GpsData> findGpsDataBySessionPersistIdWithSessionPersistData(Integer id){
		Query query = sessionFactory.getCurrentSession().createQuery(
			"from GpsData as g inner join g.sessionPersist as s where s.sessionPersistId = :sessionId");
		query.setParameter("sessionId", id);
		List<GpsData> list = query.list();
	    return list;
	}

	
	@Override
	public void updateGpsData(GpsData gpsData) {
		sessionFactory.getCurrentSession().update(gpsData);
	}
	
	@Override
	public void deleteGpsData(GpsData gpsData) {
		sessionFactory.getCurrentSession().delete(gpsData);
	}
	
	@Override
	public void persistSessionPersist(SessionPersist sessionPersist) {
		sessionFactory.getCurrentSession().persist(sessionPersist);
		
	}

	@Override
	public SessionPersist findSessionPersistById(Integer id) {
		SessionPersist sessionPersist = null;
		sessionPersist = (SessionPersist) sessionFactory.getCurrentSession().get(SessionPersist.class, id);
		
	    Hibernate.initialize(sessionPersist.getGpsDataList());  
	    
	    return sessionPersist;
	}

	@Override
	public void deleteSessionPersist(SessionPersist sessionPersist) {
		sessionFactory.getCurrentSession().delete(sessionPersist);
		
	}

	@Override
	public void persistSessionPersist(SessionPersist sessionPersist, List<GpsData> list) {
//		sessionFactory.getCurrentSession().getTransaction().begin();
		
		sessionFactory.getCurrentSession().save(sessionPersist);
		
		for (GpsData g: list) {
			g.setSessionPersist(sessionPersist);
			sessionFactory.getCurrentSession().save(g);
		}
		
//		sessionFactory.getCurrentSession().getTransaction().commit();
		
	}

	@Override
	public List<SessionPersist> findAllSessionPersistWithGpsData() {
		
		String hql="from SessionPersist"; // where sessionPersistId = 98"; 
		//String hql="from SessionPersist as s inner join GpsData as g where s.sessionPersistId = 100"; 
		//from SessionPersist";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		List<SessionPersist> sessionPersistList=query.list();
		return sessionPersistList;
	}

	
	public List<SessionPersist> findAllSessionPersist() {
		
		String hql="select sessionPersistId,dateTime, startDateTime, endDateTime, sessionName, sessionDescription from SessionPersist order by startDateTime desc"; 
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		
		List<SessionPersist> list = new ArrayList<SessionPersist>();
		Iterator<Object> itr=query.list().iterator();
		
		while (itr.hasNext()) {
			Object record[] = (Object[])itr.next();
			SessionPersist sessionPersist = new SessionPersist(
					(Integer)record[0],
					(Date)record[1],
					(Date)record[2],
					(Date)record[3],
					(String)record[4],
					(String)record[5]);
			list.add(sessionPersist);
		}
		return list;
	}

	@Override
	public SessionPersist getNewSessionPersist(Date startDate, String name) {
		
		SessionPersist sessionPersist = new SessionPersist();
		sessionPersist.setStartDateTime(startDate);
		sessionPersist.setDateTime(startDate);
		sessionPersist.setSessionName(name);
		sessionFactory.getCurrentSession().persist(sessionPersist);

		return sessionPersist;
	}


}
