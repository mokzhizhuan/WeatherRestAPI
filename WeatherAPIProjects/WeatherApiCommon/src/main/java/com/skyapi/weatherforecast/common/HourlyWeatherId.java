package com.skyapi.weatherforecast.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Embeddable
public class HourlyWeatherId {

	private int hourOfDay;

	@ManyToOne
	@JoinColumn(name = "location_code")
	private Location location;
	
	public HourlyWeatherId() 
	{ 
		
	}

	public HourlyWeatherId(int hourOfDay, Location location) {
		this.hourOfDay = hourOfDay;
		this.location = location;
	}



	public int getHourOfDay() {
		return hourOfDay;
	}

	public void setHourOfDay(int hourOfDay) {
		this.hourOfDay = hourOfDay;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
