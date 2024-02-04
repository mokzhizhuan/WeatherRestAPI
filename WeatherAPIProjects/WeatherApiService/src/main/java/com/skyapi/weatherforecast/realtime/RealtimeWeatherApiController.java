package com.skyapi.weatherforecast.realtime;

import org.modelmapper.ModelMapper;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.CommonUtility;
import com.skyapi.weatherforecast.GeoLocationService;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealTimeWeather;
import com.skyapi.weatherforecast.hourly.HourlyWeatherApiController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/realtime")
public class RealtimeWeatherApiController {
	private GeoLocationService locationService;
	private RealTimeWeatherService realtimeWeatherService;
	private ModelMapper modelMapper;
	
	public RealtimeWeatherApiController(GeoLocationService locationService,
			RealTimeWeatherService realtimeWeatherService, ModelMapper modelMapper) {
		super();
		this.locationService = locationService;
		this.realtimeWeatherService = realtimeWeatherService;
		this.modelMapper = modelMapper;
	}

	@GetMapping
	public ResponseEntity<?> getRealtimeWeatherByIPAddress(HttpServletRequest request) {
		String ipAddress = CommonUtility.getIPAddress(request);
		
		Location locationFromIP = locationService.getLocation(ipAddress);
		RealTimeWeather realtimeWeather = realtimeWeatherService.getByLocation(locationFromIP);
		
		RealtimeWeatherDTO dto = modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
		
		return ResponseEntity.ok(addLinksByIP(dto));
	}
	
	@GetMapping("/{locationCode}")
	public ResponseEntity<?> getRealtimeWeatherByLocationCode(@PathVariable("locationCode") String locationCode) {
		RealTimeWeather realtimeWeather = realtimeWeatherService.getByLocationCode(locationCode);
		
		RealtimeWeatherDTO dto = entity2DTO(realtimeWeather);
		
		return ResponseEntity.ok(addLinksByLocation(dto, locationCode));
	}
	
	@PutMapping("/{locationCode}")
	public ResponseEntity<?> updateRealtimeWeather(@PathVariable("locationCode") String locationCode,
			@RequestBody @Valid RealtimeWeatherDTO dto) {
		
		RealTimeWeather realtimeWeather = dto2Entity(dto);
		realtimeWeather.setLocationcode(locationCode);
		
		RealTimeWeather updatedRealtimeWeather = realtimeWeatherService.update(locationCode, realtimeWeather);
		
		RealtimeWeatherDTO updatedDto = entity2DTO(updatedRealtimeWeather);
		return ResponseEntity.ok(addLinksByLocation(updatedDto, locationCode));
	}
	
	private RealtimeWeatherDTO entity2DTO(RealTimeWeather realtimeWeather) {
		return modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
	}
	
	private RealTimeWeather dto2Entity(RealtimeWeatherDTO dto) {
		return modelMapper.map(dto, RealTimeWeather.class);
	}
	
	private RealtimeWeatherDTO addLinksByIP(RealtimeWeatherDTO dto) {
		
		dto.add(linkTo(
						methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByIPAddress(null))
							.withSelfRel());
		
		dto.add(linkTo(
					methodOn(HourlyWeatherApiController.class).listHourlyForecastByIPAddress(null))
						.withRel("hourly_forecast"));
		
		/*dto.add(linkTo(
				methodOn(DailyWeatherApiController.class).listDailyForecastByIPAddress(null))
					.withRel("daily_forecast"));	
		
		dto.add(linkTo(
				methodOn(FullWeatherApiController.class).getFullWeatherByIPAddress(null))
					.withRel("full_forecast"));*/		
		
		return dto;
	}
	
	private RealtimeWeatherDTO addLinksByLocation(RealtimeWeatherDTO dto, String locationCode) {
		
		dto.add(linkTo(
						methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByLocationCode(locationCode))
							.withSelfRel());
		
		dto.add(linkTo(
					methodOn(HourlyWeatherApiController.class).listHourlyForecastByLocationCode(locationCode, null))
						.withRel("hourly_forecast"));
		
		/*dto.add(linkTo(
				methodOn(DailyWeatherApiController.class).listDailyForecastByLocationCode(locationCode))
					.withRel("daily_forecast"));	
		
		dto.add(linkTo(
				methodOn(FullWeatherApiController.class).getFullWeatherByLocationCode(locationCode))
					.withRel("full_forecast"));	*/
		
		return dto;
	}	
}
