package com.skyapi.weatherforecast.realtime;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforecast.GeoLocationService;
import com.skyapi.weatherforecast.GeolocationException;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealTimeWeather;
import com.skyapi.weatherforecast.location.LocationNotFoundException;

@WebMvcTest(RealtimeWeatherApiController.class)
public class RealtimeWeatherApiControllerTests {

	private static final String END_POINT_PATH = "/v1/realtime";
	private static final String RESPONSE_CONTENT_TYPE = "application/hal+json";
	private static final String REQUEST_CONTENT_TYPE = "application/json";
	
	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper mapper;
	
	@MockBean RealTimeWeatherService realtimeWeatherService;
	@MockBean GeoLocationService locationService;
	
	@Test
	public void testGetShouldReturnStatus400BadRequest() throws Exception {
		GeolocationException ex = new GeolocationException("Geolocation error");
		Mockito.when(locationService.getLocation(Mockito.anyString())).thenThrow(ex);
		
		mockMvc.perform(get(END_POINT_PATH))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]", is(ex.getMessage())))
				.andDo(print());		
	}
	
	@Test
	public void testGetShouldReturnStatus404NotFound() throws Exception {
		Location location = new Location();
		location.setCountrycode("US");
		location.setCityname("Tampa");
		
		LocationNotFoundException ex = new LocationNotFoundException(location.getCountrycode(), location.getCityname());
		
		Mockito.when(locationService.getLocation(Mockito.anyString())).thenReturn(location);
		Mockito.when(realtimeWeatherService.getByLocation(location)).thenThrow(ex);
		
		mockMvc.perform(get(END_POINT_PATH))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errors[0]", is(ex.getMessage())))
				.andDo(print());		
	}	
	
	@Test
	public void testGetShouldReturnStatus200OK() throws Exception {
		Location location = new Location();
		location.setCode("SFCA_USA");
		location.setCityname("San Franciso");
		location.setRegionname("California");
		location.setCountryname("United States of America");
		location.setCountrycode("US");
		
		RealTimeWeather realtimeWeather = new RealTimeWeather();
		realtimeWeather.setTemperature(12);
		realtimeWeather.setHumidity(32);
		realtimeWeather.setLastUpdated(new Date());
		realtimeWeather.setPrecipitation(88);
		realtimeWeather.setStatus("Cloudy");
		realtimeWeather.setWindspeed(5);
		
		realtimeWeather.setLocation(location);
		location.setRealtimeweather(realtimeWeather);
		
		
		Mockito.when(locationService.getLocation(Mockito.anyString())).thenReturn(location);
		Mockito.when(realtimeWeatherService.getByLocation(location)).thenReturn(realtimeWeather);
		
		String expectedLocation = location.getCityname() + ", " + location.getRegionname() + ", " + location.getCountryname();
		
		mockMvc.perform(get(END_POINT_PATH))
				.andExpect(status().isOk())
				.andExpect(content().contentType(RESPONSE_CONTENT_TYPE))
				.andExpect(jsonPath("$.location", is(expectedLocation)))
				.andExpect(jsonPath("$._links.self.href", is("http://localhost/v1/realtime")))
				.andExpect(jsonPath("$._links.hourly_forecast.href", is("http://localhost/v1/hourly")))
				.andExpect(jsonPath("$._links.daily_forecast.href", is("http://localhost/v1/daily")))
				.andExpect(jsonPath("$._links.full_forecast.href", is("http://localhost/v1/full")))
				.andDo(print());		
	}
	
	@Test
	public void testGetByLocationCodeShouldReturnStatus404NotFound() throws Exception {
		String locationCode = "ABC_US";
		
		LocationNotFoundException ex = new LocationNotFoundException(locationCode);
		Mockito.when(realtimeWeatherService.getByLocationCode(locationCode)).thenThrow(ex);
		
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		mockMvc.perform(get(requestURI))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errors[0]", is(ex.getMessage())))
				.andDo(print());		
	}	
	
	@Test
	public void testGetByLocationCodeShouldReturnStatus200OK() throws Exception {
		String locationCode = "SFCA_USA";
		
		Location location = new Location();
		location.setCode(locationCode);
		location.setCityname("San Franciso");
		location.setRegionname("California");
		location.setCountryname("United States of America");
		location.setCountrycode("US");
		
		RealTimeWeather realtimeWeather = new RealTimeWeather();
		realtimeWeather.setTemperature(12);
		realtimeWeather.setHumidity(32);
		realtimeWeather.setLastUpdated(new Date());
		realtimeWeather.setPrecipitation(88);
		realtimeWeather.setStatus("Cloudy");
		realtimeWeather.setWindspeed(5);
		
		realtimeWeather.setLocation(location);
		location.setRealtimeweather(realtimeWeather);
		
		
		Mockito.when(realtimeWeatherService.getByLocationCode(locationCode)).thenReturn(realtimeWeather);
		
		String expectedLocation = location.getCityname() + ", " + location.getRegionname() + ", " + location.getCountryname();
		
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		mockMvc.perform(get(requestURI))
				.andExpect(status().isOk())
				.andExpect(content().contentType(RESPONSE_CONTENT_TYPE))
				.andExpect(jsonPath("$.location", is(expectedLocation)))
				.andExpect(jsonPath("$._links.self.href", is("http://localhost/v1/realtime/" + locationCode)))
				.andExpect(jsonPath("$._links.hourly_forecast.href", is("http://localhost/v1/hourly/" + locationCode)))
				.andExpect(jsonPath("$._links.daily_forecast.href", is("http://localhost/v1/daily/" + locationCode)))
				.andExpect(jsonPath("$._links.full_forecast.href", is("http://localhost/v1/full/" + locationCode)))				
				.andDo(print());		
	}	
	
	@Test
	public void testUpdateShouldReturn400BadRequest() throws Exception {
		String locationCode = "ABC_US";
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		RealtimeWeatherDTO dto = new RealtimeWeatherDTO();
		dto.setTemperature(120);
		dto.setHumidity(132);
		dto.setPrecipitation(188);
		dto.setStatus("Cl");
		dto.setWindSpeed(500);
		
		String bodyContent = mapper.writeValueAsString(dto);
		
		mockMvc.perform(put(requestURI).contentType(REQUEST_CONTENT_TYPE).content(bodyContent))
			.andExpect(status().isBadRequest())
			.andDo(print());		
	}
	
	@Test
	public void testUpdateShouldReturn404NotFound() throws Exception {
		String locationCode = "ABC_US";
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		RealtimeWeatherDTO dto = new RealtimeWeatherDTO();
		dto.setTemperature(12);
		dto.setHumidity(32);
		dto.setPrecipitation(88);
		dto.setStatus("Cloudy");
		dto.setWindSpeed(5);
		
		LocationNotFoundException ex = new LocationNotFoundException(locationCode);
		Mockito.when(realtimeWeatherService.update(Mockito.eq(locationCode), Mockito.any())).thenThrow(ex);
		
		String bodyContent = mapper.writeValueAsString(dto);
		
		mockMvc.perform(put(requestURI).contentType(REQUEST_CONTENT_TYPE).content(bodyContent))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.errors[0]", is(ex.getMessage())))
			.andDo(print());		
	}	
	
	@Test
	public void testUpdateShouldReturn200OK() throws Exception {
		String locationCode = "SFCA_US";
		String requestURI = END_POINT_PATH + "/" + locationCode;
		
		RealTimeWeather realtimeWeather = new RealTimeWeather();
		realtimeWeather.setTemperature(12);
		realtimeWeather.setHumidity(32);
		realtimeWeather.setPrecipitation(88);
		realtimeWeather.setStatus("Cloudy");
		realtimeWeather.setWindspeed(5);
		realtimeWeather.setLastUpdated(new Date());
		
		RealtimeWeatherDTO dto = new RealtimeWeatherDTO();
		dto.setTemperature(12);
		dto.setHumidity(32);
		dto.setPrecipitation(88);
		dto.setStatus("Cloudy");
		dto.setWindSpeed(5);
		
		Location location = new Location();
		location.setCode(locationCode);
		location.setCityname("San Franciso");
		location.setRegionname("California");
		location.setCountryname("United States of America");
		location.setCountrycode("US");
		
		realtimeWeather.setLocation(location);
		location.setRealtimeweather(realtimeWeather);
		
		Mockito.when(realtimeWeatherService.update(locationCode, realtimeWeather)).thenReturn(realtimeWeather);
		
		String bodyContent = mapper.writeValueAsString(dto);
		
		String expectedLocation = location.getCityname() + ", " + location.getRegionname() + ", " + location.getCountryname();
		
		mockMvc.perform(put(requestURI).contentType("application/json").content(bodyContent))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.location", is(expectedLocation)))
			.andExpect(content().contentType(RESPONSE_CONTENT_TYPE))			
			.andExpect(jsonPath("$._links.self.href", is("http://localhost/v1/realtime/" + locationCode)))
			.andExpect(jsonPath("$._links.hourly_forecast.href", is("http://localhost/v1/hourly/" + locationCode)))
			.andExpect(jsonPath("$._links.daily_forecast.href", is("http://localhost/v1/daily/" + locationCode)))
			.andExpect(jsonPath("$._links.full_forecast.href", is("http://localhost/v1/full/" + locationCode)))			
			.andDo(print());		
	}	
}
