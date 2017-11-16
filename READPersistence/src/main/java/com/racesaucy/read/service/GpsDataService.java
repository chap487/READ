package com.racesaucy.read.service;

import java.util.Date;
import java.util.List;

import com.racesaucy.read.domain.GpsData;
import com.racesaucy.read.domain.SessionPersist;

public interface GpsDataService {
	public void persistGpsData(GpsData gpsData);
	public void updateGpsData(GpsData gpsData);
	public GpsData findGpsDataById(Integer id);
	List<GpsData> findGpsDataBySessionPersistId(Integer id);
	public void deleteGpsData(GpsData gpsData);
	
	public void persistSessionPersist(SessionPersist sessionPersist);
	public void persistSessionPersist(SessionPersist sessionPersist, List<GpsData> list);
	public SessionPersist findSessionPersistById(Integer id);
	public List<SessionPersist> findAllSessionPersistWithGpsData();
	public List<SessionPersist> findAllSessionPersist();
	public void deleteSessionPersist(SessionPersist sessionPersist);
	
	public String toStringSessionPersistById(Integer id);

	public SessionPersist getNewSessionPersist(Date start, String name);
}
