package com.skyapi.weatherforecast;

public class GeolocationException extends RuntimeException {

	public GeolocationException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeolocationException(String message) {
		super(message);
	}

}
