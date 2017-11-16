package com.racesaucy.read.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.racesaucy.read.domain.GpsData;
import com.racesaucy.read.domain.SessionPersist;
import com.racesaucy.read.persistence.GpsDataDAO;

@Service("gpsDataService")
public class GpsDataServiceImpl implements GpsDataService {

	@Autowired
	GpsDataDAO gpsDataDAO;

	@Override
	@Transactional
	public void persistGpsData(GpsData gpsData) {
		gpsDataDAO.persistGpsData(gpsData);
	}

	@Override
	@Transactional
	public void updateGpsData(GpsData gpsData) {
		gpsDataDAO.updateGpsData(gpsData);
	}

	@Override
	@Transactional
	public GpsData findGpsDataById(Integer id) {
		return gpsDataDAO.findGpsDataById(id);
	}

	
	@Override
	@Transactional
	public List<GpsData> findGpsDataBySessionPersistId(Integer id) {
		return gpsDataDAO.findGpsDataBySessionPersistId(id);
	};

	@Override
	@Transactional
	public void deleteGpsData(GpsData gpsData) {
		gpsDataDAO.deleteGpsData(gpsData);

	}
	
	@Override
	@Transactional
	public void persistSessionPersist(SessionPersist sessionPersist) {
		gpsDataDAO.persistSessionPersist(sessionPersist);
	}

	@Override
	@Transactional
	public SessionPersist findSessionPersistById(Integer id) {
		return gpsDataDAO.findSessionPersistById(id);

	}

	@Override
	@Transactional
	public void deleteSessionPersist(SessionPersist sessionPersist) {
		gpsDataDAO.deleteSessionPersist(sessionPersist);
		
	}

	@Override
	@Transactional
	public String toStringSessionPersistById(Integer id) {
		SessionPersist sp = gpsDataDAO.findSessionPersistById(id);
		
		sp.toString();
		
		StringBuilder s = new StringBuilder();
		s.append("SessionPersist [sessionPersistId=" + sp.getSessionPersistId() + ", dateTime=" + sp.getDateTime() + ", startDateTime="
				+ sp.getStartDateTime() + ", endDateTime=" + sp.getEndDateTime() + ", sessionName=" + sp.getSessionName()
				+ ", sessionDescription=" + sp.getSessionDescription() + "\n");
		for (GpsData g: sp.getGpsDataList()) {
			s.append("\t" + g.toString() + "\n");
		}
		return s.toString();
	}

	@Override
	@Transactional
	public void persistSessionPersist(SessionPersist sessionPersist, List<GpsData> list) {
		
		Date start = new Date();
		gpsDataDAO.persistSessionPersist(sessionPersist);
		
		for (GpsData g: list) {
			if (g ==null) continue;
			//System.out.println(g.toString());
			g.setSessionPersist(sessionPersist);
			gpsDataDAO.persistGpsData(g);
		}
		Date end = new Date();		
//		System.out.println("In persistSessionPersist() before persist: " + start.toString());
//		System.out.println("In persistSessionPersist() after persist: " + end.toString() +
//				"\n\n\tTotal database persist time: " + (end.getTime() - start.getTime())/1000.0);
	}

	@Override
	public List<SessionPersist> findAllSessionPersistWithGpsData() {
		return gpsDataDAO.findAllSessionPersistWithGpsData();
	}

	@Override
	public List<SessionPersist> findAllSessionPersist() {
		return gpsDataDAO.findAllSessionPersist();
	}

	@Override
	public SessionPersist getNewSessionPersist(Date start, String name) {
		return gpsDataDAO.getNewSessionPersist(start, name);
	}


}
