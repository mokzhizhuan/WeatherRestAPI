package com.skyapi.weatherforecast.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "location")
public class Location {

	@Column(length = 12, nullable = false, unique = true)
	@Id
	@NotBlank(message = "Code cannot be left blank")
	protected String code;
	
	@Column(length = 128, nullable = false, unique = true)
	@JsonProperty("city_name")
	@Length(min = 3 , max = 128 , message = "City name must be 3- 128 characters")
	protected String cityname;
	
	@Column(length = 128, nullable = false, unique = true)
	@JsonProperty("region_name")
	@Length(min = 3 , max = 128 , message = "Region name must be 3- 128 characters")
	protected String regionname;
	
	@Column(length = 64, nullable = false, unique = true)
	@JsonProperty("country_name")
	@Length(min = 3 , max = 128 , message = "Country name must be 3- 128 characters")
	protected String countryname;
	
	@Column(length = 2, nullable = false, unique = true)
	@JsonProperty("country_code")
	@Length(min = 2 , max = 2 , message = "Country Code must be 2 characters")
	protected String countrycode;
	
	protected boolean enabled;
	
	@JsonIgnore
	protected boolean trashed;
	
	@OneToOne(mappedBy = "location", cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private RealTimeWeather realtimeweather;
	
	@OneToMany(mappedBy = "id.location")
	private List<HourlyWeather> listHourlyWeather = new ArrayList<>();
	
	@OneToMany(mappedBy = "id.location")
	private List<DailyWeather> listDailyWeather = new ArrayList<>();

	public Location(@NotBlank String code, @NotBlank String cityname, @NotNull String regionname,
			@NotNull String countryname, @NotBlank String countrycode) {
		super();
		this.code = code;
		this.cityname = cityname;
		this.regionname = regionname;
		this.countryname = countryname;
		this.countrycode = countrycode;
	}

	public Location() {
	}

	public Location(String cityname, String regionname, String countryname, String countrycode) {
		this.cityname = cityname;
		this.regionname = regionname;
		this.countryname = countryname;
		this.countrycode = countrycode;

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public String getRegionname() {
		return regionname;
	}

	public void setRegionname(String regionname) {
		this.regionname = regionname;
	}

	public String getCountryname() {
		return countryname;
	}

	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}

	public String getCountrycode() {
		return countrycode;
	}

	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isTrashed() {
		return trashed;
	}

	public void setTrashed(boolean trashed) {
		this.trashed = trashed;
	}

	public RealTimeWeather getRealtimeweather() {
		return realtimeweather;
	}

	public void setRealtimeweather(RealTimeWeather realtimeweather) {
		this.realtimeweather = realtimeweather;
	}

	public List<HourlyWeather> getListHourlyWeather() {
		return listHourlyWeather;
	}

	public void setListHourlyWeather(List<HourlyWeather> listHourlyWeather) {
		this.listHourlyWeather = listHourlyWeather;
	}
	
	public List<DailyWeather> getListDailyWeather() {
		return listDailyWeather;
	}

	public void setListDailyWeather(List<DailyWeather> listDailyWeather) {
		this.listDailyWeather = listDailyWeather;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		return Objects.equals(code, other.code);
	}
	
	@Override
	public String toString() {
		return "Location [code=" + code + ", cityname=" + cityname + ", regionname=" + regionname + ", countryname="
				+ countryname + ", countrycode=" + countrycode + ", enabled=" + enabled + ", trashed=" + trashed + "]";
	}

	public Location code(String code) {
		setCode(code);
		return this;
	}
	
	public void copyFieldsFrom(Location another) {
		setCityname(another.getCityname());
		setRegionname(another.getRegionname());
		setCountrycode(another.getCountrycode());
		setCountryname(another.getCountryname());
		setEnabled(another.isEnabled());		
	}
	
	public void copyAllFieldsFrom(Location another) {
		copyFieldsFrom(another);
		setCode(another.getCode());
		setTrashed(another.isTrashed());
	}
}
