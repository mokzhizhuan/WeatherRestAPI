package com.skyapi.weatherforecast.common;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "realtime_weather")
public class RealTimeWeather {

	@Id 
	@Column(name = "location_code")
	private String locationcode;
	
	private int temperature;
	
	private int humidity;
	
	private int precipitation;
	
	private int windspeed;
	
	@Column(length = 50)
	private String status;
	
	@JsonProperty("last_updated")
	private Date lastUpdated;
	
	@OneToOne
	@JoinColumn(name = "location_code")
	@MapsId
	private Location location;

	public String getLocationcode() {
		return locationcode;
	}

	public void setLocationcode(String locationcode) {
		this.locationcode = locationcode;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public int getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(int precipitation) {
		this.precipitation = precipitation;
	}

	public int getWindspeed() {
		return windspeed;
	}

	public void setWindspeed(int windspeed) {
		this.windspeed = windspeed;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public RealTimeWeather getByLocationCode(String locationCode2) {
		// TODO Auto-generated method stub
		return null;
	}
}
