package com.skyapi.weatherforecast.realtime;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.skyapi.weatherforecast.common.RealTimeWeather;

public interface RealTimeWeatherRepository extends CrudRepository<RealTimeWeather, String>{
	
	@Query("SELECT r FROM RealTimeWeather r WHERE r.location.countrycode = ?1 AND r.location.cityname = ?2")
	public RealTimeWeather findByCountryCodeAndCity(String countryCode, String city);
	
	@Query("SELECT r FROM RealTimeWeather r WHERE r.id = ?1 AND r.location.trashed = false")
	public RealTimeWeather findByLocationCode(String locationCode);
	
}
