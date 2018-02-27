package com.racesaucy.read.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.racesaucy.read.web.utils.DateSerializer;


@Entity
@Table(name="session_persist") 
@Repository("sessionPersist")
@Scope("prototype")
//@JsonIgnoreProperties({"gpsDataList"})
// ,@UniqueConstraint(columnNames = "SESSION_PERSIST_ID"), example of SESSION_PERSIST_ID
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="sessionPersistId")
public class SessionPersist implements Serializable {
	private static final long serialVersionUID = 2L;
	
	public SessionPersist() {
	}
	
	public SessionPersist(int sessionPersistId, Date dateTime, Date startDateTime, Date endDateTime, String sessionName,
			String sessionDescription){ //, Set<GpsData> gpsDataList) {
		super();
		this.sessionPersistId = sessionPersistId;
		this.dateTime = dateTime;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.sessionName = sessionName;
		this.sessionDescription = sessionDescription;
		//this.gpsDataList = gpsDataList;
	}

	public SessionPersist(int sessionPersistId, Date dateTime, Date startDateTime, Date endDateTime, String sessionName,
			String sessionDescription, Set<GpsData> gpsDataList) {
		super();
		this.sessionPersistId = sessionPersistId;
		this.dateTime = dateTime;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.sessionName = sessionName;
		this.sessionDescription = sessionDescription;
		this.gpsDataList = gpsDataList;
	}

	@Id
	@Column(name="SESSION_PERSIST_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int sessionPersistId;

	@Column(name="DATE_TIME_CREATE")
	private Date dateTime;

	@Column(name="SESSION_START_DATE_TIME")
	private Date startDateTime;

	@Column(name="SESSION_END_DATE_TIME")
	private Date endDateTime;

	@Column(name="SESSION_NAME")
	private String sessionName;
		
	@Column(name="SESSION_DESCRIPTION")
	private String sessionDescription;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "sessionPersist")
	@OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
	@JsonManagedReference
	private Set<GpsData> gpsDataList = new HashSet<GpsData>();

	public Set<GpsData> getGpsDataList() {
		return this.gpsDataList;
	}

	public void setGpsDataList(Set<GpsData> gpsDataList) {
		this.gpsDataList = gpsDataList;
	}

	public int getSessionPersistId() {
		return sessionPersistId;
	}
	
	public void setSessionPersistId(int sessionPersistId) {
		this.sessionPersistId = sessionPersistId;
	}

	@JsonSerialize(using=DateSerializer.class)
	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	@JsonSerialize(using=DateSerializer.class)
	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	@JsonSerialize(using=DateSerializer.class)
	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public String getSessionDescription() {
		return sessionDescription;
	}

	public void setSessionDescription(String sessionDescription) {
		this.sessionDescription = sessionDescription;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

// ran into the following error when trying to use this method and need to research proper way to resolve	
//	org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: com.racesaucy.read.domain.SessionPersist.gpsDataList, could not initialize proxy - no Session
//	@Override
//	public String toString() {
//		StringBuilder s = new StringBuilder();
//		s.append("SessionPersist [sessionPersistId=" + sessionPersistId + ", dateTime=" + dateTime + ", startDateTime="
//				+ startDateTime + ", endDateTime=" + endDateTime + ", sessionName=" + sessionName
//				+ ", sessionDescription=" + sessionDescription + "\n");
// 		for (GpsData g: gpsDataList) {
//			s.append("\t" + g.toString() + "\n");
//		}
//		return s.toString();
//	}

}

