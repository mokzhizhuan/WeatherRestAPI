package com.skyapi.weatherforecast.location;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.skyapi.weatherforecast.common.Location;

public interface LocationRepository extends FilterableLocationRepository, CrudRepository<Location, String>, 
PagingAndSortingRepository<Location, String> {

	@Query("SELECT l FROM Location l WHERE l.trashed = false")
	public List<Location> findlocations();
	
	@Query("SELECT l FROM Location l WHERE l.trashed = false AND l.code = ?1")
	public Location findbyCode(String code);
	
	@Query("Update Location l SET l.trashed = true AND l.code = ?1")
	public void trashbyCode(String code);

	@Query("SELECT l FROM Location l WHERE l.countrycode = ?1 AND l.cityname = ?2 AND l.trashed = false")
	public Location findByCountryCodeAndCityName(String countryCode, String cityName);

	@Query("SELECT l FROM Location l WHERE l.trashed = false")
	@Deprecated
	public Page<Location> findUntrashed(Pageable pageable);	
	
	@Query("SELECT l FROM Location l WHERE l.trashed = false")
	@Deprecated
	List<Location> findUntrashed();
}
