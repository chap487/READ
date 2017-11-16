package com.racesaucy.read.adapter;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Trkpt {
	String lat;
	String lon;
	double ele;
	String time;
	
	Date trkPtDate;
	
	public String getLat() {
		return lat;
	}
	
	@XmlAttribute
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	@XmlAttribute
	public void setLon(String lon) {
		this.lon = lon;
	}
	public double getEle() {
		return ele;
	}
	@XmlElement
	public void setEle(double ele) {
		this.ele = ele;
	}
	public String getTime() {
		return time;
	}
	@XmlElement
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "Trkpt [lat=" + lat + ", lon=" + lon + ", ele=" + ele + ", time=" + time + "]";
	}

	public Date getTrkPtDate() {
		return trkPtDate;
	}

	public void setTrkPtDate(Date trkPtDate) {
		this.trkPtDate = trkPtDate;
	}
	
};

