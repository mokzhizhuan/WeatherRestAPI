package com.skyapi.weatherforecast.common;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class DailyWeatherId {

	private int dayofMonth;
	
	private int month;
	
	@ManyToOne
	@JoinColumn(name = "location_code")
	private Location location;
	
	public DailyWeatherId(int dayofMonth, int month, Location location) {
		super();
		this.dayofMonth = dayofMonth;
		this.month = month;
		this.location = location;
	}

	public DailyWeatherId() 
	{
	
	}

	public int getDayofMonth() {
		return dayofMonth;
	}

	public void setDayofMonth(int dayofMonth) {
		this.dayofMonth = dayofMonth;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
