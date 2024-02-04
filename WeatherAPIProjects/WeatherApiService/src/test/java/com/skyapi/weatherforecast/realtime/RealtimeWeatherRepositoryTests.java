package com.skyapi.weatherforecast.realtime;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherforecast.common.RealTimeWeather;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RealtimeWeatherRepositoryTests {

	@Autowired
	private RealTimeWeatherRepository repo;
	
	@Test
	public void testUpdate() {
		String locationCode = "NYC_USA";
		
		RealTimeWeather realtimeWeather = repo.findById(locationCode).get();
		
		realtimeWeather.setTemperature(-2);
		realtimeWeather.setHumidity(32);
		realtimeWeather.setPrecipitation(42);
		realtimeWeather.setStatus("Snowy");
		realtimeWeather.setWindspeed(12);
		realtimeWeather.setLastUpdated(new Date());		
		
		RealTimeWeather updatedRealtimeWeather = repo.save(realtimeWeather);
		
		assertThat(updatedRealtimeWeather.getHumidity()).isEqualTo(32);
	}

	@Test
	public void testFindByCountryCodeAndCityNotFound() {
		String countryCode = "JP";
		String cityName = "Tokyo";
		
		RealTimeWeather realtimeWeather = repo.findByCountryCodeAndCity(countryCode, cityName);
		
		assertThat(realtimeWeather).isNull();
	}
	
	@Test
	public void testFindByCountryCodeAndCityFound() {
		String countryCode = "US";
		String cityName = "New York City";
		
		RealTimeWeather realtimeWeather = repo.findByCountryCodeAndCity(countryCode, cityName);
		
		assertThat(realtimeWeather).isNotNull();
		assertThat(realtimeWeather.getLocation().getCityname()).isEqualTo(cityName);
		
	}	
	
	@Test
	public void testFindByLocationNotFound() {
		String locationCode = "ABCXYZ";
		RealTimeWeather realtimeWeather = repo.findByLocationCode(locationCode);
		
		assertThat(realtimeWeather).isNull();
	}
	
	@Test
	public void testFindByTrashedLocationNotFound() {
		String locationCode = "NYC_USA";
		RealTimeWeather realtimeWeather = repo.findByLocationCode(locationCode);
		
		assertThat(realtimeWeather).isNull();
	}	
	
	@Test
	public void testFindByLocationFound() {
		String locationCode = "DELHI_IN";
		RealTimeWeather realtimeWeather = repo.findByLocationCode(locationCode);
		
		assertThat(realtimeWeather).isNotNull();
		assertThat(realtimeWeather.getLocationcode()).isEqualTo(locationCode);
	}	
}
