package com.skyapi.weatherforecast.realtime;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealTimeWeather;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationRepository;

@Service
public class RealTimeWeatherService {
	private RealTimeWeatherRepository realtimeWeatherRepo;
	private LocationRepository locationRepo;

	public RealTimeWeatherService(RealTimeWeatherRepository realtimeWeatherRepo, LocationRepository locationRepo) {
		super();
		this.realtimeWeatherRepo = realtimeWeatherRepo;
		this.locationRepo = locationRepo;
	}

	public RealTimeWeather getByLocation(Location location) {
		String countryCode = location.getCountrycode();
		String cityName = location.getCityname();
		
		RealTimeWeather realtimeWeather = realtimeWeatherRepo.findByCountryCodeAndCity(countryCode, cityName);
		
		if (realtimeWeather == null) {
			throw new LocationNotFoundException(countryCode, cityName);
		}
		
		return realtimeWeather;
	}
	
	public RealTimeWeather getByLocationCode(String locationCode) {
		RealTimeWeather realtimeWeather = realtimeWeatherRepo.findByLocationCode(locationCode);
		
		if (realtimeWeather == null) {
			throw new LocationNotFoundException(locationCode);
		}
		
		return realtimeWeather;
	}
	
	public RealTimeWeather update(String locationCode, RealTimeWeather realtimeWeather) {
		Location location = locationRepo.findbyCode(locationCode);
		
		if (location == null) {
			throw new LocationNotFoundException(locationCode);
		}
		
		realtimeWeather.setLocation(location);
		realtimeWeather.setLastUpdated(new Date());
		
		if (location.getRealtimeweather() == null) {
			location.setRealtimeweather(realtimeWeather);
			Location updatedLocation = locationRepo.save(location);
			
			return updatedLocation.getRealtimeweather();
		}
		
		return realtimeWeatherRepo.save(realtimeWeather);
	}
}
