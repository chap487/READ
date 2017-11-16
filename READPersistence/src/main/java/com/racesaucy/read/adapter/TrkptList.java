package com.racesaucy.read.adapter;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "trkptList")
@XmlAccessorType (XmlAccessType.FIELD)
public class TrkptList {

    @XmlElement(name = "trkpt")
	List<Trkpt> trkptList = null;

	public List<Trkpt> getTrkptList() {
		return trkptList;
	}

	public void setTrkptList(List<Trkpt> trkptList) {
		this.trkptList = trkptList;
	}
		
};