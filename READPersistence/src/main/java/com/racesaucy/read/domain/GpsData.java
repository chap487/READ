package com.racesaucy.read.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.racesaucy.read.web.utils.DateSerializer;


@Entity
@Table(name="gps_data")
@Scope("prototype")
@Repository("gpsData")
//@NamedQuery(name="FoodItem.findAll", query="SELECT f FROM FoodItem f")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="gpsDataId")
public class GpsData 
	implements Serializable {
	private static final long serialVersionUID = 1L;

	// If I implement Serializable, than no gps data is ouput, but still getting:
	// com.fasterxml.jackson.databind.JsonMappingException: failed to lazily initialize a collection of role

	@Id
	@Column(name="GPS_DATA_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int gpsDataId;

	@Column(name="DATE_TIME")
	private Date dateTime;
	@Column(name="LATITUDE")
	private Float latitude;
	@Column(name="LATITUDE_DIRECTION")
	private String latitudeDirection;
	@Column(name="LONGITUDE") 
	private Float longitude;
	@Column(name="LONGITUDE_DIRECTION") 
	private String longitudeDirection;
	@Column(name="ALTITUDE")
	private Float altitude;
	
	@Column(name="LEG_SPEED")
	private Float legSpeed;
	@Column(name="LEG_HEADING")
	private Float legHeading;
	@Column(name="LEG_TIME_SECS")
	private Float legTimeSecs;
	@Column(name="LEG_LENGTH")
	private Float legLength;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SESSION_PERSIST_ID")
	@PrimaryKeyJoinColumn
	// To Solve JSON infinite recursion Stackoverflow (with Spring and Jackson annotations) added:
	@JsonBackReference
	private SessionPersist sessionPersist;
	public SessionPersist getSessionPersist() {
		return sessionPersist;
	}

	public void setSessionPersist(SessionPersist sessionPersist) {
		this.sessionPersist = sessionPersist;
	}

	
	public GpsData() {
	}
	
	public GpsData(int gpsDataId,Date dateTime,Float latitude,String latitudeDirection,Float longitude,
			String longitudeDirection,Float altitude,Float legSpeed,Float legHeading,Float legTimeSecs,Float legLength) {
		this();
		this.gpsDataId = gpsDataId;
		this.dateTime = dateTime;
		this.latitude = latitude;
		this.latitudeDirection = latitudeDirection;
		this.longitude = longitude;
		this.longitudeDirection= longitudeDirection;
		this.altitude = altitude;
		this.legSpeed = legSpeed;
		this.legHeading = legHeading;
		this.legTimeSecs = legTimeSecs;
		this.legLength = legLength;
	}
	
	public GpsData(GpsData other) {
		this.gpsDataId = other.gpsDataId;
		this.dateTime = other.dateTime;
		this.latitude = other.latitude;
		this.latitudeDirection = other.latitudeDirection;
		this.longitude = other.longitude;
		this.longitudeDirection= other.longitudeDirection;
		this.altitude = other.altitude;
		this.legSpeed = other.legSpeed;
		this.legHeading = other.legHeading;
		this.legTimeSecs = other.legTimeSecs;
		this.legLength = other.legLength;
	}


	public int getGpsDataId() {
		return gpsDataId;
	}

	@SuppressWarnings("unused")
	private void setGpsDataId(int gpsDataId) {
		this.gpsDataId = gpsDataId;
	}

	@JsonSerialize(using=DateSerializer.class)
	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public String getLatitudeDirection() {
		return latitudeDirection;
	}

	public void setLatitudeDirection(String latitudeDirection) {
		this.latitudeDirection = latitudeDirection;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public String getLongitudeDirection() {
		return longitudeDirection;
	}

	public void setLongitudeDirection(String longitudeDirection) {
		this.longitudeDirection = longitudeDirection;
	}

	public Float getAltitude() {
		return altitude;
	}

	public void setAltitude(Float altitude) {
		this.altitude = altitude;
	}

	public Float getLegSpeed() {
		return legSpeed;
	}

	public void setLegSpeed(Float legSpeed) {
		this.legSpeed = legSpeed;
	}

	public Float getLegHeading() {
		return legHeading;
	}

	public void setLegHeading(Float legHeading) {
		this.legHeading = legHeading;
	}

	public Float getLegTimeSecs() {
		return legTimeSecs;
	}

	public void setLegTimeSecs(Float legTimeSecs) {
		this.legTimeSecs = legTimeSecs;
	}

	public Float getLegLength() {
		return legLength;
	}

	public void setLegLength(Float legLength) {
		this.legLength = legLength;
	}

	@Override
	public String toString() {
		return "GpsData [gpsDataId=" + gpsDataId + ", dateTime=" + dateTime + ", latitude=" + latitude
				+ ", latitudeDirection=" + latitudeDirection + ", longitude=" + longitude + ", longitudeDirection="
				+ longitudeDirection + ", altitude=" + altitude + ", legSpeed=" + legSpeed + ", legHeading="
				+ legHeading + ", legTimeSecs=" + legTimeSecs + ", legLength=" + legLength + "]";
				//", sessionPersist=" + sessionPersist + "]";
	}

	public void copyValues(GpsData other) {
		
		this.gpsDataId = other.gpsDataId;
		this.dateTime = other.dateTime;
		this.latitude = other.latitude;
		this.latitudeDirection = other.latitudeDirection;
		this.longitude = other.longitude;
		this.longitudeDirection= other.longitudeDirection;
		this.altitude = other.altitude;
		this.legSpeed = other.legSpeed;
		this.legHeading = other.legHeading;
		this.legTimeSecs = other.legTimeSecs;
		this.legLength = other.legLength;
	}


}