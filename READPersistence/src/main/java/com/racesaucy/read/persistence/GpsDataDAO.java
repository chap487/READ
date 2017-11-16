package com.racesaucy.read.persistence;

import java.util.Date;
import java.util.List;

import com.racesaucy.read.domain.GpsData;
import com.racesaucy.read.domain.SessionPersist;

public interface GpsDataDAO {
	void persistGpsData(GpsData GpsData);
	GpsData findGpsDataById(Integer id);
	void updateGpsData(GpsData GpsData);
	void deleteGpsData(GpsData GpsData);
	
	void persistSessionPersist(SessionPersist sessionPersist);
	void persistSessionPersist(SessionPersist sessionPersist, List<GpsData> list);
	SessionPersist findSessionPersistById(Integer id);
	List<SessionPersist> findAllSessionPersistWithGpsData();
	void deleteSessionPersist(SessionPersist sessionPersist);
	List<GpsData> findGpsDataBySessionPersistId(Integer id);
	
	List<SessionPersist> findAllSessionPersist();
	List<GpsData> findGpsDataBySessionPersistIdWithSessionPersistData(Integer id);
	
	SessionPersist getNewSessionPersist(Date startDate, String Name);

	
	
}

