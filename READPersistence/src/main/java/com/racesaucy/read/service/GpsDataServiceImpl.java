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
	public void updateSessionPersist(SessionPersist sessionPersist) {
		gpsDataDAO.updateSessionPersist(sessionPersist);
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
		
		SessionPersist sessionPersistExisting = null;
		
		if(sessionPersist.getSessionPersistId() > 0) {
			sessionPersistExisting = gpsDataDAO.findSessionPersistById(sessionPersist.getSessionPersistId());
		}
		
		if (sessionPersistExisting != null) {
			sessionPersist = sessionPersistExisting;
		} else {
			gpsDataDAO.persistSessionPersist(sessionPersist);
		}
		
		for (GpsData g: list) {
			if (g ==null) continue;
			//System.out.println(g.toString());
			g.setSessionPersist(sessionPersist);
			gpsDataDAO.persistGpsData(g);
		}
	}

	@Override
	@Transactional
	public void persistGpsDataList(List<GpsData> list) {
		
		for (GpsData g: list) {
			if (g ==null) continue;
			gpsDataDAO.persistGpsData(g);
		}
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
	
	@Override
	public int storeJsonSessionGPSData(String jsonString) {
		return gpsDataDAO.storeJsonSessionGPSData(jsonString);

	}



}
